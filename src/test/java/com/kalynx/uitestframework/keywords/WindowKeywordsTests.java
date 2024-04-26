package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.WindowController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

class WindowKeywordsTests {

    private static WindowKeywords sut;
    private static WindowController windowController;
    private static final List<String> WINDOWS = List.of("window1", "window2","window3");
    @BeforeAll
    static void beforeAll() throws AlreadyAddedException {
        windowController = Mockito.mock(WindowController.class);
        DI.reset();
        DI.getInstance().add(windowController);
        Mockito.when(windowController.getAllWindows()).thenReturn(WINDOWS);
        sut = new WindowKeywords();
    }

    @BeforeEach
    void beforeEach() {
        Mockito.clearInvocations(windowController);
    }

    @Test
    void getAllAvailableWindows_verification() {
        sut.getAllAvailableWindows();
        Mockito.verify(windowController, Mockito.times(1)).getAllWindows();
    }

    @Test
    void getWindowDimensions_verification() throws Exception {
        Rectangle r = new Rectangle(0,0,10,10);
        Mockito.when(windowController.getWindowDimensions("windowName")).thenReturn(null);
        Mockito.when(windowController.getWindowDimensions("window1")).thenReturn(r);
        Exception e = Assertions.assertThrows(Exception.class, () -> sut.getWindowDimensions("windowName"));
        Assertions.assertEquals("Window:windowName not found. Available windows:[window1, window2, window3]", e.getMessage());
        Map<String,Integer> result = sut.getWindowDimensions("window1");
        Map<String, Integer> expected = Map.of("x", 0, "y", 0, "width", 10, "height", 10);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void bringWindowToFront_verification() throws Exception {
        Mockito.when(windowController.bringToFront("windowName")).thenReturn(false);
        Mockito.when(windowController.bringToFront("window1")).thenReturn(true);
        Exception e = Assertions.assertThrows(Exception.class, () -> sut.bringWindowToFront("windowName"));
        Assertions.assertEquals("Window:windowName not found. Available windows:[window1, window2, window3]", e.getMessage());
        sut.bringWindowToFront("window1");
        Mockito.verify(windowController, Mockito.times(1)).bringToFront("window1");
    }

    @Test
    void moveWindow_verification() throws Exception {
        Exception e = Assertions.assertThrows(Exception.class, () -> sut.moveWindow("windowName", 0, 0, null));
        Assertions.assertEquals("Window:windowName not found. Available windows:[window1, window2, window3]", e.getMessage());

        Mockito.when(windowController.setWindowPosition("window3", 0, 0)).thenReturn(false);
        e = Assertions.assertThrows(Exception.class, () -> sut.moveWindow("window3",0,0, null));
        Assertions.assertEquals("Window attempted to be moved out of bounds", e.getMessage());

        Mockito.when(windowController.setWindowPosition("window1", 0, 0)).thenReturn(true);
        sut.moveWindow("window1", 0, 0, null);
    }

    @Test
    void setWindowSize_verification() {
        Exception e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.resizeWindow("window1", -1, 0));
        Assertions.assertEquals("Width and height must be greater than 0", e.getMessage());

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.resizeWindow("window1", 0, -1));
        Assertions.assertEquals("Width and height must be greater than 0", e.getMessage());

        Mockito.when(windowController.getAllWindows()).thenReturn(WINDOWS);
        Exception e2 = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.resizeWindow("windowName", 1, 1));
        Assertions.assertEquals("Window:windowName not found. Available windows:[window1, window2, window3]", e2.getMessage());

        sut.resizeWindow("window1", 1, 1);
        Mockito.verify(windowController, Mockito.times(1)).setWindowSize("window1", 1, 1);
    }

}
