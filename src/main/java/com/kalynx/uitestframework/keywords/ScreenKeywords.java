package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.Result;
import com.kalynx.uitestframework.data.ScreenshotData;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.time.Duration;
import java.util.Map;
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
        Result<ScreenshotData> r = getResultFromDisplay(imageName, minMatchScore, waitTime, displayName, ContainType.DOES_CONTAIN);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Does Not Exist On Display
                        
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
        Result<ScreenshotData> r = getResultFromDisplay(imageName, minMatchScore, waitTime, displayName, ContainType.DOES_NOT_CONTAIN);
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
        Result<ScreenshotData> r = getResultFromWindow(imageName, minMatchScore, waitTime, windowName, ContainType.DOES_NOT_CONTAIN);
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
    @ArgumentNames({"imagePath", "windowName", "minMatchScore=-1", "waitTime=200"})
    public void verifyImageExistsOnWindow(String imageName, String windowName, double minMatchScore, int waitTime) throws Exception {
        Result<?> r =getResultFromWindow(imageName, minMatchScore, waitTime, windowName, ContainType.DOES_CONTAIN);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Get Image Bounds
            
            Gets the Image bounds.
            
            Note that only display or window can be defined, not both. Otherwise the keyword will fail.
            
            """)
    @ArgumentNames({"imagePath", "minMatchScore=-1", "waitTime=200", "display=", "window="})
    public Map<String, Integer> getImageBounds(String image, double minMatchScore, int waitTime, String display, String window) throws Exception {
        if(display != null && window != null) throw new Exception("Cannot specify both display and window");
        Result<ScreenshotData> r;
        if(window != null) {
            r = getResultFromWindow(image, minMatchScore, waitTime, window, ContainType.DOES_CONTAIN);
        } else if(display != null) {
            r = getResultFromDisplay(image, minMatchScore, waitTime, display, ContainType.DOES_CONTAIN);
        } else {
            r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), image, minMatchScore);
        }
        if(r.isFailure()) throw new Exception(HTML + r.getInfo());
        LOGGER.info(r.getInfo());
        return Map.of("x", r.getData().foundLocation().x, "y", r.getData().foundLocation().y, "width", r.getData().foundLocation().width, "height", r.getData().foundLocation().height);
    }

    private Result<ScreenshotData> getResultFromWindow(String image, double minMatchScore, int waitTime, String window, ContainType type) throws Exception {
        Regions regions = getWindowDisplayRegions(window);
        regions.switchToTemporaryDisplay();
        Result<ScreenshotData> r = type == ContainType.DOES_CONTAIN ? CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), image, minMatchScore) : CV_MONITOR.monitorForLackOfImage(Duration.ofMillis(waitTime), image, minMatchScore);
        regions.switchToOriginalDisplay();
        return r;
    }

    private Result<ScreenshotData> getResultFromDisplay(String image, double minMatchScore, int waitTime, String display, ContainType type) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(display);
        Result<ScreenshotData> r = type == ContainType.DOES_CONTAIN ? CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), image, minMatchScore) : CV_MONITOR.monitorForLackOfImage(Duration.ofMillis(waitTime), image, minMatchScore);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        return r;
    }


    private Regions getWindowDisplayRegions(String window) throws Exception {
        Rectangle formRegion = WINDOW_CONTROLLER.getWindowDimensions(window);
        if(formRegion == null) throw new Exception("Window: " + window + " not found. Available windows:" + WINDOW_CONTROLLER.getAllWindows().toString());
        Optional<DisplayAttributes> attr = DISPLAY_MANAGER.getDisplays().stream().filter(display -> formRegion.x >= display.x()
                && formRegion.x < display.x() + display.width()
                && formRegion.y >= display.y()
                && formRegion.y < display.y() + display.height()).findFirst();
        if(attr.isEmpty()) throw new Exception("Window not found on any display. Is the form currently hidden or partially off a screen?");
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(attr.get().displayId());
        DisplayManager.DisplayData displayData =DISPLAY_MANAGER.getSelectedDisplayRegion();
        Rectangle originalRegion = new Rectangle(displayData.displayRegion());
        return new Regions(originalDisplay, originalRegion, attr.get().displayId(), formRegion);
    }

    private enum ContainType {
        DOES_CONTAIN,
        DOES_NOT_CONTAIN
    }

    private static class Regions {

        public final int originalDisplay;
        public final Rectangle original;

        public final int temporaryDisplay;
        public final Rectangle temporary;

        private Regions(int originalDisplay, Rectangle original, int temporaryDisplay,  Rectangle temporary) {
            this.originalDisplay = originalDisplay;
            this.original = original;
            this.temporaryDisplay = temporaryDisplay;
            this.temporary = temporary;
        }

        public void switchToOriginalDisplay() {
            DISPLAY_MANAGER.setDisplay(originalDisplay);
            DISPLAY_MANAGER.setCaptureRegion(original);
        }

        public void switchToTemporaryDisplay() {
            DISPLAY_MANAGER.setDisplay(temporaryDisplay);
            DISPLAY_MANAGER.setCaptureRegion(temporary);
        }
    }
}
