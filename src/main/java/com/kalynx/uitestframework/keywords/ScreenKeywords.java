package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.Result;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.time.Duration;
import java.util.List;
import java.util.Optional;


@RobotKeywords
public class ScreenKeywords {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final CvMonitor CV_MONITOR = DI.getInstance().getDependency(CvMonitor.class);
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
    private static final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);
    public static final String HTML = "*HTML*";


    @RobotKeyword("""
            Verify Image Exists
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate
                        
            for more information
            """)
    @ArgumentNames({"image_name", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageExists(String imageName, double minMatchScore, int waitTime) throws Exception {
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        if(r.isFailure()) throw new Exception(HTML + r.getInfo());
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Does Not Exist
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate
                        
            for more information
            """)
    @ArgumentNames({"image_name", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageDoesNotExist(String imageName, double minMatchScore, int waitTime) throws Exception {
        Result<?> r = CV_MONITOR.monitorForLackOfImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        if(r.isFailure()) throw new Exception(HTML + r.getInfo());
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Exists On Display
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
            | display  |   N/A    | display id                                   |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate
                        
            for more information
            """)
    @ArgumentNames({"imagePath", "displayName", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageExistsOnDisplay(String imageName, String displayName, double minMatchScore, int waitTime) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayName);
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Exists On Display
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
            | display  |   N/A    | display id                                   |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate
                        
            for more information
            """)
    @ArgumentNames({"imagePath", "displayName", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageDoesNotExistOnDisplay(String imageName, String displayName, double minMatchScore, int waitTime) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayName);
        Result<?> r = CV_MONITOR.monitorForLackOfImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Does Not Exist On Window
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
            | window     |   N/A    | window name                                   |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate
                        
            for more information
            """)
    @ArgumentNames({"imagePath", "displayName", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageDoesNotExistOnWindow(String imageName, String windowName, double minMatchScore, int waitTime) throws Exception {
        // Get the current window attributes
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
        Result<?> r = CV_MONITOR.monitorForLackOfImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        DISPLAY_MANAGER.setCaptureRegion(originalRegion);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Does Not Exist On Window
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
            | window     |   N/A    | window name                                   |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate
                        
            for more information
            """)
    @ArgumentNames({"imagePath", "displayName", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageExistsOnWindow(String imageName, String windowName, double minMatchScore, int waitTime) throws Exception {
        // Get the current window attributes
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
        Rectangle originalRegion = new Rectangle(displayData.displayRegion());
        DISPLAY_MANAGER.setCaptureRegion(formRegion);
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        DISPLAY_MANAGER.setCaptureRegion(originalRegion);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }
}
