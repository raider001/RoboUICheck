package com.kalynx.snagtest.os.windows;

import com.kalynx.snagtest.os.Window;
import com.sun.jna.Native;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class WindowsWindowApi implements Window {

    public User32 windowApi = User32.INSTANCE;

    @Override
    public List<String> getAllWindows() {

        List<String> windowTitles = new ArrayList<>();
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            windowApi.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && !windowTitles.contains(windowTitle)) {
                windowTitles.add(windowTitle);
            }
            return true;
        };
        windowApi.EnumWindows(enumCallback, null);
        return windowTitles;
    }

    @Override
    public Rectangle getWindowDimensions(String windowName) {
        AtomicReference<Rectangle> rectangle = new AtomicReference<>();
        User32.WNDENUMPROC enumCallback = (hWnd, arg) -> {
            char[] buffer = new char[1024];
            windowApi.GetWindowTextW(hWnd, buffer, buffer.length);
            String windowTitle = Native.toString(buffer);
            if (!windowTitle.isEmpty() && windowTitle.equals(windowName)) {
                User32.RECT rect = new User32.RECT();
                windowApi.GetWindowRect(hWnd, rect);
                rectangle.set(new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top));
                return false;
            }
            return true;
        };
        windowApi.EnumWindows(enumCallback, null);
        return rectangle.get();
    }

    @Override
    public void setWindowPosition(String windowName, int x, int y) {
        Rectangle windowDimensions = getWindowDimensions(windowName);
    }

    @Override
    public void setWindowPosition(String windowName, int display, int x, int y) {
        
    }


}
