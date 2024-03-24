package com.kalynx.uitestframework.os.windows;

import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.os.Window;
import com.sun.jna.Native;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WindowsWindowApi implements Window {

    private static final User32 WINDOW_API = User32.INSTANCE;
    private final DisplayManager displayManager;

    public WindowsWindowApi(DisplayManager displayManager) {
        this.displayManager = displayManager;
    }

    @Override
    public List<String> getAllWindows() {

        List<String> windowTitles = new ArrayList<>();
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            WINDOW_API.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && !windowTitles.contains(windowTitle)) {
                windowTitles.add(windowTitle);
            }
            return true;
        };
        WINDOW_API.EnumWindows(enumCallback, null);
        return windowTitles;
    }

    @Override
    public Rectangle getWindowDimensions(String windowName) {
        AtomicReference<Rectangle> rectangle = new AtomicReference<>();
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            WINDOW_API.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && windowTitle.contains(windowName)) {
                User32.RECT rect = new User32.RECT();
                WINDOW_API.GetWindowRect(hWnd, rect);
                rectangle.set(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
                return false;
            }
            return true;
        };
        WINDOW_API.EnumWindows(enumCallback, null);
        return rectangle.get();
    }

    @Override
    public boolean setWindowPosition(String windowName, int x, int y) {
        // set the Window Position for the given window with the window name to the given x and y position.
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            WINDOW_API.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && windowTitle.contains(windowName)) {
                User32.RECT rect = new User32.RECT();
                WINDOW_API.GetWindowRect(hWnd, rect);
                Rectangle rectangle = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);

                WINDOW_API.MoveWindow(hWnd, x + rect.left, y + rect.top, rectangle.width, rectangle.height, true);
                return false;
            }
            return true;
        };
        return !WINDOW_API.EnumWindows(enumCallback, null);
    }


    @Override
    public boolean setWindowPosition(String windowName, String displayReference, int x, int y) {
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            WINDOW_API.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && windowTitle.contains(windowName)) {
                User32.RECT rect = new User32.RECT();
                WINDOW_API.GetWindowRect(hWnd, rect);
                int dispX = displayManager.getDisplay(displayReference).x();
                int dispY = displayManager.getDisplay(displayReference).y();
                Rectangle rectangle = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
                WINDOW_API.MoveWindow(hWnd, x + dispX, y + dispY, rectangle.width, rectangle.height, true);
                return false;
            }
            return true;
        };
        return WINDOW_API.EnumWindows(enumCallback, null);
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
        if (width < 0 || height < 0) throw new IllegalArgumentException("Width and Height must be greater than 0");
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            WINDOW_API.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && windowTitle.contains(windowName)) {
                User32.RECT rect = new User32.RECT();
                WINDOW_API.GetWindowRect(hWnd, rect);
                WINDOW_API.MoveWindow(hWnd, rect.left, rect.top,  width, height, true);
                return false;
            }
            return true;
        };
        return WINDOW_API.EnumWindows(enumCallback, null);
    }

    /**
     * Bring the window to the front.
     *
     * @param windowName
     * @return
     */
    @Override
    public boolean bringToFront(String windowName) {
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            WINDOW_API.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && windowTitle.contains(windowName)) {
                User32.RECT rect = new User32.RECT();
                WINDOW_API.SetForegroundWindow(hWnd);
                return false;
            }
            return true;
        };
        return WINDOW_API.EnumWindows(enumCallback, null);
    }
}
