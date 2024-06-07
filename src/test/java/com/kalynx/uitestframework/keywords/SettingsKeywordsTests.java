package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.KeyboardController;
import com.kalynx.uitestframework.controller.Settings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;

class SettingsKeywordsTests {

    private static SettingsKeywords sut;
    private static Settings cvMonitor;
    private static KeyboardController keyboardController;
    @BeforeAll
    static void beforeAll() throws AlreadyAddedException {
        cvMonitor = Mockito.mock(Settings.class);
        keyboardController = Mockito.mock(KeyboardController.class);
        DI.reset();
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
        Mockito.verify(cvMonitor).setTimeout(Duration.ofMillis(100));
    }

    @Test
    void setPallRate_verification() throws Exception {
        sut.setPollRate(100);
        Mockito.verify(cvMonitor).setPollRate(Duration.ofMillis(100));
    }

    @Test
    void setMathPercentage_verification() throws Exception {
        sut.setMatchPercentage(0.95);
        Mockito.verify(cvMonitor, Mockito.times(1)).setMatchScore(0.95);
    }

    @Test
    void setKeystrokeSpeed_verification() {
        sut.setKeystrokeSpeed(100);
        Mockito.verify(keyboardController, Mockito.times(1)).setTypeDelay(Duration.ofMillis(100));
    }
}
