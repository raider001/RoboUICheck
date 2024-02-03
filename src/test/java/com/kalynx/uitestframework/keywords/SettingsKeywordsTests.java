package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.KeyboardController;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

class SettingsKeywordsTests {

    private static SettingsKeywords sut;
    private static CvMonitor cvMonitor;
    private static KeyboardController keyboardController;
    @BeforeAll
    static void beforeAll() throws AlreadyAddedException {
        cvMonitor = Mockito.mock(CvMonitor.class);
        keyboardController = Mockito.mock(KeyboardController.class);
        DI.getInstance().add(keyboardController);
        DI.getInstance().add(cvMonitor);

        sut = new SettingsKeywords();
    }

    @BeforeEach
    void beforeEach() {
        Mockito.clearInvocations(cvMonitor);
    }

    @Test
    void setTimeoutTime_verification() {
        sut.setTimeoutTime(100);
        Mockito.verify(cvMonitor).setTimeoutTime(Duration.ofMillis(100));
    }

    @Test
    void setPallRate_verification() {
        sut.setPollRate(100);
        Mockito.verify(cvMonitor).setPollRate(Duration.ofMillis(100));
    }

    @Test
    void getImagePaths_verification() {
        Mockito.verify(cvMonitor,Mockito.times(1)).getImagePaths();
    }

    @Test
    void setResultPath_verification() throws Exception {
        sut.setResultPath("path");
        Mockito.verify(cvMonitor, Mockito.times(1)).setResultsLocation(java.nio.file.Path.of("path"));
    }

    @Test
    void setMathPercentage_verification() {
        sut.setMatchPercentage(0.95);
        Mockito.verify(cvMonitor, Mockito.times(1)).setMatchScore(0.95);
    }

    @Test
    void addImageLocation_verification() {
        sut.addImageLocation("path");
        Mockito.verify(cvMonitor, Mockito.times(1)).addImagePath("path");
    }

    @Test
    void setKeystrokeSpeed_verification() {
        sut.setKeystrokeSpeed(100);
        Mockito.verify(keyboardController, Mockito.times(1)).setTypeDelay(Duration.ofMillis(100));
    }
}
