package com.kalynx.snagtest.control;

import com.kalynx.snagtest.wrappers.MouseInfoControl;
import com.kalynx.snagtest.wrappers.RobotControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MouseControllerTest {

    MouseController sut;
    private RobotControl robotControl;
    private MouseInfoControl mouseInfoControl;
    private static final List<Rectangle> displays = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        displays.add(new Rectangle(-1000,0,1000,1000));
        displays.add(new Rectangle(0,0,1000,1000));
        displays.add(new Rectangle(1000,0,1000,500));
    }
    @BeforeEach
    void beforeEach() {
        robotControl = Mockito.mock(RobotControl.class);
        mouseInfoControl = Mockito.mock(MouseInfoControl.class);
        sut = new MouseController(robotControl, mouseInfoControl, displays);
    }

    @Test
    void mouseMoveToDisplay_insideBounds_mouseMoved() {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10,10));
        sut.moveMouseToLocation(2, 100, 100);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(1100,100);
    }
    @Test
    void mouseMove_insideDisplayBounds_mouseMoved() {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10,10));
        sut.moveMouseToLocation(100,100);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(100,100);
        sut.moveMouseToLocation(0,0);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(0,0);
        sut.moveMouseToLocation(1000,1000);
        Mockito.verify(robotControl, Mockito.times(1)).mouseMove(1000,1000);
    }

    @Test
    void mouseMove_outOfBounds_exceptionFired() {
        Mockito.when(mouseInfoControl.getMousePosition()).thenReturn(new Point(10,10));
        Assertions.assertThrows(AssertionError.class, () -> sut.moveMouseToLocation(-100,100));
        Assertions.assertThrows(AssertionError.class, () -> sut.moveMouseToLocation(100,-100));
        Assertions.assertThrows(AssertionError.class, () -> sut.moveMouseToLocation(1001,0));
        Assertions.assertThrows(AssertionError.class, () -> sut.moveMouseToLocation(0,1001));
      }
}
