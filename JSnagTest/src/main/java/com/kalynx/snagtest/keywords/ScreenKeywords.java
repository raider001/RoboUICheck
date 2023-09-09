package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.control.MainController;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.data.SuccessfulResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

@RobotKeywords
public class ScreenKeywords {

     private final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

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
        Result<?> r = MainController.getInstance().getCvMonitor().monitorFor(image);
        if(r.isFailure()) throw new Exception(r.getInfo());
        LOGGER.info(r.getInfo());
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
    public String getImagePaths() {
        return new SuccessfulResult<>(Optional.of(MainController.getInstance().getCvMonitor().getImagePaths().toArray(new String[0]))).toJson();
    }

    @RobotKeyword("""
            Set Result Path
            """)
    public void setResultPath(String resultPath) throws Exception {
         Path p = Path.of(resultPath);
         MainController.getInstance().getCvMonitor().setResultsLocation(p);
    }
 }
