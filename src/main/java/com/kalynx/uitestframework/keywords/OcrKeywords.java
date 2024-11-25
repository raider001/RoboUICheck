package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.ValidationUtils;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.OcrController;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.OcrMode;
import com.kalynx.uitestframework.data.SegmentationMode;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import com.kalynx.uitestframework.exceptions.OcrException;
import com.kalynx.uitestframework.exceptions.WindowException;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RobotKeywords
public class OcrKeywords {
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
    private static final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);
    private static final OcrController OCR_CONTROLLER = DI.getInstance().getDependency(OcrController.class);

    @RobotKeyword("""
            Gets all the words found on the display in a format containing the name of the word and its location.
            Based on the parameters provided, the words will be extracted from the display, window, or a specific region.
            The dimensions of the region can be specified using the x, y, width, and height parameters and act as offsets from the top-left corner of the display or window.
            All options are optional, but where a dimension is provided, all dimensions must also be provided.
            Display and window cannot be defined at the same time.
            """)
    @ArgumentNames({"display=", "window=", "x=", "y=", "width=", "height="})
    public List<Map<String, Object>> getWords(String display, String window, Integer x, Integer y, Integer width, Integer height) throws DisplayNotFoundException, OcrException, WindowException {
        BufferedImage img = determineCaptureRegion(display, window, x, y, width, height);
        return getWords(img);
    }

    @RobotKeyword("""
            Returns all the text read from on the screen
            Based on the parameters provided, the words will be extracted from the display, window, or a specific region.
            The dimensions of the region can be specified using the x, y, width, and height parameters and act as offsets from the top-left corner of the display or window.
            All options are optional, but where a dimension is provided, all dimensions must also be provided.
            Display and window cannot be defined at the same time.
            """)
    @ArgumentNames({"display=", "window=", "x=", "y=", "width=", "height="})
    public String getText(String display, String window, Integer x, Integer y, Integer width, Integer height) throws DisplayNotFoundException, TesseractException, OcrException, WindowException {
        BufferedImage img = determineCaptureRegion(display, window, x, y, width, height);
        return OCR_CONTROLLER.readText(img);
    }

    @RobotKeyword("""
            Set OCR Mode
            Available Options:
            - OEM_TESSERACT_ONLY
            - OEM_LSTM_ONLY
            - OEM_TESSERACT_LSTM_COMBINED
            - OEM_DEFAULT
            """)
    @ArgumentNames({"ocrMode"})
    public void setOcrMode(String val) {
        OcrMode mode;
        try {
            mode = OcrMode.valueOf(val.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OCR Mode.");
        }
        OCR_CONTROLLER.setOcrMode(mode.ordinal());
    }

    @RobotKeyword("""
            Set Page Segmentation Mode
            Available Options:
            - OSD_ONLY
            - AUTO_OSD
            - AUTO_ONLY
            - AUTO
            - SINGLE_COLUMN
            - SINGLE_BLOCK_VERT_TEXT
            - SINGLE_BLOCK
            - SINGLE_LINE
            - SINGLE_WORD
            - CIRCLE_WORD
            - SINGLE_CHAR
            - SPARSE_TEXT
            - SPARSE_TEXT_OSD
            - RAW_LINE
            - COUNT
            """)
    @ArgumentNames({"pageSegMode"})
    public void setPageSegMode(String val) {
        SegmentationMode mode;
        try {
            mode = SegmentationMode.valueOf(val.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Segmentation Mode.");
        }
        OCR_CONTROLLER.setPageSegMode(mode.ordinal());
    }

    @RobotKeyword("""
            Sets the OCR Trained Data Path
            """)
    @ArgumentNames({"path"})
    public void setDataPath(String path) {
        OCR_CONTROLLER.setDataPath(path);
    }

    private List<Map<String, Object>> getWords(BufferedImage image) {
        List<Word> words = OCR_CONTROLLER.getWords(image);
        List<Map<String, Object>> data = new ArrayList<>();

        for (Word word : words) {
            Map<String, Object> wordData = Map.of(
                    "text", word.getText().replace("\n", "").trim(),
                    "x", word.getBoundingBox().getX(),
                    "y", word.getBoundingBox().getY(),
                    "confidence", word.getConfidence(),
                    "width", word.getBoundingBox().getWidth(),
                    "height", word.getBoundingBox().getHeight());
            data.add(wordData);
        }
        return data.stream().filter(word -> !word.get("text").toString().isEmpty()).toList();
    }


    private void translateRegionIfDimensionsDefined(Integer x, Integer y, Integer width, Integer height, Rectangle r) {
        if (x != null) {
            r.x += x;
            r.y += y;
            r.width = width;
            r.height = height;
        }
    }

    private BufferedImage determineCaptureRegion(String display, String window, Integer x, Integer y, Integer width, Integer height) throws OcrException, DisplayNotFoundException, WindowException {
        ValidationUtils.performBasicValidation(display, window, x, y, width, height);
        Rectangle r;
        if (display != null) {
            DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(display);
            r = new Rectangle(DISPLAY_MANAGER.getDisplayDisplayRegion(attr).displayRegion());
            translateRegionIfDimensionsDefined(x, y, width, height, r);
        } else if (window != null) {
            r = WINDOW_CONTROLLER.getWindowDimensions(window);
            translateRegionIfDimensionsDefined(x, y, width, height, r);
        } else if (x != null) {
            r = new Rectangle(x, y, width, height);
        } else {
            DisplayAttributes attr = DISPLAY_MANAGER.getSelectedDisplay();
            DisplayManager.DisplayData dd = DISPLAY_MANAGER.getDisplayDisplayRegion(attr);
            r = new Rectangle(dd.displayRegion());
        }

        return DISPLAY_MANAGER.capture(r);
    }

}
