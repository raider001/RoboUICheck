package com.kalynx.snagtest.screen;

import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.manager.DisplayManager;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.Rectangle;

public class Ocr {
    private final Tesseract tesseract;
    private final CvMonitor cvMonitor;
    private final DisplayManager displayManager;

    public Ocr(Tesseract tesseract, CvMonitor cvMonitor, DisplayManager displayManager) {
        this.tesseract = tesseract;
        this.cvMonitor = cvMonitor;
        this.displayManager = displayManager;
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
    }

    public String getText() throws TesseractException {
        return tesseract.doOCR(cvMonitor.capture());
    }

    public String getTextFromImage(String imageName) throws Exception {
        
        Result<ScreenshotData> data = cvMonitor.monitorForImage(imageName);
        if (data.isFailure()) throw new Exception(data.getInfo());

        Rectangle originalRegion = new Rectangle(displayManager.getSelectedDisplayRegion().displayRegion());
        Rectangle foundLocation = data.getData().foundLocation();
        displayManager.setCaptureRegion(foundLocation);
        String text = tesseract.doOCR(cvMonitor.capture());
        displayManager.setCaptureRegion(originalRegion);
        return text;
    }

    public void setPsm(int psm) {
        tesseract.setPageSegMode(psm);
    }

    public void setOcrEngineMode(int mode) {
        tesseract.setOcrEngineMode(mode);
    }
}
