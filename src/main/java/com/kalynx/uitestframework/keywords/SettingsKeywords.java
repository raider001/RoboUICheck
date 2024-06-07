package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.KeyboardController;
import com.kalynx.uitestframework.controller.Settings;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

@RobotKeywords
public class SettingsKeywords {

    private final Settings settings = DI.getInstance().getDependency(Settings.class);
    private final CvMonitor CV_MONITOR = DI.getInstance().getDependency(CvMonitor.class);
    private final KeyboardController KEYBOARD_CONTROLLER = DI.getInstance().getDependency(KeyboardController.class);
    @RobotKeyword("""
            Set Timeout Time
            Sets the timeout time for the monitor to look for an image in milliseconds.
            """)
    @ArgumentNames({"timeoutTime"})
    public void setTimeoutTime(int timeoutTime) {
        settings.setTimeout(Duration.ofMillis(timeoutTime));
    }

    @RobotKeyword("""
            Set Poll Rate
            Sets the frequency the monitor will poll for an image in milliseconds.
            Note that it is not recommended to set this value below 100ms as it will attempt to poll at the given rate,
            but cannot be guarranteed due to potential system limitations.
            """)
    @ArgumentNames({"pollRate"})
    public void setPollRate(int pollRate) throws Exception {
        settings.setPollRate(Duration.ofMillis(pollRate));
    }

    @RobotKeyword("""
            Returns all the paths that will be searched for given images.
            """)
    public List<String> getImagePaths() {
        return CV_MONITOR.getImagePaths();
    }

    @RobotKeyword("""
            Sets the path for where images will be saved.
            """)
    @ArgumentNames({"path"})
    public void setResultPath(String path) {
        CV_MONITOR.setResultsLocation(Path.of(path, "image_results"));
    }

    @RobotKeyword("""
            Set Match Percentage
            Sets the minimum accepted similarity as a percentage. When attempting to find image matching, if the image
            score is below the given similarity, it will be considered a failure.
            """)
    @ArgumentNames({"minSimilarity"})
    public void setMatchPercentage(double minSimilarity) throws Exception {
        settings.setMatchScore(minSimilarity);
    }

    @RobotKeyword("""
            Adds a location to search for images.
            This path can be relative or absolute.
            """)
    @ArgumentNames({"imageLocation"})
    public void addImageLocation(String imageLocation) {
        CV_MONITOR.addImagePath(imageLocation);
    }

    @RobotKeyword("""
            Set Keystroke Speed
            Sets the delay between each keystroke.
            """)
    @ArgumentNames({"delaySpeed"})
    public void setKeystrokeSpeed(int delaySpeed) {
        KEYBOARD_CONTROLLER.setTypeDelay(Duration.ofMillis(delaySpeed));
    }
}
