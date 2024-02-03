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
    @ArgumentNames({"imagePath"})
    public void verifyImageExists(String imagePath) throws Exception {
        Result<?> r = CV_MONITOR.monitorForImage(imagePath);
        if (r.isFailure()) throw new Exception(HTML + r.getInfo());
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Exists With Min Match Score
                        
            | variable      | default  | unit                                         |
            |---------------|----------|----------------------------------------------|
            | image         |   N/A    | relative path to image added image locations |
            | minMatchScore | 0.95 | percent decimal |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate            
                        
            for more information
            """)
    @ArgumentNames({"imageName", "minMatchScore"})
    public void verifyImageExistsWithMinMatch(String imageName, int minMatchScore) throws Exception {
        Result<?> r = CV_MONITOR.monitorForImage(imageName, minMatchScore);
        if(r.isFailure()) throw new Exception(HTML + r.getInfo());
        LOGGER.info(r.getInfo());
    }

    @RobotKeywordOverload
    public void verifyImageExists(String imageName, int minMatchScore, int waitTime) throws Exception {
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        if(r.isFailure()) throw new Exception(HTML + r.getInfo());
        LOGGER.info(r.getInfo());
    }

    @RobotKeywordOverload
    public void verifyImageExistsOverDuration(String imageName, int waitTime) throws Exception {
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName);
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
    @ArgumentNames({"imagePath", "displayId", "minMatchScore", "waitTime"})
    public void verifyImageExistsOnDisplay(String imageName, int displayId) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayId);
        Result<?> r = CV_MONITOR.monitorForImage(imageName);
        if (r.isFailure()) {
            DISPLAY_MANAGER.setDisplay(originalDisplay);
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeywordOverload
    public void verifyImageOnDisplay(String imageName, int displayId, int minMatchScore, int waitTime) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayId);
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName, minMatchScore);
        if(r.isFailure()) {
            DISPLAY_MANAGER.setDisplay(originalDisplay);
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Exists On Display With Min Match Score
                        
            | variable      | default  | unit                                         |
            |---------------|----------|----------------------------------------------|
            | display       |   N/A    | display id                                   |
            | image         |   N/A    | relative path to image added image locations |
            | minMatchScore | 0.95 | percent decimal |

                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate            
                        
            for more information
            """)
    public void verifyImageOnDisplayWithMinMatch(String imageName, int displayId, int minMatchScore) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayId);
        Result<?> r = CV_MONITOR.monitorForImage(imageName, minMatchScore);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if(r.isFailure()) {

            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Verify Image Exists On Display Over Duration
                        
            | variable | default  | unit                                         |
            |----------|----------|----------------------------------------------|
            | image    |   N/A    | relative path to image added image locations |
            | display  |   N/A    | display id                                   |
            | waitTime |   N/A    | milliseconds                                 |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Similarity
            - Set Poll Rate            
                        
            for more information
            """)
    public void verifyImageOnDisplayOverDuration(String imageName, int displayId, int waitTime) throws Exception {
        int originalDisplay = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        DISPLAY_MANAGER.setDisplay(displayId);
        Result<?> r = CV_MONITOR.monitorForImage(Duration.ofMillis(waitTime), imageName);
        DISPLAY_MANAGER.setDisplay(originalDisplay);
        if(r.isFailure()) {
            throw new Exception(HTML + r.getInfo());
        }
        LOGGER.info(r.getInfo());
    }

}
