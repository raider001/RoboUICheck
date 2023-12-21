package com.kalynx.snagtest.keywordsold;

import com.kalynx.snagtest.SnagTestOld;
import com.kalynx.snagtest.control.WindowController;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class WindowKeywords {
    private static final WindowController WINDOW_CONTROLLER = SnagTestOld.DI.getDependency(WindowController.class);

    @RobotKeyword()
    public void moveWindowTo(String windowName, int x, int y) {
        WINDOW_CONTROLLER.setWindowPosition(windowName, x, y);
    }

    @RobotKeyword()
    public void moveWindowToDisplay(String windowName, String display, int x, int y) {
        WINDOW_CONTROLLER.setWindowPosition(windowName, display, x, y);
    }

    @RobotKeyword()
    public void setWindowSize(String windowName, int width, int height) {
        WINDOW_CONTROLLER.setWindowSize(windowName, width, height);
    }

    @RobotKeyword()
    public String[] getAllWindows() {
        return WINDOW_CONTROLLER.getAllWindows().toArray(new String[0]);
    }

    @RobotKeyword()
    public void bringToFront(String windowName) {
        WINDOW_CONTROLLER.bringToFront(windowName);
    }
}
