package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.data.FailedResult;
import com.kalynx.uitestframework.data.SuccessfulResult;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.Duration;
import java.util.Optional;

class ScreenKeywordsTests {
    private ScreenKeywords sut;
    private static CvMonitor cvMonitor;
    private static DisplayManager displayManager;

    private static final String FOUND_IMAGE = "foundImage";
    private static final String LOST_IMAGE = "lostImage";
    @BeforeAll
    public static void before() throws Exception {
        DI.reset();

        cvMonitor = Mockito.mock(CvMonitor.class);
        displayManager = Mockito.mock(DisplayManager.class);

        DI.getInstance().add(cvMonitor);
        DI.getInstance().add(displayManager);
    }
    @BeforeEach
    public void beforeEach() {
        Mockito.clearInvocations(cvMonitor);
        Mockito.clearInvocations(displayManager);
        sut = new ScreenKeywords();
    }

    @Test
    void verifyImageExists_verification() throws Exception {

        // Case 1
        Mockito.when(cvMonitor.monitorForImage(FOUND_IMAGE)).thenReturn(new SuccessfulResult<>(Optional.empty() , ""));
        Mockito.when(cvMonitor.monitorForImage(LOST_IMAGE)).thenReturn(new FailedResult<>(""));

        sut.verifyImageExists(FOUND_IMAGE);
        Mockito.verify(cvMonitor).monitorForImage(FOUND_IMAGE);
        Assertions.assertThrows(Exception.class, () -> sut.verifyImageExists(LOST_IMAGE));

        // Case 2
        Mockito.when(cvMonitor.monitorForImage(FOUND_IMAGE,0.95)).thenReturn(new SuccessfulResult<>(""));
        Mockito.when(cvMonitor.monitorForImage(LOST_IMAGE,0.95)).thenReturn(new FailedResult<>(""));

        sut.verifyImageExists(FOUND_IMAGE, 0.95);
        Mockito.verify(cvMonitor).monitorForImage(FOUND_IMAGE, 0.95);
        Assertions.assertThrows(Exception.class, () -> sut.verifyImageExists(LOST_IMAGE, 0.95));

        // Case 3
        Mockito.when(cvMonitor.monitorForImage(Duration.ofMillis(2000), FOUND_IMAGE, 0.95)).thenReturn(new SuccessfulResult<>(Optional.empty() , ""));
        Mockito.when(cvMonitor.monitorForImage(Duration.ofMillis(2000), LOST_IMAGE, 0.95)).thenReturn(new FailedResult<>(""));

        sut.verifyImageExists(FOUND_IMAGE, 0.95,2000);
        Mockito.verify(cvMonitor).monitorForImage(Duration.ofMillis(2000), FOUND_IMAGE, 0.95);
        Assertions.assertThrows(Exception.class, () -> sut.verifyImageExists(LOST_IMAGE, 0.95, 2000));

        // case 4
        Mockito.when(cvMonitor.monitorForImage(Duration.ofMillis(2000), FOUND_IMAGE)).thenReturn(new SuccessfulResult<>(Optional.empty() , ""));
        Mockito.when(cvMonitor.monitorForImage(Duration.ofMillis(2000), LOST_IMAGE)).thenReturn(new FailedResult<>(""));

        sut.verifyImageExists(FOUND_IMAGE, 2000);
        Mockito.verify(cvMonitor).monitorForImage(Duration.ofMillis(2000), FOUND_IMAGE);
        Assertions.assertThrows(Exception.class, () -> sut.verifyImageExists(LOST_IMAGE,2000));
    }
}
