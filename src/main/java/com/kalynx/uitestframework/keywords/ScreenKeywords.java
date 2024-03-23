package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.data.Result;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.time.Duration;


@RobotKeywords
public class ScreenKeywords {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final CvMonitor CV_MONITOR = DI.getInstance().getDependency(CvMonitor.class);
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
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
    @ArgumentNames({"imagePath", "displayName", "minMatchScore", "waitTime"})
    public void verifyImageExistsOnDisplay(String imageName, String displayName) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayName);
        Result<?> r = CV_MONITOR.monitorForImage(imageName);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    public void verifyImageExistsOnDisplay(String imageName, String displayName, double matchScore) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayName);
        Result<?> r = CV_MONITOR.monitorForImage(imageName, matchScore);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    public void verifyImageExistsOnDisplay(String imageName, String displayName, int waitTime) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayName);
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    public void verifyImageExistsOnDisplay(String imageName, String displayName, double matchScore, int waitTime) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayName);
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName, matchScore);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if (r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }
}
