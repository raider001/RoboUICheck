package com.kalynx.uitestframework;

import com.kalynx.uitestframework.exceptions.OcrException;

public class ValidationUtils {
    public static void performBasicValidation(String display, String window, Integer x, Integer y, Integer width, Integer height) throws OcrException {
        isDimensionsValid(x, y, width, height);
        if (display != null && window != null) throw new OcrException("Cannot specify both display and window.");
    }

    public static void isDimensionsValid(Integer x, Integer y, Integer width, Integer height) throws OcrException {
        int nulls = 0;
        if (x == null) nulls++;
        if (y == null) nulls++;
        if (width == null) nulls++;
        if (height == null) nulls++;

        if (nulls > 0 && nulls < 4) throw new OcrException("Either all dimensions must be applied, or none at all.");
    }
}
