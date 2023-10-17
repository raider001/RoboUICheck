package com.kalynx.snagtest.screen;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class Ocr {
    private final Tesseract tesseract;
    private final CvMonitor cvMonitor;

    public Ocr(Tesseract tesseract, CvMonitor cvMonitor) {
        this.tesseract = tesseract;
        this.cvMonitor = cvMonitor;
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
    }

    public String getText() throws TesseractException {
        return tesseract.doOCR(cvMonitor.capture());
    }

    public void setPsm(int psm) {
        tesseract.setPageSegMode(psm);
    }

    public void setOcrEngineMode(int mode) {
        tesseract.setOcrEngineMode(mode);
    }
}
