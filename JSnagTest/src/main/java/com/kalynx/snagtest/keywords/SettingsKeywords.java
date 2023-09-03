package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.control.MainController;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.time.Duration;

@RobotKeywords
public class SettingsKeywords {
    @RobotKeyword("""
                    Add Image Path
                    Adds the image path relative to the file location calling it.
                    """)
    @ArgumentNames({"path"})
    public boolean addImagePath(String path) {
        MainController.getInstance().getCvMonitor().addImagePath(path);
        return true;
    }

    @RobotKeyword("""
                    Add a HTTP Image Path
                    Adds the image path relative to the file location calling it.
                    """)
    @ArgumentNames({"path"})
    public boolean addHttpImagePath(String path) {
        return false;
    }

   @RobotKeyword("Set Similarity")
    @ArgumentNames({"similarity"})
    public boolean setSimilarity(double similarity) {
        MainController.getInstance().getCvMonitor().setMatchScore(similarity);
        return true;
    }

    @RobotKeyword("Set Poll Rate")
    @ArgumentNames({"pollRate"})
    public void setPollRate(long pollRate) {
        MainController.getInstance().getCvMonitor().setPollRate(Duration.ofMillis(pollRate));
    }

    @RobotKeyword("Set Timeout Time")
    @ArgumentNames({"timeoutTime"})
    public void setTimeoutTime(long timeoutTime) {
        MainController.getInstance().getCvMonitor().setTimeoutTime(Duration.ofMillis(timeoutTime));
    }
}
