package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.SnagTest;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.data.SuccessfulResult;
import com.kalynx.snagtest.screen.CvMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;


@RobotKeywords
public class ScreenKeywords {

    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
    private static final CvMonitor CV_MONITOR = SnagTest.DI.getDependency(CvMonitor.class);

    @RobotKeyword("""
            Set Poll Rate
                        
            | variable | default | unit         |
            | pollRate |   100   | milliseconds |
                        
            Sets the rate the adaption will attempt to poll for updates.
            The poll rate speed is dependent on the system it is actioned on.
                        
            Note that if the poll rate is too slow, validation can take longer as it validates the images received
            sequentially to assess the actual time the image has been displayed on the screen (with the accuracy of the poll rate).
                        
            """)
    @ArgumentNames({"pollRate"})
    public void setPollRate(long pollRate) {
        CV_MONITOR.setPollRate(Duration.ofMillis(pollRate));
    }

    @RobotKeyword("""
            Set Min Similarity
                        
            | variable | default  | unit            |
            | pollRate |   0.95   | percent decimal |
                  
            Sets the minimum accepted similarity as a percentage. When attempting to find image matching, if the image
            score is below the given similarity, it wil lbe considered a failure
            """)
    @ArgumentNames({"minSimilarity"})
    public void setMinSimilarity(double minSimilarity) {
        CV_MONITOR.setMatchScore(minSimilarity);
    }

    @RobotKeyword("""
            Set Timeout Time
                        
            | variable | default  | unit            |
            | pollRate |   0.95   | percent decimal |
                      
            Sets the maximum time to wait for an image to appear. Note that the test event can take longer based on poll rate
            as it does not include evaluation time.
            """)
    @ArgumentNames({"timeoutTime"})
    public void setTimeoutTime(long timeoutTime) {
        CV_MONITOR.setTimeoutTime(Duration.ofMillis(timeoutTime));
    }

    @RobotKeyword("""
            Find Image
                        
            | variable | default  | unit            |
            | image |   0.95   | percent decimal    |
                        
            Looks for the given image on the screen.
            The time, tolerance and poll time for finding the image can be updated to address timing and other needs.
            See:
            - Set Timeout Time
            - Set Minimum Simularity
            - Set Poll Rate            
                        
            for more information
            """)
    @ArgumentNames({"imagePath"})
    public void findImage(String imagePath) throws Exception {
        Result<?> r = CV_MONITOR.monitorFor(imagePath);
        if (r.isFailure()) throw new Exception("*HTML*" + r.getInfo());
        LOGGER.info(r.getInfo());
    }

    @RobotKeyword("""
            Add Image Path
                        
            | variable | default  | unit                  |
            | path     |   N/A    | File Path location    |
                        
            Adds a path to search for images. Multiple paths can be added.
                        
            """)
    @ArgumentNames({"path"})
    public void addImagePath(String path) {
        CV_MONITOR.addImagePath(path);
    }

    @RobotKeyword("""
            Get Image Paths
                        
            Returns all the image paths registered to the agent.
            Useful for debugging.
            """)
    public String getImagePaths() {
        return new SuccessfulResult<>(Optional.of(CV_MONITOR.getImagePaths().toArray(new String[0]))).toJson();
    }

    @RobotKeyword("""
            Set Result Path
                        
            | variable       | default  | unit                  |
            | resultPath     |   ./    | File Path location    |
                        
            The result path should be defined as a relative path
            based on where log files are written to.
            """)
    @ArgumentNames({"resultPath"})
    public void setResultPath(String resultPath) throws Exception {
        Path p = Path.of(resultPath);
        CV_MONITOR.setResultsLocation(p);
    }

    @RobotKeyword("""
            Set Display
                        
            | variable       | default  | unit                  |
            | displayId      |   0      | integer|String        |
                        
            Sets the display to look at.
            """)
    @ArgumentNames({"displayId"})
    public void setDisplay(int display) {
        CV_MONITOR.setDisplay(display);
    }

    @RobotKeyword("""
            Set Display Reference
                        
            | variable       | default  | unit                  |
            | display        |   N/A    | string                |
                        
            Sets the display to look at.
            """
    )
    @ArgumentNames({"display"})
    public void setDisplayByReference(String display) {
        CV_MONITOR.setDisplay(display);
    }

    @RobotKeyword("""
            Set Capture Region
            Sets the capture region for the currently selected display.
                        
            | variable   | default                        | unit                                               |
            | x          |   0                            | integer x >=0 | < display width                    |
            | y          |   0                            | integer y >=0 | < display height                   |
            | width      |   selected display width       | integer x + display width > 0 | < display width    |
            | height     |   selected display height      | integer y + display width > 0 | < display width    |
            The display is selected by:
            - Set Display
            """)
    @ArgumentNames({"x", "y", "width", "height"})
    public void setCaptureRegion(int x, int y, int width, int height) {
        CV_MONITOR.setCaptureRegion(new Rectangle(x, y, width, height));
    }
}
