package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.MouseEvent.MouseButtonDown;
import com.kalynx.uitestframework.controller.MouseController;
import com.kalynx.uitestframework.exceptions.MouseException;
import com.kalynx.uitestframework.screen.CvMonitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MouseKeywordTests {
    private static MouseKeywords sut;
    private static MouseController mouseController;
    private static CvMonitor cvMonitor;
    @BeforeAll
    public static void beforeAll() throws AlreadyAddedException {
        mouseController = Mockito.mock(MouseController.class);
        cvMonitor = Mockito.mock(CvMonitor.class);
        DI.reset();
        DI.getInstance().add(mouseController);
        DI.getInstance().add(cvMonitor);
        sut = new MouseKeywords();
    }

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(mouseController);
        Mockito.reset(cvMonitor);
    }
    @Test
    void mouseMove_verification() {
        sut.moveMouse(1, 2);
        Mockito.verify(mouseController).moveMouse(1, 2);
    }

    @Test
    void mouseMoveToDisplay_verification() throws Exception {
        sut.moveMouseTo(1, 2, null, "display", null);
        Mockito.verify(mouseController).moveMouseTo("display", 1, 2);
    }

    @Test
    void mouseMoveTo_verification() throws Exception {
        sut.moveMouseTo(1, 2, null, null, null);
        Mockito.verify(mouseController).moveMouseTo(1, 2);
    }

    @Test
    void click_extension_verification() throws Exception {
        sut.click("LEFT", 1, null, null, null, null, null);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.LEFT, 1);

        sut.click("RIGHT", 3, null, null, null, null, null);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.RIGHT, 3);

        sut.click("MIDDLE", 5, null, null, null, null, null);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.MIDDLE, 5);

        Exception e = Assertions.assertThrows(Exception.class, () -> sut.click("INVALID", 1, null, null, null, null, null));
        Assertions.assertEquals("Invalid Click option INVALID given.", e.getMessage());
    }

    @Test
    void click_verification() throws Exception {
        sut.click("LEFT", 1, null, null, null, null, null);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.LEFT, 1);

        sut.click("RIGHT",1, null, null, null, null, null);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.LEFT, 1);

        sut.click("MIDDLE",1, null, null, null, null, null);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.LEFT, 1);

        Exception e = Assertions.assertThrows(Exception.class, () -> sut.click("INVALID", 1, null, null, null, null, null));
        Assertions.assertEquals("Invalid Click option INVALID given.", e.getMessage());
    }

    @Test
    void clickLocation_verification() throws Exception {

        Exception e = Assertions.assertThrows(Exception.class, () -> sut.click("LEFT", 0, 2,-1, null, null, null));
        Assertions.assertEquals("Mouse click count must be greater than 0", e.getMessage());

        sut.click("LEFT", 3, 4,2, null, null, null);
        Mockito.verify(mouseController).moveMouseTo(4, 2);
        Mockito.verify(mouseController).mouseClick(MouseButtonDown.LEFT, 3);
    }

    @Test
    void pressMouseButton_verification() throws Exception {
        sut.pressMouseButton("LEFT");
        Mockito.verify(mouseController).mousePress(MouseButtonDown.LEFT);

        sut.pressMouseButton("RIGHT");
        Mockito.verify(mouseController).mousePress(MouseButtonDown.RIGHT);

        sut.pressMouseButton("MIDDLE");
        Mockito.verify(mouseController).mousePress(MouseButtonDown.MIDDLE);

        Exception e = Assertions.assertThrows(Exception.class, () -> sut.pressMouseButton("INVALID"));
        Assertions.assertEquals("Invalid Press option INVALID given.", e.getMessage());
    }

    @Test
    void releaseMouseButton_verification() throws Exception {
        sut.releaseMouseButton("LEFT");
        Mockito.verify(mouseController).mouseRelease(MouseButtonDown.LEFT);

        sut.releaseMouseButton("RIGHT");
        Mockito.verify(mouseController).mouseRelease(MouseButtonDown.RIGHT);

        sut.releaseMouseButton("MIDDLE");
        Mockito.verify(mouseController).mouseRelease(MouseButtonDown.MIDDLE);

        Exception e = Assertions.assertThrows(Exception.class, () -> sut.releaseMouseButton("INVALID"));
        Assertions.assertEquals("Invalid Release option INVALID given.", e.getMessage());
    }

    @Test
    void setMouseMoveSpeed_verification() throws MouseException {
        sut.setMouseMoveSpeed(100L);
        Mockito.verify(mouseController).setMouseMoveSpeed(100L);
    }

    @Test
    void mouseScrollUp_verification() {
        sut.mouseScroll(100);
        Mockito.verify(mouseController).mouseScroll(100);
    }

    @Test
    void mouseScrollDown_verification() {
        sut.mouseScroll(-100);
        Mockito.verify(mouseController).mouseScroll(-100);
    }
}
