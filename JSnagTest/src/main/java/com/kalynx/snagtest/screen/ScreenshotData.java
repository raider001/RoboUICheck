package com.kalynx.snagtest.screen;

import org.opencv.core.Mat;

import java.awt.*;

public record ScreenshotData(long timeTaken, Mat screenshot, Rectangle foundLocation) {
}
