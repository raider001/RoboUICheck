package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.control.MainController;
import com.kalynx.snagtest.data.Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.image.BufferedImage;
import java.time.Duration;

@RobotKeywords
public class ScreenKeywords {
    private static final Log LOG = LogFactory.getLog(ScreenKeywords.class);
    @RobotKeyword("""
            Set Min Similarity
            """)
    @ArgumentNames({"Minimum Similarity"})
    public void setMinSimilarity(double minSimilarity) {
        MainController.getInstance().getCvMonitor().setMatchScore(minSimilarity);
    }

    @RobotKeyword("""
            Set Poll Rate
            """)
    @ArgumentNames({"Set Poll Rate"})
    public void setPollRate(long pollRate) {
        MainController.getInstance().getCvMonitor().setPollRate(Duration.ofMillis(pollRate));
    }

    @RobotKeyword("""
            Set Timeout Time
            """)
    @ArgumentNames({"timeoutTime"})
    public void setTimeoutTime(long timeoutTime) {
        MainController.getInstance().getCvMonitor().setTimeoutTime(Duration.ofMillis(timeoutTime));
    }

    @RobotKeyword("""
            Find Image
            """)
    @ArgumentNames({"image"})
    public void findImage(String image) throws Exception {
        Result<BufferedImage> result = MainController.getInstance().getCvMonitor().monitorFor(image);
        if(result.isFailure()) {
            throw new Exception(result.getInfo());
        } else {
            LOG.info(result.getInfo());
        }
    }

    @RobotKeyword("""
            Add Image Path
            """)
    @ArgumentNames({"path"})
    public void addImagePath(String path) {
        MainController.getInstance().getCvMonitor().addImagePath(path);
    }

    @RobotKeyword("""
            Get Image Paths
            """)
    public String[] getImagePaths() {
        return MainController.getInstance().getCvMonitor().getImagePaths().toArray(new String[0]);
    }
}
