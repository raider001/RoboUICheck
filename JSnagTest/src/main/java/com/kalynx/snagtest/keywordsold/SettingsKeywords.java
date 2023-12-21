package com.kalynx.snagtest.keywordsold;

import com.kalynx.snagtest.SnagTestOld;
import com.kalynx.snagtest.data.SuccessfulResult;
import com.kalynx.snagtest.screen.CvMonitor;
import com.kalynx.snagtest.screen.Ocr;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;

@RobotKeywords
public class SettingsKeywords {

    private static final CvMonitor CV_MONITOR = SnagTestOld.DI.getDependency(CvMonitor.class);
    private final Ocr OCR = SnagTestOld.DI.getDependency(Ocr.class);

    @RobotKeyword("""
            Set Timeout Time
                        
            | variable    | default  | unit            |
            | timeoutTime |   2000   | milliseconds    |
                      
            Sets the maximum time to wait for an image to appear. Note that the test event can take longer based on poll rate
            as it does not include evaluation time.
            """)

    @ArgumentNames({"timeoutTime"})
    public void setTimeoutTime(long timeoutTime) {
        CV_MONITOR.setTimeoutTime(Duration.ofMillis(timeoutTime));
    }

    @RobotKeyword("""
            Set Match Percentage
                        
            | variable | default  | unit            |
            | similarity |   0.95   | percent decimal |
                  
            Sets the minimum accepted similarity as a percentage. When attempting to find image matching, if the image
            score is below the given similarity, it wil lbe considered a failure
            """)
    @ArgumentNames({"minSimilarity"})
    public void setMatchPercentage(double minSimilarity) {
        CV_MONITOR.setMatchScore(minSimilarity);
    }

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
            Set Result Path
                        
            | variable       | default  | unit                                        |
            | resultPath     |   ./     | File Path location from jSnagTest server    |
                        
            The result path should be defined as a relative path
            based on where log files are written to.
            """)
    @ArgumentNames({"resultPath"})
    public void setResultPath(String resultPath) throws Exception {
        Path p = Path.of(resultPath);
        CV_MONITOR.setResultsLocation(p);
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
            Set PSM
            Existing Modes
            0  OSD_ONLY Orientation and script detection (OSD) only.
            1  AUTO_OSD Automatic page segmentation with OSD.
            2  AUTO_ONLY Automatic page segmentation, but no OSD, or OCR.
            3  AUTO Fully automatic page segmentation, but no OSD. (Default)
            4  SINGLE_COLUMN Assume a single column of text of variable sizes.
            5  SINGLE_BLOCK_VERT_TEXT Assume a single uniform block of vertically aligned text.
            6  SINGLE_COLUMN Assume a single uniform block of text.
            7  SINGLE_LINE Treat the image as a single text line.
            8  SINGLE_WORD Treat the image as a single word.
            9  CIRCLE_WORD Treat the image as a single word in a circle.
            10 SINGLE_CHAR Treat the image as a single character.
            11 SPARSE_TEXT Sparse text. Find as much text as possible in no particular order.
            12 SPARSE_TEXT_OSD Sparse text with OSD.
            13 RAW_LINE Raw line. Treat the image as a single text line, bypassing hacks that are Tesseract-
            """)
    @ArgumentNames({"psm"})
    public void setPsm(int psm) {
        OCR.setPsm(psm);
    }

    @RobotKeyword("Set OCR Engine")
    public void setOCREngine(int engine) {
        OCR.setOcrEngineMode(engine);
    }

    @RobotKeyword("Set OCR Languae")
    public void setOCRLanguage(String language) {
        OCR.setLanguage(language);
    }
}
