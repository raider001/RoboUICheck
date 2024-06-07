package com.kalynx.uitestframework.os;

import com.kalynx.uitestframework.exceptions.WindowException;

import java.awt.*;
import java.util.List;

public interface Window {

    /**
     * Returns all window names.
     *
     * @return Name of All windows
     */
    List<String> getAllWindows();

    /**
     * Gets the window dimensions for the specified window
     *
     * @param windowName The name of the window
     * @return Window dimensions, or Null if window not found.
     */
    Rectangle getWindowDimensions(String windowName) throws WindowException;

    /**
     * Sets the window position relative to its current position
     *
     * @param windowName The name of the window
     * @param x The horizontal position on the screen
     * @param y The vertical position on the screen
     * @return True if the window position was set successfully, False otherwise
     */
    boolean setWindowPosition(String windowName, int x, int y);

    /**
     * Sets the window position based on the display reference
     *
     * @param windowName The name of the window
     * @param displayReference The reference of the display the window is on.
     * @param x The horizontal position on the screen
     * @param y The vertical position on the screen.
     * @return True if the window position was set successfully, False otherwise
     */
    boolean setWindowPosition(String windowName, String displayReference, int x, int y);

    /**
     * Sets the window size
     *
     * @param windowName The name of the window
     * @param width The new width for the window
     * @param height The new height for the window
     * @return True if the window size was set successfully, False otherwise
     */
    boolean setWindowSize(String windowName, int width, int height);

    /**
     * Bring the window to the front.
     *
     * @param windowName The name of the window
     * @return True if the window was brought to the front, False otherwise
     */
    boolean bringToFront(String windowName);
}
