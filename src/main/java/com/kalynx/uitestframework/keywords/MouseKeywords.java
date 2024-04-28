package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.DisplayRegionUtil;
import com.kalynx.uitestframework.MouseEvent.MouseButtonDown;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.MouseController;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.Result;
import com.kalynx.uitestframework.data.ScreenshotData;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import com.kalynx.uitestframework.exceptions.MonitorException;
import com.kalynx.uitestframework.exceptions.MouseException;
import com.kalynx.uitestframework.exceptions.WindowException;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Map;

@RobotKeywords
public class MouseKeywords {
    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final MouseController MOUSE_CONTROLLER = DI.getInstance().getDependency(MouseController.class);
    private static final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
    private static final CvMonitor CV_MONITOR = DI.getInstance().getDependency(CvMonitor.class);
    private static final DisplayRegionUtil DISPLAY_REGION_UTIL = DI.getInstance().getDependency(DisplayRegionUtil.class);
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
            Move Mouse To
                        
            If the mouse x and y is outside of the monitor bounds it is on, an exception will be fired.
            If wanting to change the mouse to another display, use the 'Move Mouse To Display' keyword.
            """)
    @ArgumentNames({"x=", "y=", "image=", "display=", "window="})
    public void moveMouseTo(Integer x, Integer y, String image, String display, String window) throws MouseException, WindowException, DisplayNotFoundException, InterruptedException, MonitorException, IOException {
        performBasicValidation(1, display, window, image, x, y);
        if(x != null) {
            handleXAndYMouseMove(x, y, display, window, 0, MouseButtonDown.LEFT.name());
        } else if (image != null) {
            handleImageMouseMove(image, display, window, 0, MouseButtonDown.LEFT.name());
        }
    }

    private void handleXAndYMouseMove(int x, int y, String display, String window, int clicks, String button) throws MouseException, DisplayNotFoundException, WindowException, InterruptedException {
        if(display != null) {
            MOUSE_CONTROLLER.moveMouseTo(display, x, y);
        } else if (window != null) {
            Rectangle r = WINDOW_CONTROLLER.getWindowDimensions(window);
            MOUSE_CONTROLLER.moveMouseTo(r.x + x, r.y + y);
        } else {
            MOUSE_CONTROLLER.moveMouseTo(x, y);
        }
        if(clicks > 0)  click(button, clicks);
    }

    private void handleImageMouseMove(String image, String display, String window, int clicks, String button) throws DisplayNotFoundException, MouseException, WindowException, InterruptedException, IOException {
        if(display != null) {
            handleMoveMouseToImageOnDisplay(image, display, clicks, button);
        } else if(window != null) {
            handleMoveMouseToImageOnWindow(image, window, clicks, button);
        } else {
            moveMouseToImage(image);
        }
        if(clicks > 0) click(button, clicks);
    }

    private void handleMoveMouseToImageOnDisplay(String image, String display, int clicks, String button) throws DisplayNotFoundException, IOException, MouseException, InterruptedException {
        DisplayAttributes originalDisplay = DISPLAY_MANAGER.getSelectedDisplay();
        DisplayAttributes displayOfInterest = DISPLAY_MANAGER.getDisplay(display);
        DISPLAY_MANAGER.setDisplay(displayOfInterest.displayId());
        Result<ScreenshotData> res = CV_MONITOR.monitorForImage(image);
        DISPLAY_MANAGER.setDisplay(originalDisplay.displayId());
        if (res.isFailure()) throw new MouseException(HTML + res.getInfo());
        LOGGER.info(res.getInfo());
        DISPLAY_MANAGER.setDisplay(displayOfInterest.displayId());
        Rectangle p = res.getData().foundLocation();
        MOUSE_CONTROLLER.moveMouseTo(p.x + p.width / 2, p.y + p.height / 2);
        DISPLAY_MANAGER.setDisplay(originalDisplay.displayId());
        if(clicks > 0) click(button, clicks);
    }
    private void handleMoveMouseToImageOnWindow(String image, String window, int clicks, String button) throws WindowException, IOException, MouseException, InterruptedException {
        DisplayRegionUtil.Regions region = DISPLAY_REGION_UTIL.getWindowDisplayRegions(window);
        region.switchToTemporaryDisplay();
        Result<ScreenshotData> res = CV_MONITOR.monitorForImage(image);
        region.switchToOriginalDisplay();
        if (res.isFailure()) throw new MouseException(HTML + res.getInfo());
        LOGGER.info(res.getInfo());
        Rectangle p = res.getData().foundLocation();
        region.switchToTemporaryDisplay();
        MOUSE_CONTROLLER.moveMouseTo(p.x + p.width / 2, p.y + p.height / 2);
        region.switchToOriginalDisplay();
        if(clicks > 0) click(button, clicks);
    }
    private void moveMouseToImage(String image) throws MouseException, IOException {
        Result<ScreenshotData> res = CV_MONITOR.monitorForImage(image);
        if (res.isFailure()) throw new MouseException(HTML + res.getInfo());
        LOGGER.info(res.getInfo());
        Rectangle p = res.getData().foundLocation();
        MOUSE_CONTROLLER.moveMouseTo(p.x + p.width / 2, p.y + p.height / 2);
    }

    private void click(String button, int count) throws MouseException, InterruptedException {
        if (count <= 0) throw new MouseException("Mouse click count must be greater than 0");
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mouseClick(mask, count);
        } catch (InterruptedException a) {
            throw a;
        } catch (Exception e) {
            throw new MouseException(INVALID_CLICK_OPTION_S_GIVEN.formatted(button));
        }

    }

    @RobotKeyword("""
            Simulates a Mouse click
            Available options are:
             - LEFT
             - MIDDLE
             - RIGHT
            
            window - defines the mouse click in the context of the window itself,
            treating the context as the top left corner of the window as 0,0 if x,y is defined.
            If image is defined, it will only search that window for the image and nothing else.
            
            display - defines the mouse click in the context of the display itself, treating the context as the top left corner of the display as 0,0 if x,y is defined.
            If the image is defined, then it will search that specific display for the image.
            
            If neither is defined, it will work from the context of the default selected display.
            
            Note, the test will fail under the following conditions.
            Only x,y or image can be defined (But not both at the same time).
            window or display can be defined (But not both at the same time).
            
            """)
    @ArgumentNames({"button=LEFT", "times=1", "x=", "y=", "image=", "display=", "window="})
    public void click(String button, int times, Integer x, Integer y, String image, String display, String window) throws MouseException, WindowException, IOException, DisplayNotFoundException, InterruptedException, MonitorException {
        performBasicValidation(times, display, window, image, x, y);
        // handle images
        if(image != null) {
            handleImageMouseMove(image, display, window, times, button);
        } else if(x == null) {
            click(button, times);
        } else {
            handleXAndYMouseMove(x, y, display, window, times, button);
        }
    }

    private void performBasicValidation(int times, String display, String window, String image, Integer x, Integer y) throws MouseException {
        if(times <= 0) throw new MouseException("Mouse click count must be greater than 0");
        if(image != null && (x != null || y != null)) throw new MouseException("Cannot use both image and x/y coordinates");
        if(x != null && y == null || x == null && y != null) throw new MouseException("Both x and y must be defined");
        if(display != null && window != null) throw new NotImplementedException("Cannot use both display and window");
    }

    @RobotKeyword("""
            Press Mouse Button
            """)
    public void pressMouseButton(String button) throws MouseException {
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mousePress(mask);
        } catch (Exception e) {
            throw new MouseException("Invalid Press option %s given.".formatted(button));
        }
    }

    @RobotKeyword("""
            Get Mouse Position
            """)
    public Map<String, Integer> getMousePosition() {
        Point p = MOUSE_CONTROLLER.getMousePosition();
        return Map.of("x", p.x, "y", p.y );
    }

    @RobotKeyword("""
            Release Mouse Button
            """)
    public void releaseMouseButton(String button) throws MouseException {
        try {
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MOUSE_CONTROLLER.mouseRelease(mask);
        } catch (Exception e) {
            throw new MouseException("Invalid Release option %s given.".formatted(button));
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
    public void setMouseMoveSpeed(long speed) throws MouseException {
        MOUSE_CONTROLLER.setMouseMoveSpeed(speed);
    }
}
