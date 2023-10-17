package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.SnagTest;
import com.kalynx.snagtest.screen.Ocr;
import net.sourceforge.tess4j.TesseractException;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class OcrKeywords {

    private final Ocr OCR = SnagTest.DI.getDependency(Ocr.class);

    @RobotKeyword("""
            Set PSM
            Existing Modes
            0  OSD_ONLY Orientation and script detection (OSD) only.
            1  AUTO_OSD Automatic page segmentation with OSD.
            2  AUTO_ONLY Automatic page segmentation, but no OSD, or OCR.
            3  AUTO Fully automatic page segmentation, but no OSD. (Default)
            4  SINGLE_COLUMN Assume a single column of text of variable sizes.
            5  SINGLE_BLOCK_VERT_TEXT Assume a single uniform block of vertically aligned text.
            6  SINGLE_COLUMN Assume a single uniform block of text.
            7  SINGLE_LINE Treat the image as a single text line.
            8  SINGLE_WORD Treat the image as a single word.
            9  CIRCLE_WORD Treat the image as a single word in a circle.
            10 SINGLE_CHAR Treat the image as a single character.
            11 SPARSE_TEXT Sparse text. Find as much text as possible in no particular order.
            12 SPARSE_TEXT_OSD Sparse text with OSD.
            13 RAW_LINE Raw line. Treat the image as a single text line, bypassing hacks that are Tesseract-
            """)
    @ArgumentNames({"psm"})
    public boolean setPsm(int psm) {
        return false;
    }

    @RobotKeyword("""
            """)
    public void getText() throws TesseractException {
        OCR.getText();
    }
}
