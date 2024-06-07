package com.kalynx.uitestframework.screen;

import com.kalynx.simplethreadingservice.ThreadService;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.Settings;
import com.kalynx.uitestframework.data.*;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class CvMonitor {
    private final static Map<Integer, Function<Core.MinMaxLocResult,Double>> matchAlgorithm = new HashMap<>();
    private final ImageLibrary imageLibrary = new ImageLibrary();
    private  final DisplayManager displayManager;
    private final Settings settings;
    private Path imageLocation = Path.of(".", "images");
    private Path resultLocation = Path.of(".","log", "image_results");

    public CvMonitor(double matchScore, DisplayManager displayManager, Settings settings) {
        if (matchScore <= 0 || matchScore >= 1) throw new AssertionError("matchScore can only be between 0 and 1");
        matchAlgorithm.put(Imgproc.TM_SQDIFF, r -> 1 - r.minVal);
        matchAlgorithm.put(Imgproc.TM_SQDIFF_NORMED, r -> 1 - r.minVal);
        matchAlgorithm.put(Imgproc.TM_CCOEFF, r -> r.maxVal / 100);
        matchAlgorithm.put(Imgproc.TM_CCOEFF_NORMED, r -> r.maxVal);
        matchAlgorithm.put(Imgproc.TM_CCORR, r -> r.maxVal);
        matchAlgorithm.put(Imgproc.TM_CCORR_NORMED, r -> r.maxVal);
        this.displayManager = displayManager;
        this.settings = settings;
    }

    private static Mat imageToMat(BufferedImage sourceImg) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(sourceImg, "jpg", byteArrayOutputStream);
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            throw new IOException(e);
        }
        return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.IMREAD_COLOR);
    }

    private Result<ScreenshotData> generateSuccessfulResult(Mat matchImage, double expectedSimilarity, Result<Data> successfulResult) throws IOException {
        Data data = successfulResult.getData();
        Point xy1 = data.loc;
        Point xy2 = new Point(data.loc.x + matchImage.cols(), data.loc.y + matchImage.rows());

        Imgproc.rectangle(successfulResult.getData().screenshot(), xy1, xy2,
                new Scalar(0, 0, 255), 2, 8, 0);

        BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(successfulResult.getData().screenshot());
        if(Files.notExists(resultLocation)) Files.createDirectories(resultLocation);
        Path resultLoc = Path.of(resultLocation.toString(), successfulResult.getData().takenTime() + ".jpg");
        ImageIO.write(buffImage, "jpg", resultLoc.toFile());
        String htmlResult = generateHTMLResult(data.score, expectedSimilarity,
                successfulResult.getData().takenTime(), matchImage,
                buffImage);
        Rectangle rect = new Rectangle((int) xy1.x, (int) xy1.y, matchImage.cols(), matchImage.rows());
        return new SuccessfulResult<>(Optional.of(new ScreenshotData(data.takenTime(), data.screenshot(), rect)), htmlResult);
    }

    private Result<ScreenshotData> generateFailedResult(Mat match, double expectedSimilarity, List<Result<Data>> results) throws IOException {
        Comparator<Result<Data>> comp = Comparator.comparingDouble(data -> data.getData().score);
        results.sort(comp);
        Result<Data> res = results.get(0);
        Data data = res.getData();
        int width = match.cols();
        int height = match.rows();
        Imgproc.rectangle(res.getData().screenshot(), data.loc, new Point(data.loc.x + width, data.loc.y + height),
                new Scalar(0, 0, 255), 2, 8, 0);
        BufferedImage buffImage = (BufferedImage) HighGui.toBufferedImage(res.getData().screenshot());

        return new FailedResult<>(generateHTMLResult(
                data.score,
                expectedSimilarity,
                res.getData().takenTime(),
                match,
                buffImage));
    }

    public void setLogLocation(Path logLocation) {
        this.logLocation = logLocation;
        resultLocation = Path.of(logLocation.toString(), imageLocation.toString());

    }

    public void setResultsLocation(Path imageLocation) {
        this.imageLocation = imageLocation;
        resultLocation = imageLocation;
    }

    public void addImagePath(String imagePath) {
        imageLibrary.addLibrary(Path.of(imagePath));

    }

    public List<String> getImagePaths() {
        return imageLibrary.getLibraryPaths();
    }


    public Result<ScreenshotData> monitorForImage(String imageLocation) throws IOException {
        return monitorForImage(settings.getTimeout(), imageLocation, settings.getMatchScore());
    }

    public Result<ScreenshotData> monitorForLackOfImage(Duration duration, String imageLocation, double matchScore) throws IOException {
        Result<MonitorData> data = setupMonitoring(duration, imageLocation, matchScore);

        if (data.isFailure()) return new FailedResult<>(data.getInfo());
        MonitorData monitorData = data.getData();

        Supplier<Result<Data>> action = buildMatchResults(monitorData.templateContainer, monitorData.results, monitorData.requiredMatchScore);
        ThreadService.schedule(action).forEvery(settings.getPollRate()).over(duration).orUntil(Result::isFailure).andWaitForCompletion();
        Optional<Result<Data>> finalResult = monitorData.results.stream().filter(Result::isFailure).findFirst();

        if (finalResult.isPresent()) return generateSuccessfulResult(monitorData.templateContainer.template, monitorData.requiredMatchScore, finalResult.get());

        return generateFailedResult(monitorData.templateContainer.template, monitorData.requiredMatchScore, monitorData.results);
    }

    public Result<ScreenshotData> monitorForImage(Duration duration, String imageLocation, double matchScore) throws IOException {
        Result<MonitorData> data = setupMonitoring(duration, imageLocation, matchScore);

        if (data.isFailure()) return new FailedResult<>(data.getInfo());

        MonitorData monitorData = data.getData();
        Supplier<Result<Data>> action = buildMatchResults(data.getData().templateContainer, monitorData.results, monitorData.requiredMatchScore);
        ThreadService.schedule(action).forEvery(settings.getPollRate()).over(duration).orUntil(Result::isSuccess).andWaitForCompletion();
        Optional<Result<Data>> finalResult = monitorData.results.stream().filter(Result::isSuccess).findFirst();
        if (finalResult.isPresent()) return generateSuccessfulResult(monitorData.templateContainer.template, monitorData.requiredMatchScore, finalResult.get());

        return generateFailedResult(monitorData.templateContainer.template, monitorData.requiredMatchScore, monitorData.results);
    }

    private Result<MonitorData> setupMonitoring(Duration duration, String imageLocation, double matchScore) {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(imageLocation);

        if(matchScore == -1) matchScore = settings.getMatchScore();
        Result<ImageLibrary.TemplateContainer> imageToFind = imageLibrary.findImage(Path.of(imageLocation));

        if (imageToFind.isFailure()) return new FailedResult<>(imageToFind.getInfo());

        MonitorData monitorData = new MonitorData(matchScore, imageToFind.getData());
        return new SuccessfulResult<>(Optional.of(monitorData));
    }

    private static class MonitorData {
        final double requiredMatchScore;
        final ImageLibrary.TemplateContainer templateContainer;
        final List<Result<Data>> results = Collections.synchronizedList(new ArrayList<>());

        private MonitorData(double requiredMatchScore, ImageLibrary.TemplateContainer templateContainer) {
            this.requiredMatchScore = requiredMatchScore;
            this.templateContainer = templateContainer;
        }
    }

    private Supplier<Result<Data>> buildMatchResults(ImageLibrary.TemplateContainer template, List<Result<Data>> results, double requiredMatchScore) {
        return () -> {
            Result<Data> res;
            try {
                res = match(template.template, template.mask, requiredMatchScore);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            results.add(res);
                return res;
        };
    }

    private String generateHTMLResult(double similarity, double expectedSimularity, long time, Mat template, BufferedImage screenshot) throws IOException {
        String screenshotFileName = time + "_screenshot.jpg";
        String expectedFileName = time + "_expected.jpg";
        // Path to write
        Path screenshotPath = Path.of(resultLocation.toString(), screenshotFileName);
        Path expectedResultPath = Path.of(resultLocation.toString(), expectedFileName);

        if(Files.notExists(resultLocation.toAbsolutePath()))
            Files.createDirectories(resultLocation.toAbsolutePath());

        ImageIO.write(screenshot, "jpg", screenshotPath.toFile());
        Imgcodecs.imwrite(expectedResultPath.toString(), template);

        // logLocation
        Path relativeToLogScreenshot = Path.of(imageLocation.toString(), screenshotFileName);
        Path relativeToLogExpected = Path.of(imageLocation.toString(), expectedFileName);
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
                expectedSimularity,
                relativeToLogScreenshot.toString(),
                relativeToLogScreenshot.toString(),
                relativeToLogExpected.toString());
    }


    private Result<Data> match(Mat template, Mat mask, double matchScore) throws IOException {
        mask.convertTo(mask, CvType.CV_8U);
        template.convertTo(template, CvType.CV_8U);
        long takenTime = System.currentTimeMillis();
        BufferedImage image = displayManager.capture();

        Mat screenshot = imageToMat(image);
        screenshot.convertTo(screenshot, CvType.CV_8U);
        int resultCols = screenshot.cols() - template.cols() + 1;
        int resultRows = screenshot.rows() - template.rows() + 1;
        Mat result = new Mat(resultRows, resultCols, CvType.CV_8U);
        int algorithm = Imgproc.TM_SQDIFF_NORMED;
        Imgproc.matchTemplate(screenshot, template, result, algorithm, mask);
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        double res = matchAlgorithm.get(algorithm).apply(mmr);
        double el = res == Double.POSITIVE_INFINITY ? 0 :res;
        double actualScore = res == Double.NEGATIVE_INFINITY ? 1 : el;
        Point p = algorithm == Imgproc.TM_SQDIFF || algorithm == Imgproc.TM_SQDIFF_NORMED ? mmr.minLoc : mmr.maxLoc;
        if (actualScore > matchScore) {
            return new SuccessfulResult<>(Optional.of(new Data(takenTime, screenshot, actualScore, p)));
        }
        return new FailedResult<>("", Optional.of(new Data(takenTime, screenshot, actualScore, p)));
    }

    private record Data(long takenTime, Mat screenshot, double score, Point loc) {
    }
}
