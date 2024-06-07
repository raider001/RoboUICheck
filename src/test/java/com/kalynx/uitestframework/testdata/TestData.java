package com.kalynx.uitestframework.testdata;

import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.data.DisplayAttributes;

import java.awt.*;

public class TestData {
    public static DisplayManager createDisplayManager() {
        GraphicsDevice defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        DisplayAttributes primary = new DisplayAttributes(1, defaultDevice, true, 0, 0, 1920, 1080);
        DisplayAttributes top = new DisplayAttributes(0, defaultDevice, false, 0, -1920, 1920, 1080);
        DisplayAttributes bottom = new DisplayAttributes(2, defaultDevice, false, 0, 1920, 1920, 1080);
        DisplayAttributes left = new DisplayAttributes(3, defaultDevice, false, -1920, 0, 1920, 1080);
        DisplayAttributes right = new DisplayAttributes(4, defaultDevice, false, 1920, 0, 1920, 1080);
        DisplayAttributes smaller = new DisplayAttributes(5, defaultDevice, false, -2920, 0, 960, 540);
        DisplayAttributes larger = new DisplayAttributes(6, defaultDevice, false, 0, 3840, 3840, 2160);
        return new DisplayManager(primary, top, bottom, left, right, smaller, larger);
    }
}
