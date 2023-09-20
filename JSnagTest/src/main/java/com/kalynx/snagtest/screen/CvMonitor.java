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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.function.Supplier;
public class CvMonitor {
    private final ImageLibrary imageLibrary = new ImageLibrary();
    private double matchScore = 0.95;
    private Duration pollRate = Duration.ofMillis(100);
    private Duration timeoutTime = Duration.ofMillis(2000);
    private final Map<Integer, DisplayData> displayData = new HashMap<>();
    private int selectedDisplay;
    private final Path imageResultRelativeLocation = Path.of(".", "image_results");
    private Path resultLocation = Path.of(".", imageResultRelativeLocation.toString());

    public CvMonitor(double matchScore, GraphicsDevice[] graphicDevices) throws AWTException {
        if (matchScore <= 0 || matchScore >=1) throw new AssertionError("matchScore can only be between 0 and 1");

        // This really isn't accurate anymore, used to ensure one robot per thread, but now double the robots are made.
        // Leaving because worst case scenario is more memory usage(which is already quite small)
        int cores = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < graphicDevices.length; i++) {
            Rectangle r = graphicDevices[i].getConfigurations()[0].getBounds();
            Rectangle rectangle = new Rectangle(0,0,r.width,r.height);
            ConcurrentLinkedQueue<Robot> robots = new ConcurrentLinkedQueue<>();

            for (int j = 0; j < cores; j++) {
                robots.add(new Robot(graphicDevices[i]));
            }

            displayData.put(i, new DisplayData(r, new Rectangle(r), robots));
        }
        this.selectedDisplay = 0;
    }

    public void setResultsLocation(Path resultLocation) throws Exception {
        resultLocation = resultLocation.resolve(imageResultRelativeLocation);
        if(resultLocation.toFile().exists() && !resultLocation.toFile().isDirectory()) throw new Exception(resultLocation.toString() + " is not a directory.");
        this.resultLocation = resultLocation;

        if(!resultLocation.toFile().exists())
            Files.createDirectories(resultLocation);
    }

    public void addImagePath(String imagePath) {
        imageLibrary.addLibrary(Path.of(imagePath));

    }

    public List<String> getImagePaths() {
        return imageLibrary.getLibraryPaths();
    }

    public void setDisplay (int display) {
        if(display < 0 || display >= displayData.size()) throw new AssertionError("Given display outside of display range.");
        selectedDisplay = display;
    }
    /**
     * Sets the capture region for the currently selected display.
     * @param screenRegion
     */
    public void setCaptureRegion(Rectangle screenRegion) {
        Objects.requireNonNull(screenRegion);
        if (screenRegion.width <= 0) throw new AssertionError("screenRegion width must be greater than 0");
        if (screenRegion.height <= 0) throw new AssertionError("screenRegion height must be greater than 0");
        DisplayData d = displayData.get(selectedDisplay);
        Rectangle adjustedToDisplay = new Rectangle(screenRegion.x + d.displayDimensions.x,
                screenRegion.y + d.displayDimensions.y,
                screenRegion.width,
                screenRegion.height);
        if(d.displayDimensions.x < screenRegion.x ||
           d.displayDimensions.y < screenRegion.y ||
           screenRegion.x + screenRegion.width > d.displayDimensions.width + d.displayDimensions.x ||
           screenRegion.y + screenRegion.height > d.displayDimensions.height + d.displayDimensions.y) throw new AssertionError("Given parameters are not on the screen specified.");

        d.displayRegion().setBounds(adjustedToDisplay);
    }

    public double getMatchScore() {
        return matchScore;
    }

    public Result<String> setMatchScore(double matchScore) {
        if (matchScore <= 0 || matchScore > 1)
            return new FailedResult<>("matchScore must be greater than 0 or equal to/less than 1.");
        this.matchScore = matchScore;
        return new SuccessfulResult<>();
    }

    public Duration getPollRate() {
        return pollRate;
    }

    public Result<String> setPollRate(Duration pollRate) {
        if(pollRate.isNegative() || pollRate.isZero())  new FailedResult<>("pollRate must be greater than 0.");
        if(timeoutTime.toMillis() < pollRate.toMillis()) return new FailedResult<>("pollRate must be less than timeoutTime: " + timeoutTime + "<" + pollRate);
        this.pollRate = pollRate;
        return new SuccessfulResult<>();
    }

    public Duration getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(Duration timeoutTime) {
        if(timeoutTime.isNegative() || timeoutTime.isZero()) throw new AssertionError("timeout time must be greater than 0.");
        if(timeoutTime.toMillis() < pollRate.toMillis()) throw new AssertionError("timeoutTime must be greater than pollRate: " + timeoutTime + "<" + pollRate);
        this.timeoutTime = timeoutTime;
    }

    public Result<ScreenshotData> monitorFor(Duration duration, String imageLocation) throws Exception {
        return monitorFor(duration, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorFor(String imageLocation, double matchScore) throws Exception {
        return monitorFor(timeoutTime, imageLocation, matchScore);
    }
    public Result<ScreenshotData> monitorFor(String imageLocation) throws Exception {
        return monitorFor(timeoutTime, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorFor(Duration duration, String imageLocation, double matchScore) throws Exception {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(imageLocation);
        Result<Mat> result = imageLibrary.findImage(Path.of(imageLocation));
        if(result.isFailure()) return new FailedResult<>(result.getInfo());
        final Mat template = result.getData();
        final Mat mask = new Mat(template.rows(),template.cols(), Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.cvtColor(template, mask, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mask, mask,0,255,Imgproc.THRESH_BINARY);
        List<Result<Data>> results  = Collections.synchronizedList(new ArrayList<>());

        Supplier<Result<Data>> action = () -> {
            try {
                Result<Data> res = match(template, mask, matchScore);
                results.add(res);
                return res;
            } catch(Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.toString());
            }
        };

        Function<Result<Data>, Boolean> condition = Result::isSuccess;

        TemporaryThreadingService.schedule(action).forEvery(pollRate).over(timeoutTime).orUntil(condition).andWaitForCompletion();
        Optional<Result<Data>> finalResult = results.stream().filter(Result::isSuccess).findFirst();

        if(finalResult.isPresent()) {
            Core.MinMaxLocResult locRes = finalResult.get().getData().res();
            Point xy1 = locRes.maxLoc;
            Point xy2 = new Point(locRes.maxLoc.x + template.cols(), locRes.maxLoc.y + template.rows());

            Imgproc.rectangle(finalResult.get().getData().screenshot(), xy1, xy2,
                new Scalar(0, 0, 255), 2, 8, 0);

            BufferedImage buffImage = (BufferedImage)HighGui.toBufferedImage(finalResult.get().getData().screenshot());
            Path resultLoc = Path.of(resultLocation.toString(),  finalResult.get().getData().takenTime() + ".jpg");
            ImageIO.write(buffImage, "jpg", resultLoc.toFile());
            String htmlResult = generateHTMLResult(true, 1 - locRes.maxVal,
                    finalResult.get().getData().takenTime(),template,
                    buffImage);
            Data data = finalResult.get().getData();
            Rectangle rect = new Rectangle((int) xy1.x, (int) xy1.y, template.cols(), template.rows());
            return new SuccessfulResult<>(Optional.of(new ScreenshotData(data.takenTime(), data.screenshot(), rect)), htmlResult);
        }

        Comparator<Result<Data>> comp = Comparator.comparingDouble(data -> data.getData().res().maxVal);
        results.sort(comp);
        Result<Data> res = results.get(0);

        Core.MinMaxLocResult locRes = res.getData().res();
        Imgproc.rectangle(res.getData().screenshot(), locRes.maxLoc, new Point(locRes.maxLoc.x + template.cols(), locRes.maxLoc.y + template.rows()),
                new Scalar(0, 0, 255), 2, 8, 0);
        BufferedImage buffImage = (BufferedImage)HighGui.toBufferedImage(res.getData().screenshot());
        double actualScore = locRes.maxVal == Double.NEGATIVE_INFINITY ? 0 : locRes.minVal == Double.POSITIVE_INFINITY ? 1 : locRes.maxVal;
        return new FailedResult<>(generateHTMLResult(true,
                                             actualScore,
                                            res.getData().takenTime(),
                                            template,
                                            buffImage));
    }

    private String generateHTMLResult(boolean isSuccessful, double similarity, long time, Mat template, BufferedImage screenshot) throws IOException {
        String screenshotFileName = time + "_screenshot.jpg";
        String expectedFileName = time + "_expected.jpg";
        // Path to write
        Path screenshotPath = Path.of(resultLocation.toString(), screenshotFileName);
        Path expectedResultPath = Path.of(resultLocation.toString(), expectedFileName);
        Imgcodecs.imwrite(expectedResultPath.toString(), template);
        ImageIO.write(screenshot, "jpg", screenshotPath.toFile());
        // logLocation
        Path relativeToLogScreenshot = Path.of(imageResultRelativeLocation.toString(), screenshotFileName);
        Path relativeToLogExpected = Path.of(imageResultRelativeLocation.toString(), expectedFileName);
        BigDecimal bd = BigDecimal.valueOf(Double.isInfinite(similarity) ? 1 : similarity);
        bd = bd.setScale(5, RoundingMode.HALF_UP);
        return """
                Best match score: %s
                Needed: %s
                Best Match:
                <table>
                    <tr>
                        <td>Best Match</td>
                        <td>Wanted</td>
                    </tr>
                    <tr>
                        <td><a href="%s"><img src="%s" height="100" width="100"></img></a></td>
                        <td><img src="%s"></img></td>
                    </tr>
                </table>
               """.formatted(bd.toString(),
                            matchScore,
                            relativeToLogScreenshot.toString(),
                            relativeToLogScreenshot.toString(),
                            relativeToLogExpected.toString());
    }
    private Result<Data> match(Mat template, Mat mask, double matchScore) {
        Robot robot = displayData.get(selectedDisplay).robots().poll();
        assert robot != null;
        long takenTime = System.currentTimeMillis();
        BufferedImage image = robot.createScreenCapture(displayData.get(selectedDisplay).displayRegion());
        displayData.get(selectedDisplay).robots().add(robot);
        Mat screenshot = imageToMat(image);
        int result_cols = screenshot.cols() - template.cols() + 1;
        int result_rows = screenshot.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(screenshot, template, result, Imgproc.TM_CCORR_NORMED, mask);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double actualScore = mmr.maxVal == Double.NEGATIVE_INFINITY ? 1 : mmr.maxVal == Double.POSITIVE_INFINITY ? 0 : mmr.maxVal;
        if(actualScore > matchScore) {
            return new SuccessfulResult<>(Optional.of(new Data(takenTime,screenshot, mmr)));
        }

        return new FailedResult<>("",Optional.of(new Data(takenTime, screenshot,mmr)));
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
    private record Data (long takenTime, Mat screenshot, Core.MinMaxLocResult res){}

    private record DisplayData(Rectangle displayDimensions, Rectangle displayRegion, ConcurrentLinkedQueue<Robot> robots){};
}
