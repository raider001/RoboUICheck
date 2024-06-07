package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.exceptions.WindowException;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

@RobotKeywords
public class WindowKeywords {

    public static final String WINDOW = "Window:";
    public static final String NOT_FOUND_AVAILABLE_WINDOWS = " not found. Available windows:";
    private static final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);

    @RobotKeyword("""
            Gets all available windows, minimized or maximized.
            """)
    public List<String> getAllAvailableWindows() {
        return WINDOW_CONTROLLER.getAllWindows();
    }

    @RobotKeyword("""
            Gets the dimensions for the window requested.
            """)
    @ArgumentNames({"windowName"})
    public Map<String,Integer> getWindowDimensions(String windowName) throws WindowException {
        Rectangle r = WINDOW_CONTROLLER.getWindowDimensions(windowName);
        if(r == null) throw new WindowException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        return Map.of("x", r.x, "y", r.y, "width", r.width, "height", r.height);
    }

    @RobotKeyword("""
            Brings the window to the front.
            """)
    @ArgumentNames({"windowName"})
    public void bringWindowToFront(String windowName) throws WindowException {
        boolean success = WINDOW_CONTROLLER.bringToFront(windowName);
        if(!success) throw new WindowException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
    }

    @RobotKeyword("""
            Moves the window to the specified location.
            When not defined, the display will be the currently selected display.
            """)
    @ArgumentNames({"windowName", "x", "y", "display="})
    public void moveWindow(String windowName, int x, int y, String display) throws IllegalArgumentException, WindowException {
        if(!WINDOW_CONTROLLER.getAllWindows().contains(windowName)) throw new IllegalArgumentException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        boolean success = display == null ? WINDOW_CONTROLLER.setWindowPosition(windowName, x, y) : WINDOW_CONTROLLER.setWindowPosition(windowName, display, x, y);
        if(!success) throw new WindowException("Window attempted to be moved out of bounds");
    }

    @RobotKeyword("""
            Resize Window
            Resizes the specified window
            """)
    @ArgumentNames({"windowName", "width", "height"})
    public void resizeWindow(String windowName, int width, int height) {
        if(width < 0 || height < 0) throw new IllegalArgumentException("Width and height must be greater than 0");
        if(!WINDOW_CONTROLLER.getAllWindows().contains(windowName)) throw new IllegalArgumentException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        WINDOW_CONTROLLER.setWindowSize(windowName, width, height);
    }
}
