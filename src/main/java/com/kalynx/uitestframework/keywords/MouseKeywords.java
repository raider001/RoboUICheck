package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.MouseEvent.MouseButtonDown;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.MouseController;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.Result;
import com.kalynx.uitestframework.data.ScreenshotData;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.*;
import java.util.Optional;

@RobotKeywords
public class MouseKeywords {
    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final MouseController MOUSE_CONTROLLER = DI.getInstance().getDependency(MouseController.class);
    private static final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
    private static final CvMonitor CV_MONITOR = DI.getInstance().getDependency(CvMonitor.class);
    public static final String INVALID_CLICK_OPTION_S_GIVEN = "Invalid Click option %s given.";
    public static final String HTML = "*HTML*";

    @RobotKeyword("""
            Move Mouse
                        
            | variable  | default | unit         |
            | xRelative |   N/A   | pixel        |
            | yRelative |   N/A   | pixel        |
                        
            Moves the mouse a relative distance based on its original location.
            """)
    @ArgumentNames({"xRelative", "yRelative"})
    public void moveMouse(int xRelative, int yRelative) {
        MOUSE_CONTROLLER.moveMouse(xRelative, yRelative);
    }

    @RobotKeyword("""
            Move Mouse To Display
                        
            | variable  | default | unit         |
            | display   |   N/A   | monitorId    |
            | x         |   N/A   | pixel        |
            | y         |   N/A   | pixel        |
                        
            If the mouse x and y is outside of the display bounds, an exception will be fired.
            """)
    @ArgumentNames({"display", "x", "y"})
    public void moveMouseToDisplay(String display, int x, int y) throws Exception {
        MOUSE_CONTROLLER.moveMouseTo(display, x, y);
    }

    @RobotKeyword("""
            Move Mouse To
                        
            If the mouse x and y is outside of the monitor bounds it is on, an exception will be fired.
            If wanting to change the mouse to another display, use the 'Move Mouse To Display' keyword.
            """)
    @ArgumentNames({"x", "y"})
    public void moveMouseTo(int x, int y) throws Exception {
        MOUSE_CONTROLLER.moveMouseTo(x, y);
    }

    @RobotKeyword("""
            Move Mouse To Image
            Moves the mouse to the center of the matched image on the screen.
            """)
    @ArgumentNames({"image"})
    public void moveMouseToImage(String image) throws Exception {
        Result<ScreenshotData> res = CV_MONITOR.monitorForImage(image);
        if (res.isFailure()) throw new Exception("*HTML*" + res.getInfo());
        LOGGER.info(res.getInfo());
        Rectangle p = res.getData().foundLocation();
        MOUSE_CONTROLLER.moveMouseTo(p.x + p.width / 2, p.y + +p.height / 2);
    }

    @RobotKeyword("""
            Simulates a Mouse click
            Available options are:
             - LEFT
             - MIDDLE
             - RIGHT
                        
            | variable  | default | unit         |
            | display   |   N/A   | monitorId    |
            | button    |   N/A   | option       |
            | count     |    1    | Clicks       |
                        
            The number of clicks can also be defined.
            """)
    @ArgumentNames({"button", "count=1"})
    public void click(String button, int count) throws Exception {
        if (count <= 0) throw new Exception("Mouse click count must be greater than 0");
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mouseClick(mask, count);
        } catch (InterruptedException a) {
            throw a;
        } catch (Exception e) {
            throw new Exception(INVALID_CLICK_OPTION_S_GIVEN.formatted(button));
        }

    }

    @RobotKeywordOverload
    public void click(String button) throws Exception {
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mouseClick(mask, 1);
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(INVALID_CLICK_OPTION_S_GIVEN.formatted(button));
        }
    }

    @RobotKeyword("""
            Click Location
            """)
    @ArgumentNames({"button", "x", "y", "times=1"})
    public void clickLocation(String button, int x, int y, int times) throws Exception {
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.moveMouseTo(x, y);
            MOUSE_CONTROLLER.mouseClick(mask, times);
        } catch (InterruptedException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception(INVALID_CLICK_OPTION_S_GIVEN.formatted(button));
        }
    }

    @RobotKeyword("""
            Click Image
            Clicks the center of the matched image on the screen.
            """)
    @ArgumentNames({"button", "image", "times=1"})
    public void clickImage(String button, String image, int times) throws Exception {
        Result<ScreenshotData> res = CV_MONITOR.monitorForImage(image);
        if (res.isFailure()) throw new Exception("*HTML*" + res.getInfo());
        LOGGER.info(res.getInfo());
        Rectangle p = res.getData().foundLocation();
        clickLocation(button, p.x + p.width / 2, p.y + +p.height / 2, times);
    }

    @RobotKeyword("""
            Click Image On Window
            Clicks the center of the matched image on the window.
            """)
    @ArgumentNames({"button", "image", "windowName", "times=1"})
    public void clickImageOnWindow(String button, String image, String windowName, int times) throws Exception {
        Rectangle formRegion = WINDOW_CONTROLLER.getWindowDimensions(windowName);
        if(formRegion == null) throw new Exception("Window: " + windowName + " not found. Available windows:" + WINDOW_CONTROLLER.getAllWindows().toString());

        // get the currently selected display
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();

        Optional<DisplayAttributes> attr = DISPLAY_MANAGER.getDisplays().stream().filter(display -> formRegion.x >= display.x()
                && formRegion.x < display.x() + display.width()
                && formRegion.y >= display.y()
                && formRegion.y < display.y() + display.height()).findFirst();
        if(attr.isEmpty()) throw new Exception("Window not found on any display. Is the form currently hidden or partially off a screen?");
        DISPLAY_MANAGER.setDisplay(attr.get().displayId());
        DisplayManager.DisplayData displayData =DISPLAY_MANAGER.getSelectedDisplayRegion();
        Rectangle originalRegion = displayData.displayRegion();
        DISPLAY_MANAGER.setCaptureRegion(formRegion);
        Result<ScreenshotData> r = CV_MONITOR.monitorForImage(image);
        DISPLAY_MANAGER.setCaptureRegion(originalRegion);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }

        LOGGER.info(r.getInfo());
        Rectangle p = r.getData().foundLocation();
        clickLocation(button, p.x + p.width / 2, p.y + +p.height / 2, times);
    }

    @RobotKeyword("""
            Press Mouse Button
            """)
    public void pressMouseButton(String button) throws Exception {
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mousePress(mask);
        } catch (Exception e) {
            throw new Exception("Invalid Press option %s given.".formatted(button));
        }
    }

    @RobotKeyword("""
            Press Mouse Button
            """)
    public void releaseMouseButton(String button) throws Exception {
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mouseRelease(mask);
        } catch (Exception e) {
            throw new Exception("Invalid Release option %s given.".formatted(button));
        }
    }

    @RobotKeyword("""
            Mouse Scroll
            """)
    public void mouseScroll(int scrollAmount) {
        MOUSE_CONTROLLER.mouseScroll(scrollAmount);
    }

    // Mouse Movement Settings
    @RobotKeyword("Set Mouse Move Speed")
    @ArgumentNames({"speed"})
    public void setMouseMoveSpeed(long speed) {
        MOUSE_CONTROLLER.setMouseMoveSpeed(speed);
    }
}
