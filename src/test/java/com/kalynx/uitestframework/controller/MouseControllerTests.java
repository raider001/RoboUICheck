package com.kalynx.uitestframework.controller;

import com.kalynx.uitestframework.MouseEvent.MouseButtonDown;
import com.kalynx.uitestframework.data.RelativeEnum;
import com.kalynx.uitestframework.exceptions.MouseException;
import com.kalynx.uitestframework.testdata.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.function.Consumer;

class MouseControllerTests {
    private static MouseController sut;
    Robot r = Mockito.mock(Robot.class);
    MouseInfoControl mic;
    DisplayManager dm = TestData.createDisplayManager();
    Consumer<Point> cons;
    @BeforeEach
    void beforeEach() {
        r = Mockito.mock(Robot.class);
        mic = Mockito.mock(MouseInfoControl.class);
        RobotControl control = new RobotWrapper(r);
        cons = Mockito.mock(Consumer.class);
        sut = new MouseController(control, mic,dm,cons);
    }

    @Test
    void mousePress_validate() {
        sut.mousePress(MouseButtonDown.LEFT);
        Mockito.verify(r,Mockito.times(1)).mousePress(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Test
    void mouseRelease_validate() {
        sut.mouseRelease(MouseButtonDown.LEFT);
        Mockito.verify(r,Mockito.times(1)).mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Test
    void mouseClick_validate() throws InterruptedException {
        sut.mouseClick(MouseButtonDown.LEFT, 2);
        Mockito.verify(r,Mockito.times(2)).mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Mockito.verify(r,Mockito.times(2)).mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    @Test
    void mouseScroll_validation() {
        sut.mouseScroll(2);
        Mockito.verify(r,Mockito.times(1)).mouseWheel(2);
    }

    @Test
    void setMouseMoveSpeed_validation() throws MouseException {
        sut.setMouseMoveSpeed(10);
        Assertions.assertEquals(10, sut.getMouseMoveSpeed());
    }

    @Test
    void moveMouse_movesRelativeToOriginal() {
        Mockito.when(mic.getMousePosition()).thenReturn(new Point(100,100));
        sut.moveMouse(100,100);
        Mockito.verify(cons,Mockito.times(1)).accept(new Point(200,200));
    }

    @Test
    void moveMouse_moveMouseToDisplay() throws Exception {
        dm.setPrimaryReference("PRIMARY");
        dm.setReference("SECONDARY").relative(RelativeEnum.RIGHT).of("PRIMARY");
        Mockito.when(mic.getMousePosition()).thenReturn(new Point(100,100));
        sut.moveMouseTo("SECONDARY",100,100);
        Mockito.verify(cons,Mockito.times(1)).accept(new Point(2020,100));
    }

    @Test
    void moveMouse_moveMouseCurrentDisplay() throws Exception {
        Mockito.when(mic.getMousePosition()).thenReturn(new Point(100,100));
        sut.moveMouseTo(100,100);
        Mockito.verify(cons,Mockito.times(1)).accept(new Point(100,100));
    }

}
