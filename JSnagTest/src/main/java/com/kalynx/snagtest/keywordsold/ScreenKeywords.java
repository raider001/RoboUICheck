package com.kalynx.snagtest.keywordsold;

import com.kalynx.snagtest.SnagTestOld;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.screen.CvMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;


@RobotKeywords
public class ScreenKeywords {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final CvMonitor CV_MONITOR = SnagTestOld.DI.getDependency(CvMonitor.class);
    private static final DisplayManager DISPLAY_MANAGER = SnagTestOld.DI.getDependency(DisplayManager.class);


    @RobotKeyword("""
            Verify Image Exists
                        
            | variable | default  | unit                                         |
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
        if (r.isFailure()) throw new Exception("*HTML*" + r.getInfo());
        LOGGER.info(r.getInfo());
    }
}
