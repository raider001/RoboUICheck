package com.kalynx.uitestframework.controller;

import com.kalynx.uitestframework.exceptions.UnsupportedOS;
import com.kalynx.uitestframework.exceptions.WindowException;
import com.kalynx.uitestframework.os.Window;
import com.kalynx.uitestframework.os.linux.LinuxWindowApi;
import com.kalynx.uitestframework.os.windows.WindowsWindowApi;

import java.awt.Rectangle;
import java.util.List;

public class WindowController implements Window {

    private final Window window;

    public WindowController(DisplayManager displayManager) throws UnsupportedOS {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            window = new WindowsWindowApi(displayManager);
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            window = new LinuxWindowApi(displayManager);
        } else {
            throw new UnsupportedOS();
        }
    }

    /**
     * Returns all window names.
     *
     * @return All window names available in the system
     */
    @Override
    public List<String> getAllWindows() {
        return window.getAllWindows();
    }

    @Override
    public Rectangle getWindowDimensions(String windowName) throws WindowException {
        Rectangle r = window.getWindowDimensions(windowName);
        if (r == null) throw new WindowException(windowName + " does Not Exist");

        return new Rectangle(r);
    }

    /**
     * Sets the window position relative to its current position
     *
     * @param windowName the window name
     * @param x          the x position
     * @param y          the y position
     * @return true if the window was moved
     */
    @Override
    public boolean setWindowPosition(String windowName, int x, int y) {
        return window.setWindowPosition(windowName, x, y);
    }

    /**
     * Sets the window position based on the display reference
     *
     * @param windowName       the window name
     * @param displayReference the display reference
     * @param x                the x position
     * @param y                the y position
     * @return true if the window was moved
     */
    @Override
    public boolean setWindowPosition(String windowName, String displayReference, int x, int y) {
        return window.setWindowPosition(windowName, displayReference, x, y);
    }

    /**
     * Sets the window size
     *
     * @param windowName the window name
     * @param width      the width
     * @param height     the height
     * @return true if the window was resized
     */
    @Override
    public boolean setWindowSize(String windowName, int width, int height) {
        return window.setWindowSize(windowName, width, height);
    }

    /**
     * Bring the window to the front.
     *
     * @param windowName the window name
     * @return true if the window was brought to the front
     */
    @Override
    public boolean bringToFront(String windowName) {
        return window.bringToFront(windowName);
    }
}
