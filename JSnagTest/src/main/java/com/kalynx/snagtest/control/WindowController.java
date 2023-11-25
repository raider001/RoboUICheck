package com.kalynx.snagtest.control;

import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.os.Window;
import com.kalynx.snagtest.os.windows.WindowsWindowApi;

public class WindowController {

    private Window window;

    public WindowController(DisplayManager displayManager) {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("win")) {
            window = new WindowsWindowApi(displayManager);
        } else if (OS.contains("mac")) {

        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {

        } else {
            throw new RuntimeException("Unsupported OS");
        }
    }
}
