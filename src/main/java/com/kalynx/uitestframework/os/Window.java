package com.kalynx.uitestframework.os;

import java.awt.*;
import java.util.List;

public interface Window {

    /**
     * Returns all window names.
     *
     * @return
     */
    List<String> getAllWindows();

    /**
     * Gets the window dimensions for the specified window
     *
     * @param windowName
     * @return Window dimensions, or Null if window not found.
     */
    Rectangle getWindowDimensions(String windowName);

    /**
     * Sets the window position relative to its current position
     *
     * @param windowName
     * @param x
     * @param y
     * @return
     */
    boolean setWindowPosition(String windowName, int x, int y);

    /**
     * Sets the window position based on the display reference
     *
     * @param windowName
     * @param displayReference
     * @param x
     * @param y
     * @return
     */
    boolean setWindowPosition(String windowName, String displayReference, int x, int y);

    /**
     * Sets the window size
     *
     * @param windowName
     * @param width
     * @param height
     * @return
     */
    boolean setWindowSize(String windowName, int width, int height);

    /**
     * Bring the window to the front.
     *
     * @param windowName
     * @return
     */
    boolean bringToFront(String windowName);
}
