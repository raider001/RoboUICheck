package com.kalynx.robotsikuliwrapper.screen;

import com.kalynx.robotsikuliwrapper.threading.TemporaryThreadingService;
import com.kalynx.robotsikuliwrapper.tmp.Masker;
import org.apache.commons.text.numbers.DoubleFormat;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CvMonitor {
    private final ConcurrentLinkedQueue<Robot> robotInstances = new ConcurrentLinkedQueue<>();

    private Rectangle screenRegion;
    private double matchScore = 0.95;

    private Map<Long, BufferedImage> sampleMap = Collections.synchronizedMap(new HashMap<>());

    public CvMonitor(Rectangle screenRegion, double matchScore) throws AWTException {
        setCaptureRegion(screenRegion);
        if (matchScore <= 0 || matchScore >=100) throw new AssertionError("matchScore can only be between 0 and 1");
        int cores = Runtime.getRuntime().availableProcessors();
        for(int i = 0; i < cores; i++) {
            robotInstances.add(new Robot());
        }
    }

    public void setCaptureRegion(Rectangle screenRegion) {
        Objects.requireNonNull(screenRegion);
        if (screenRegion.width <= 0) throw new AssertionError("screenRegion width must be greater than 0");
        if (screenRegion.height <= 0) throw new AssertionError("screenRegion height must be greater than 0");
        this.screenRegion = screenRegion;
    }
    public void monitorFor(Duration duration, String imageLocation) throws InterruptedException, IOException {
        monitorFor(duration, imageLocation, matchScore);
    }
    public void monitorFor(Duration duration, String imageLocation, double matchScore) throws InterruptedException, IOException {
        long start = System.currentTimeMillis();
        final Mat template = Imgcodecs.imread(imageLocation,Imgcodecs.IMREAD_COLOR);
        final Mat mask = new Mat(template.rows(),template.cols(), Imgcodecs.IMREAD_GRAYSCALE);
        Imgproc.threshold(template, mask,0,255,Imgproc.THRESH_BINARY);
        System.out.println("Mapping time: " + (System.currentTimeMillis() - start));
//        TemporaryThreadingService.schedule(() -> {
//            long snapshotTime= System.currentTimeMillis();
//            BufferedImage image = match(template, mask);
//            sampleMap.put(snapshotTime, image);
//        }).forEvery(Duration.ofMillis(50)).over(duration).andWaitForCompletion();
        // Used to handle the timing of snapshots to be taken. Ultimately will delegate the snapshot to a separate thread.

//        sampleMap.forEach((key,value) -> {
//            try {
//                ImageIO.write(value, "jpg", new File("./images/results/" + key + ".jpg"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
        System.out.println("completionService with " + sampleMap.size() + " complete in " + (System.currentTimeMillis() - start));

    }

    public BufferedImage match(Mat template, Mat mask) {
        Robot robot = robotInstances.poll();
        BufferedImage image = robot.createScreenCapture(screenRegion);
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
        Point matchLoc =  mmr.minLoc;
        BigDecimal bd = BigDecimal.valueOf(mmr.minVal);
        bd = bd.setScale(5, RoundingMode.HALF_UP);
        double matchScore = 1 - mmr.minVal;
        if(matchScore > 0.95) {
            Imgproc.rectangle(screenshot, matchLoc, new Point(matchLoc.x + template.cols(), matchLoc.y + template.rows()),
                    new Scalar(0, 0, 255), 2, 8, 0);
        }
        return (BufferedImage) HighGui.toBufferedImage(screenshot);
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
}
