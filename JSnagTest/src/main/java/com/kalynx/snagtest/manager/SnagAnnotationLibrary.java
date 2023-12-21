package com.kalynx.snagtest.manager;

import org.robotframework.javalib.library.AnnotationLibrary;

public class SnagAnnotationLibrary extends AnnotationLibrary {


    public SnagAnnotationLibrary() {
        super("com/kalynx/snagtest/keywordsold/*.class");
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__")) {
            return """
                    A UI Test Library using Computer Vision and OCR.
                    This library uses OpenCV to find and click on components and Tesseract to read text from the screen.
                    """;
        }
        return super.getKeywordDocumentation(keywordName);
    }
}
