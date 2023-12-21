package com.kalynx.snagtest.keywordsold;

import com.kalynx.snagtest.SnagTestOld;
import com.kalynx.snagtest.screen.Ocr;
import net.sourceforge.tess4j.TesseractException;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class OcrKeywords {

    private final Ocr OCR = SnagTestOld.DI.getDependency(Ocr.class);

    @RobotKeyword("""
            Get Text
            Retrieves all text from the defined display region.
            The region can be defined with 'Set Capture Region' keyword.
            """)
    public String getText() throws TesseractException {
        return OCR.getText();
    }

    @RobotKeyword("""
            Get Text From Image
            """)
    public String getTextFromImage(String imageName) throws Exception {
        return OCR.getTextFromImage(imageName);
    }
}
