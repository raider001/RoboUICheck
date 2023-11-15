package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.SnagTest;
import com.kalynx.snagtest.annotations.RobotKeywordName;
import com.kalynx.snagtest.data.DisplayAttributes;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.screen.CvMonitor;
import com.kalynx.snagtest.screen.ScreenshotData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class CombinationKeywords {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final DisplayManager DISPLAY_MANAGER = SnagTest.DI.getDependency(DisplayManager.class);
    private static final CvMonitor CV_MONITOR = SnagTest.DI.getDependency(CvMonitor.class);

    @RobotKeywordName("Verify ${image} Is On ${display}")
    @RobotKeyword("""
            """)
    public void imageOnDisplay(String imageName, String display) throws Exception {
        DisplayAttributes currentDisplay = DISPLAY_MANAGER.getSelectedDisplay();
        DISPLAY_MANAGER.setDisplay(display);
        Result<ScreenshotData> res = CV_MONITOR.monitorFor(imageName);
        DISPLAY_MANAGER.setDisplay(currentDisplay.displayId());
        if (res.isFailure()) throw new Exception("*HTML" + res.getInfo());
        LOGGER.info(res.getInfo());

    }

    @RobotKeywordName("Verify ${image} Is Not On ${display}")
    public void imageNotOnDisplay(String imageName, String display) throws Exception {
        DisplayAttributes currentDisplay = DISPLAY_MANAGER.getSelectedDisplay();
        DISPLAY_MANAGER.setDisplay(display);
        Result<ScreenshotData> res = CV_MONITOR.monitorFor(imageName);
        DISPLAY_MANAGER.setDisplay(currentDisplay.displayId());
        if (res.isSuccess()) throw new Exception("*HTML" + res.getInfo());
        LOGGER.info(res.getInfo());
    }
}
