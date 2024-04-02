package com.kalynx.uitestframework.controller;

import com.kalynx.uitestframework.exceptions.UnsupportedOS;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.LoadLibs;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class OcrController {
    private final Tesseract tesseract;

    public OcrController(Tesseract tesseract) throws UnsupportedOS, IOException {
        this.tesseract = tesseract;

        String os = System.getProperty("os.name").toLowerCase();
        File tmpFolder;
        if (os.contains("win")) {
            tmpFolder = LoadLibs.extractTessResources("win32-x86-64");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            throw new UnsupportedOS();
        } else {
            throw new UnsupportedOS();
        }
        System.setProperty("java.library.path", tmpFolder.getPath());
        File tessdataFolder = new File("./tessdata");
        URL input = getClass().getResource("/tessdata/eng.traineddata");
        FileUtils.copyURLToFile(input, new File(tessdataFolder.getPath() + "/eng.traineddata"));
        this.tesseract.setDatapath(tessdataFolder.getAbsolutePath());
        this.tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);
    }

    public String readText(BufferedImage image) throws TesseractException {
        return tesseract.doOCR(image);
    }

    public List<Word> getWords(BufferedImage image) {
        return tesseract.getWords(image,0);
    }

    public void setOcrMode(int mode) {
        tesseract.setOcrEngineMode(mode);
    }
    public void setPageSegMode(int mode) {
        tesseract.setPageSegMode(mode);
    }

    public void setLanguage(String language) {
        tesseract.setLanguage(language);
    }

    public void setDataPath(String tessDataPath) {
        tesseract.setDatapath(tessDataPath);
    }
}
