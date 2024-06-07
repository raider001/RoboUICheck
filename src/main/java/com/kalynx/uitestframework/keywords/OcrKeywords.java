package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.OcrController;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.OcrMode;
import com.kalynx.uitestframework.data.SegmentationMode;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
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

    @RobotKeyword("Gets all the words found on the display in a format containing the name of the word and its location.")
    public List<Map<String,Object>> getWords() throws DisplayNotFoundException, TesseractException {
        DisplayAttributes attr = DISPLAY_MANAGER.getSelectedDisplay();
        DisplayManager.DisplayData dd = DISPLAY_MANAGER.getDisplayDisplayRegion(attr);
        BufferedImage img = DISPLAY_MANAGER.capture(dd.displayRegion());
        return getWords(img);
    }

    @RobotKeyword("Returns all the text read from on the screen")
    public String getText() throws DisplayNotFoundException, TesseractException {
        DisplayAttributes attr = DISPLAY_MANAGER.getSelectedDisplay();
        DisplayManager.DisplayData data = DISPLAY_MANAGER.getDisplayDisplayRegion(attr);
        BufferedImage img = DISPLAY_MANAGER.capture(data.displayRegion());
        return OCR_CONTROLLER.readText(img);
    }

    @RobotKeyword("Get Text From Display")
    @ArgumentNames({"display"})
    public String getTextFromDisplay(String display) throws DisplayNotFoundException, TesseractException {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(display);
        DisplayManager.DisplayData data = DISPLAY_MANAGER.getDisplayDisplayRegion(attr);
        BufferedImage img = DISPLAY_MANAGER.capture(data.displayRegion());
        return OCR_CONTROLLER.readText(img);
    }

    @RobotKeyword("Get Words From Display")
    @ArgumentNames({"display"})
    public List<Map<String,Object>> getWordsFromDisplay(String display) throws DisplayNotFoundException, TesseractException {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(display);
        DisplayManager.DisplayData dd = DISPLAY_MANAGER.getDisplayDisplayRegion(attr);
        BufferedImage img = DISPLAY_MANAGER.capture(dd.displayRegion());
        return getWords(img);
    }

    @RobotKeyword("Get Text From Form")
    @ArgumentNames({"form"})
    public String getTextFromForm(String form) throws WindowException, DisplayNotFoundException, TesseractException {
        Rectangle r = WINDOW_CONTROLLER.getWindowDimensions(form);
        BufferedImage img = DISPLAY_MANAGER.capture(r);
        return OCR_CONTROLLER.readText(img);
    }

    @RobotKeyword("Get Words From Form")
    @ArgumentNames({"form"})
    public List<Map<String, Object>> getWordsFromForm(String form) throws WindowException, DisplayNotFoundException, TesseractException {
        Rectangle r = WINDOW_CONTROLLER.getWindowDimensions(form);
        BufferedImage img = DISPLAY_MANAGER.capture(r);
        return getWords(img);
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
        OcrMode mode = null;
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
        SegmentationMode mode = null;
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

    private List<Map<String,Object>> getWords(BufferedImage image) throws TesseractException {
        List<Word> words = OCR_CONTROLLER.getWords(image);
        List<Map<String, Object>> data = new ArrayList<>();

        for(Word word : words) {
            Map<String, Object> wordData = Map.of(
                    "text", word.getText().replace("\n", "").trim(),
                    "x", word.getBoundingBox().getX(),
                    "y", word.getBoundingBox().getY(),
                    "confidence", word.getConfidence(),
                    "width", word.getBoundingBox().getWidth(),
                    "height", word.getBoundingBox().getHeight());
            data.add(wordData);
        }
        return data.stream().filter(word -> !word.get("text").toString().equals("")).toList();
    }
}
