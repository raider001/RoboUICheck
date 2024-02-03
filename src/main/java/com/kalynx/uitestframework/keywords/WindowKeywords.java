package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.WindowController;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

@RobotKeywords
public class WindowKeywords {

    public static final String WINDOW = "Window:";
    public static final String NOT_FOUND_AVAILABLE_WINDOWS = " not found. Available windows:";
    private final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);

    @RobotKeyword("""
            Gets all available windows, minimized or maximized.
            """)
    public List<String> getAllAvailableWindows() {
        return WINDOW_CONTROLLER.getAllWindows();
    }

    @RobotKeyword("""
            Gets the dimensions for the window requested.
            """)
    public List<Integer> getWindowDimensions(String windowName) throws Exception {
        Rectangle r = WINDOW_CONTROLLER.getWindowDimensions(windowName);
        if(r == null) throw new Exception(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        return Arrays.asList(r.x, r.y, r.width, r.height);
    }

    @RobotKeyword("""
            Brings the window to the front.
            """)
    public void bringWindowToFront(String windowName) throws Exception {
        boolean success = WINDOW_CONTROLLER.bringToFront(windowName);
        if(!success) throw new Exception(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
    }

    @RobotKeyword("""
            Move Window
            Moves the window to the back
            """)
    public void moveWindow(String windowName, int x, int y) throws Exception {
        if(!WINDOW_CONTROLLER.getAllWindows().contains(windowName)) throw new IllegalArgumentException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        boolean success = WINDOW_CONTROLLER.setWindowPosition(windowName, x, y);
        if(!success) throw new Exception("Window attempted to be moved out of bounds");
    }

    @RobotKeywordOverload()
    public void moveWindow(String windowName, String displayReference, int x, int y) throws Exception {
        if(!WINDOW_CONTROLLER.getAllWindows().contains(windowName)) throw new IllegalArgumentException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        boolean success = WINDOW_CONTROLLER.setWindowPosition(windowName, displayReference, x, y);
        if(!success) throw new Exception("Window attempted to be moved out of bounds");
    }

    @RobotKeyword("""
            Resize Window
            Resizes the specified window
            """)
    public void resizeWindow(String windowName, int width, int height) {
        if(width < 0 || height < 0) throw new IllegalArgumentException("Width and height must be greater than 0");
        if(!WINDOW_CONTROLLER.getAllWindows().contains(windowName)) throw new IllegalArgumentException(WINDOW + windowName + NOT_FOUND_AVAILABLE_WINDOWS + getAllAvailableWindows().toString());
        WINDOW_CONTROLLER.setWindowSize(windowName, width, height);
    }
}
