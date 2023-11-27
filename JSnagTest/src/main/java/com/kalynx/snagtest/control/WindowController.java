package com.kalynx.snagtest.control;

import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.os.Window;
import com.kalynx.snagtest.os.linux.LinuxWindowApi;
import com.kalynx.snagtest.os.windows.WindowsWindowApi;

import java.awt.Rectangle;
import java.util.List;

public class WindowController implements Window {

    private final Window window;

    public WindowController(DisplayManager displayManager) {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("win")) {
            window = new WindowsWindowApi(displayManager);
        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            window = new LinuxWindowApi();
        } else {
            throw new RuntimeException("Unsupported OS");
        }
    }

    /**
     * Returns all window names.
     *
     * @return
     */
    @Override
    public List<String> getAllWindows() {
        return window.getAllWindows();
    }

    @Override
    public Rectangle getWindowDimensions(String windowName) {
        return window.getWindowDimensions(windowName);
    }

    /**
     * Sets the window position relative to its current position
     *
     * @param windowName
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean setWindowPosition(String windowName, int x, int y) {
        return window.setWindowPosition(windowName, x, y);
    }

    /**
     * Sets the window position based on the display reference
     *
     * @param windowName
     * @param displayReference
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean setWindowPosition(String windowName, String displayReference, int x, int y) {
        return window.setWindowPosition(windowName, displayReference, x, y);
    }

    /**
     * Sets the window size
     *
     * @param windowName
     * @param width
     * @param height
     * @return
     */
    @Override
    public boolean setWindowSize(String windowName, int width, int height) {
        return window.setWindowSize(windowName, width, height);
    }

    /**
     * Bring the window to the front.
     *
     * @param windowName
     * @return
     */
    @Override
    public boolean bringToFront(String windowName) {
        return window.bringToFront(windowName);
    }
}
