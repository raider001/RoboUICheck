package com.kalynx.snagtest.control;

public class WindowController {

    public WindowController() {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("win")) {

        } else if (OS.contains("mac")) {

        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {

        } else {
            throw new RuntimeException("Unsupported OS");
        }
    }
}
