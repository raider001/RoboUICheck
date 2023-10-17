package com.kalynx.snagtest.control;

import com.kalynx.snagtest.data.DisplayAttributes;
import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.wrappers.MouseInfoControl;
import com.kalynx.snagtest.wrappers.RobotControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class MouseControllerTest {

    private static final List<Rectangle> displays = new ArrayList<>();
    private static DisplayManager displayManager;
    MouseController sut;
    private RobotControl robotControl;
    private MouseInfoControl mouseInfoControl;

    @BeforeAll
    static void beforeAll() {
        displayManager = new DisplayManager(new DisplayAttributes(0, null, false, -1000, 0, 1000, 1000),
                new DisplayAttributes(1, null, false, 0, 0, 1000, 1000),
                new DisplayAttributes(2, null, false, 0, 0, 1000, 1000),
                new DisplayAttributes(3, null, false, 1000, 0, 1000, 500));
        displayManager.setPrimaryReference("PRIMARY");

    }

    @BeforeEach
    void beforeEach() {
        robotControl = Mockito.mock(RobotControl.class);
        mouseInfoControl = Mockito.mock(MouseInfoControl.class);
        sut = new MouseController(robotControl, mouseInfoControl, displayManager, p -> robotControl.mouseMove(p.x, p.y));
    }

    @Test
    void mouseMoveToDisplay_insideBounds_mouseMoved() throws Exception {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10, 10));
        sut.moveMouseTo("PRIMARY", 100, 100);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(1100, 100);
    }

    @Test
    void mouseMoveTo_insideDisplayBounds_mouseMoved() throws Exception {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10, 10));
        sut.moveMouseTo(100, 100);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(100, 100);
        sut.moveMouseTo(0, 0);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(0, 0);
        sut.moveMouseTo(1000, 1000);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(1000, 1000);
    }

    @Test
    void mouseMoveTo_outOfBounds_exceptionFired() {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10, 10));
        Assertions.assertThrows(Exception.class, () -> sut.moveMouseTo(-100, 100));
        Assertions.assertThrows(Exception.class, () -> sut.moveMouseTo(100, -100));
        Assertions.assertThrows(Exception.class, () -> sut.moveMouseTo(1001, 0));
        Assertions.assertThrows(Exception.class, () -> sut.moveMouseTo(0, 1001));
    }

    @Test
    void moveMouse_insideBounds_mouseMoved() {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10, 10));
        sut.moveMouse(100, 100);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(110, 110);
    }

    @Test
    void moveMouse_outsideBounds_exceptionRaised() {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10, 10));
        Assertions.assertThrows(Exception.class, () -> sut.moveMouseTo(-15, 100));

        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10, 10));
        Assertions.assertThrows(Exception.class, () -> sut.moveMouseTo(100, -15));
    }
}
