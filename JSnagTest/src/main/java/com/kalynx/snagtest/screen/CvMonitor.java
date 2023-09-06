package com.kalynx.snagtest.screen;

import com.kalynx.snagtest.data.FailedResult;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.data.SuccessfulResult;
import com.kalynx.snagtest.threading.TemporaryThreadingService;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.Supplier;
public class CvMonitor {
    private final ConcurrentLinkedQueue<Robot> robotInstances = new ConcurrentLinkedQueue<>();
    private final ImageLibrary imageLibrary = new ImageLibrary();
    private double matchScore = 0.95;
    private Duration pollRate = Duration.ofMillis(100);
    private Duration timeoutTime = Duration.ofMillis(2000);
    private final List<Rectangle> displays;
    private final Map<Rectangle, Rectangle> displayRegions = new HashMap<>();
    private Rectangle selectedDisplay;
    public CvMonitor(double matchScore, List<Rectangle> screenDisplays) throws AWTException {

        if (matchScore <= 0 || matchScore >=1) throw new AssertionError("matchScore can only be between 0 and 1");
        int cores = Runtime.getRuntime().availableProcessors();
        this.displays = screenDisplays;
        this.displays.forEach(display -> displayRegions.put(display, display));
        selectedDisplay = displays.get(0);
        setCaptureRegion(displayRegions.get(this.displays.get(0)));

        for(int i = 0; i < cores; i++) {
            robotInstances.add(new Robot());
        }
    }

    public void addImagePath(String imagePath) {
        imageLibrary.addLibrary(Path.of(imagePath));
    }

    public List<String> getImagePaths() {
        return imageLibrary.getLibraryPaths();
    }

    public void setDisplay (int display) {
        if(display < 0 || display > displays.size()) throw new AssertionError("Given display outside of display range.");
        selectedDisplay = displays.get(display);
    }
    /**
     * Sets the capture region for the currently selected display.
     * @param screenRegion
     */
    public void setCaptureRegion(Rectangle screenRegion) {
        Objects.requireNonNull(screenRegion);
        if (screenRegion.width <= 0) throw new AssertionError("screenRegion width must be greater than 0");
        if (screenRegion.height <= 0) throw new AssertionError("screenRegion height must be greater than 0");

        Rectangle adjustedToDisplay = new Rectangle(screenRegion.x + selectedDisplay.x,
                screenRegion.y + selectedDisplay.y,
                screenRegion.width,
                screenRegion.height);
        if(selectedDisplay.x < screenRegion.x ||
           selectedDisplay.y < screenRegion.y ||
           screenRegion.x + screenRegion.width > selectedDisplay.width + selectedDisplay.x ||
           screenRegion.y + screenRegion.height > selectedDisplay.height + selectedDisplay.y) throw new AssertionError("Given parameters are not on the screen specified.");

        displayRegions.put(selectedDisplay, adjustedToDisplay);
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        if (matchScore <= 0 || matchScore > 1) throw new AssertionError("matchScore must be greater than 0 or equal to/less than 1.");
        this.matchScore = matchScore;
    }

    public Duration getPollRate() {
        return pollRate;
    }

    public void setPollRate(Duration pollRate) {
        if(pollRate.isNegative() || pollRate.isZero()) throw new AssertionError("pollRate must be greater than 0.");
        if(timeoutTime.toMillis() < pollRate.toMillis()) throw new AssertionError("pollRate must be less than timeoutTime: " + timeoutTime + "<" + pollRate);

        this.pollRate = pollRate;
    }

    public Duration getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(Duration timeoutTime) {
        if(timeoutTime.isNegative() || timeoutTime.isZero()) throw new AssertionError("timeout time must be greater than 0.");
        if(timeoutTime.toMillis() < pollRate.toMillis()) throw new AssertionError("timeoutTime must be greater than pollRate: " + timeoutTime + "<" + pollRate);
        this.timeoutTime = timeoutTime;
    }

    public void monitorFor(Duration duration, String imageLocation) throws Exception {
        monitorFor(duration, imageLocation, matchScore);
    }

    public Result<BufferedImage> monitorFor(String imageLocation, double matchScore) throws Exception {
        return monitorFor(timeoutTime, imageLocation, matchScore);
    }
    public Result<BufferedImage> monitorFor(String imageLocation) throws Exception {
        return monitorFor(timeoutTime, imageLocation, matchScore);
    }

    public Result<BufferedImage> monitorFor(Duration duration, String imageLocation, double matchScore) throws Exception {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(imageLocation);
        Result<Mat> result = imageLibrary.findImage(Path.of(imageLocation));
        if(result.isFailure()) throw new Exception(result.getInfo());
        final Mat template = result.getData();
        final Mat mask = new Mat(template.rows(),template.cols(), Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.threshold(template, mask,0,255,Imgproc.THRESH_BINARY);
        List<Result<ScreenshotData>> results = new ArrayList<>();

        Supplier<Result<ScreenshotData>> action = () -> {
            Result<ScreenshotData> res = match(template, mask, matchScore);
            results.add(res);
            return res;
        };
        Function<Result<ScreenshotData>, Boolean> condition = Result::isSuccess;

        TemporaryThreadingService.schedule(action).forEvery(pollRate).over(timeoutTime).orUntil(condition).andWaitForCompletion();

        Optional<Result<ScreenshotData>> finalResult = results.stream().filter(Result::isSuccess).findFirst();

        if(finalResult.isPresent()) {
            Core.MinMaxLocResult locRes = finalResult.get().getData().foundLocation;
            Imgproc.rectangle(finalResult.get().getData().screenshot(), locRes.minLoc, new Point(locRes.minLoc.x + template.cols(), locRes.minLoc.y + template.rows()),
                new Scalar(0, 0, 255), 2, 8, 0);
            return new SuccessfulResult<>(Optional.of((BufferedImage)HighGui.toBufferedImage(finalResult.get().getData().screenshot())));
        }

        Comparator<Result<ScreenshotData>> comp = Comparator.comparingDouble(data -> data.getData().foundLocation.minVal);
        results.sort(comp);
        Result<ScreenshotData> res = results.get(0);

        Core.MinMaxLocResult locRes = res.getData().foundLocation;
        Imgproc.rectangle(res.getData().screenshot(), locRes.minLoc, new Point(locRes.minLoc.x + template.cols(), locRes.minLoc.y + template.rows()),
                new Scalar(0, 0, 255), 2, 8, 0);
        return new FailedResult<>("""
        Image not found, closest match: %s
        <img src='fake.png'> </img>
        """.formatted(1 - locRes.minVal),Optional.of((BufferedImage)HighGui.toBufferedImage(res.getData().screenshot())));
    }

    private Result<ScreenshotData> match(Mat template, Mat mask, double matchScore) {
        Robot robot = robotInstances.poll();
        assert robot != null;
        BufferedImage image = robot.createScreenCapture(displayRegions.get(selectedDisplay));
        robotInstances.add(robot);
        Mat screenshot = imageToMat(image);
        int result_cols = screenshot.cols() - template.cols() + 1;
        int result_rows = screenshot.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        // TODO - Add Masking capabilities
        // TODO - Imgproc.TM_SQDIFF || match_method == Imgproc.TM_SQDIFF_NORMED
        // TODO - Note for SQDIFF. Lower numbers are better
        Imgproc.matchTemplate(screenshot, template, result, Imgproc.TM_SQDIFF_NORMED, mask);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        BigDecimal bd = BigDecimal.valueOf(mmr.minVal);
        bd = bd.setScale(5, RoundingMode.HALF_UP);
        double actualScore = 1 - mmr.minVal;
        if(actualScore > matchScore) {
            return new SuccessfulResult<>(Optional.of(new ScreenshotData(screenshot, mmr)),
                    """
                    Image found with similarity of %s
                    <img src="fake.png"></img>
                    """.formatted(actualScore));
        }

        return new FailedResult<>("""
                Image with min similarity of %s not found.
                Best similarity: %s
                """.formatted(this.matchScore, matchScore),Optional.of(new ScreenshotData(screenshot,mmr)));
    }

    private static Mat imageToMat(BufferedImage sourceImg) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(sourceImg, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_COLOR);
    }

    public record ScreenshotData(Mat screenshot, Core.MinMaxLocResult foundLocation){}
}
