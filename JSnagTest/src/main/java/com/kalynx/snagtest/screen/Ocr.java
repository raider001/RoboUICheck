package com.kalynx.snagtest.screen;

import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.manager.DisplayManager;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.Word;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

public class Ocr {
    private final Tesseract tesseract;
    private final CvMonitor cvMonitor;
    private final DisplayManager displayManager;
    private int psm = 3;

    public Ocr(Tesseract tesseract, CvMonitor cvMonitor, DisplayManager displayManager) {
        this.tesseract = tesseract;
        this.cvMonitor = cvMonitor;
        this.displayManager = displayManager;
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
    }

    public Rectangle getWordRegion(String word) throws Exception {
        BufferedImage img = cvMonitor.capture();
        List<Word> words = tesseract.getWords(img, psm);
        List<Word> matches = words.stream().filter(w -> w.getText().equals(word)).toList();
        if (matches.size() > 1) throw new Exception("Word" + word + " found in multiple places");
        if (matches.isEmpty()) throw new Exception("Word" + word + " not found");
        return matches.get(0).getBoundingBox();
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
        this.psm = psm;
        tesseract.setPageSegMode(psm);
    }

    public void setOcrEngineMode(int mode) {
        tesseract.setOcrEngineMode(mode);
    }

    public void setLanguage(String language) {
        tesseract.setLanguage(language);
    }
}
