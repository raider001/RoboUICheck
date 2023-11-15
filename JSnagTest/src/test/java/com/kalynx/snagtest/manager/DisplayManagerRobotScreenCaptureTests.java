package com.kalynx.snagtest.manager;

import com.kalynx.simplethreadingservice.ThreadService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DisplayManagerRobotScreenCaptureTests {

    private static DisplayManager sut;

    @BeforeAll
    public static void beforeAll() throws AWTException {
        sut = new DisplayManager();
        sut.setDisplay(1);
    }

    @Test
    public void getFramesPerSecond() {
        List<BufferedImage> im = new ArrayList<>();
        Supplier<Boolean> r = () -> {
            try {
                im.add(new Robot(sut.getSelectedDisplay().graphicsDevice()).createScreenCapture(sut.getSelectedDisplayRegion().displayRegion()));
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
            return true;
        };
        int timePeriod = 60;
        ThreadService.schedule(r).forEvery(Duration.ofMillis(100)).over(Duration.ofSeconds(timePeriod)).andWaitForCompletion();
        System.out.println(im.size() / timePeriod);
    }
}
