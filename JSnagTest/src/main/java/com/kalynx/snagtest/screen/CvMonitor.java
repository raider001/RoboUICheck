package com.kalynx.snagtest.screen;

import com.kalynx.simplethreadingservice.ThreadService;
import com.kalynx.snagtest.data.DisplayAttributes;
import com.kalynx.snagtest.data.FailedResult;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.data.SuccessfulResult;
import com.kalynx.snagtest.manager.DisplayManager;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CvMonitor {
    private final ImageLibrary imageLibrary = new ImageLibrary();
    private final Path imageResultRelativeLocation = Path.of(".", "image_results");
    DisplayManager displayManager;
    private double matchScore = 0.95;
    private Duration pollRate = Duration.ofMillis(100);
    private Duration timeoutTime = Duration.ofMillis(2000);
    private Path resultLocation = Path.of(".", imageResultRelativeLocation.toString());

    public CvMonitor(double matchScore, DisplayManager displayManager) throws AWTException {
        if (matchScore <= 0 || matchScore >= 1) throw new AssertionError("matchScore can only be between 0 and 1");
        this.matchScore = matchScore;
        this.displayManager = displayManager;
        // This really isn't accurate anymore, used to ensure one robot per thread, but now double the robots are made.
        // Leaving because worst case scenario is more memory usage(which is already quite small)
        int cores = Runtime.getRuntime().availableProcessors();

        for (int i = 0; i < displayManager.getDisplays().size(); i++) {
            DisplayAttributes r = displayManager.getDisplay(i);
            Rectangle rectangle = new Rectangle(0, 0, r.width(), r.height());
            ConcurrentLinkedQueue<Robot> robots = new ConcurrentLinkedQueue<>();

        }
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

    private Result<ScreenshotData> generateSuccessfulResult(Mat matchImage, Result<Data> successfulResult) throws IOException {
        Core.MinMaxLocResult locRes = successfulResult.getData().res();
        Point xy1 = locRes.maxLoc;
        Point xy2 = new Point(locRes.maxLoc.x + matchImage.cols(), locRes.maxLoc.y + matchImage.rows());

        Imgproc.rectangle(successfulResult.getData().screenshot(), xy1, xy2,
                new Scalar(0, 0, 255), 2, 8, 0);

        BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(successfulResult.getData().screenshot());
        Path resultLoc = Path.of(resultLocation.toString(), successfulResult.getData().takenTime() + ".jpg");
        ImageIO.write(buffImage, "jpg", resultLoc.toFile());
        String htmlResult = generateHTMLResult(true, locRes.maxVal,
                successfulResult.getData().takenTime(), matchImage,
                buffImage);
        Data data = successfulResult.getData();
        Rectangle rect = new Rectangle((int) xy1.x, (int) xy1.y, matchImage.cols(), matchImage.rows());
        return new SuccessfulResult<>(Optional.of(new ScreenshotData(data.takenTime(), data.screenshot(), rect)), htmlResult);
    }

    private Result<ScreenshotData> generateFailedResult(Mat match, List<Result<Data>> results) throws IOException {
        Comparator<Result<Data>> comp = Comparator.comparingDouble(data -> data.getData().res().maxVal);
        results.sort(comp);
        Result<Data> res = results.get(0);

        Core.MinMaxLocResult locRes = res.getData().res();
        Imgproc.rectangle(res.getData().screenshot(), locRes.maxLoc, new Point(locRes.maxLoc.x + match.cols(), locRes.maxLoc.y + match.rows()),
                new Scalar(0, 0, 255), 2, 8, 0);
        BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(res.getData().screenshot());
        double actualScore = locRes.maxVal == Double.NEGATIVE_INFINITY ? 0 : locRes.minVal == Double.POSITIVE_INFINITY ? 1 : 1 - locRes.maxVal;
        return new FailedResult<>(generateHTMLResult(true,
                actualScore,
                res.getData().takenTime(),
                match,
                buffImage));
    }

    public void setResultsLocation(Path resultLocation) throws Exception {
        resultLocation = resultLocation.resolve(imageResultRelativeLocation);
        if (resultLocation.toFile().exists() && !resultLocation.toFile().isDirectory())
            throw new Exception(resultLocation + " is not a directory.");
        this.resultLocation = resultLocation;

        if (!resultLocation.toFile().exists())
            Files.createDirectories(resultLocation);
    }

    public void addImagePath(String imagePath) {
        imageLibrary.addLibrary(Path.of(imagePath));

    }

    public List<String> getImagePaths() {
        return imageLibrary.getLibraryPaths();
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
        if (pollRate.isNegative() || pollRate.isZero()) new FailedResult<>("pollRate must be greater than 0.");
        if (timeoutTime.toMillis() < pollRate.toMillis())
            return new FailedResult<>("pollRate must be less than timeoutTime: " + timeoutTime + "<" + pollRate);
        this.pollRate = pollRate;
        return new SuccessfulResult<>();
    }

    public Duration getTimeoutTime() {
        return timeoutTime;
    }

    public void setTimeoutTime(Duration timeoutTime) {
        if (timeoutTime.isNegative() || timeoutTime.isZero())
            throw new AssertionError("timeout time must be greater than 0.");
        if (timeoutTime.toMillis() < pollRate.toMillis())
            throw new AssertionError("timeoutTime must be greater than pollRate: " + timeoutTime + "<" + pollRate);
        this.timeoutTime = timeoutTime;
    }

    public Result<ScreenshotData> monitorForImage(Duration duration, String imageLocation) throws Exception {
        return monitorForImage(duration, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorForImage(String imageLocation, double matchScore) throws Exception {
        return monitorForImage(timeoutTime, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorForImage(String imageLocation) throws Exception {
        return monitorForImage(timeoutTime, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorForLackOfImage(Duration duration, String imageLocation) throws Exception {
        return monitorForLackOfImage(duration, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorForLackOfImage(String imageLocation, double matchScore) throws Exception {
        return monitorForLackOfImage(timeoutTime, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorForLackOfImage(String imageLocation) throws Exception {
        return monitorForLackOfImage(timeoutTime, imageLocation, matchScore);
    }

    public Result<ScreenshotData> monitorForLackOfImage(Duration duration, String imageLocation, double matchScore) throws IOException {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(imageLocation);
        Result<Mat> imageToFind = imageLibrary.findImage(Path.of(imageLocation));
        if (imageToFind.isFailure()) return new FailedResult<>(imageToFind.getInfo());

        List<Result<Data>> results = Collections.synchronizedList(new ArrayList<>());
        Supplier<Result<Data>> action = buildMatchResults(imageToFind.getData(), results);
        Predicate<Result<Data>> condition = Result::isFailure;
        ThreadService.schedule(action).forEvery(pollRate).over(timeoutTime).orUntil(condition).andWaitForCompletion();
        Optional<Result<Data>> finalResult = results.stream().filter(Result::isFailure).findFirst();

        if (finalResult.isPresent()) return generateSuccessfulResult(imageToFind.getData(), finalResult.get());

        return generateFailedResult(imageToFind.getData(), results);
    }

    public Result<ScreenshotData> monitorForImage(Duration duration, String imageLocation, double matchScore) throws Exception {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(imageLocation);
        Result<Mat> imageToFind = imageLibrary.findImage(Path.of(imageLocation));

        if (imageToFind.isFailure()) return new FailedResult<>(imageToFind.getInfo());

        List<Result<Data>> results = Collections.synchronizedList(new ArrayList<>());

        Supplier<Result<Data>> action = buildMatchResults(imageToFind.getData(), results);
        Predicate<Result<Data>> condition = Result::isSuccess;

        ThreadService.schedule(action).forEvery(pollRate).over(timeoutTime).orUntil(condition).andWaitForCompletion();
        Optional<Result<Data>> finalResult = results.stream().filter(Result::isSuccess).findFirst();

        if (finalResult.isPresent()) return generateSuccessfulResult(imageToFind.getData(), finalResult.get());

        return generateFailedResult(imageToFind.getData(), results);
    }

    private Supplier<Result<Data>> buildMatchResults(Mat template, List<Result<Data>> results) {
        final Mat mask = createMask(template);

        return () -> {
            try {
                Result<Data> res = match(template, mask, matchScore);
                results.add(res);
                return res;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.toString());
            }
        };
    }

    private Mat createMask(Mat template) {
        final Mat mask = new Mat(template.rows(), template.cols(), Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.cvtColor(template, mask, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(mask, mask, 0, 255, Imgproc.THRESH_BINARY);
        return mask;
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

    public BufferedImage capture() {
        DisplayManager.DisplayData displayData = displayManager.getSelectedDisplayRegion();

        Robot robot = displayManager.getSelectedDisplayRegion().robots().poll();
        assert robot != null;
        BufferedImage img = robot.createScreenCapture(displayData.displayRegion());
        displayManager.getSelectedDisplayRegion().robots().add(robot);
        return img;
    }

    private Result<Data> match(Mat template, Mat mask, double matchScore) {

        long takenTime = System.currentTimeMillis();
        BufferedImage image = capture();

        Mat screenshot = imageToMat(image);
        int result_cols = screenshot.cols() - template.cols() + 1;
        int result_rows = screenshot.rows() - template.rows() + 1;
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);

        Imgproc.matchTemplate(screenshot, template, result, Imgproc.TM_CCORR_NORMED, mask);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double actualScore = mmr.maxVal == Double.NEGATIVE_INFINITY ? 1 : mmr.maxVal == Double.POSITIVE_INFINITY ? 0 : mmr.maxVal;
        if (actualScore > matchScore) {
            return new SuccessfulResult<>(Optional.of(new Data(takenTime, screenshot, mmr)));
        }
        return new FailedResult<>("", Optional.of(new Data(takenTime, screenshot, mmr)));
    }

    private record Data(long takenTime, Mat screenshot, Core.MinMaxLocResult res) {
    }

    private record DisplayData(Rectangle displayRegion, ConcurrentLinkedQueue<Robot> robots) {
    }

}
