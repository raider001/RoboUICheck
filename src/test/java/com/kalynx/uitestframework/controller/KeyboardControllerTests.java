package com.kalynx.uitestframework.controller;

import com.kalynx.uitestframework.data.KeyboardSpecialKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.Robot;

class KeyboardControllerTests {
    private static KeyboardController sut;
    Robot r = Mockito.mock(Robot.class);

    @BeforeEach
    public void beforeEach() {
        r = Mockito.mock(Robot.class);
        RobotControl control = new RobotWrapper(r);
        sut = new KeyboardController(control);
    }

    @Test
    void keyPress_validation() throws InterruptedException {
        sut.keyPress(KeyboardSpecialKeys.A, KeyboardSpecialKeys.ALT);
        Mockito.verify(r, Mockito.times(1)).keyPress(KeyboardSpecialKeys.A.id);
        Mockito.verify(r, Mockito.times(1)).keyPress(KeyboardSpecialKeys.ALT.id);
    }

    @Test
    void keyRelease_validation() throws InterruptedException {
        sut.keyRelease(KeyboardSpecialKeys.A, KeyboardSpecialKeys.ALT);
        Mockito.verify(r, Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A.id);
        Mockito.verify(r, Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ALT.id);
    }

    @Test
    void keyClick_validation() throws InterruptedException {
        sut.keyClick(KeyboardSpecialKeys.A, KeyboardSpecialKeys.ALT);
        Mockito.verify(r, Mockito.times(1)).keyPress(KeyboardSpecialKeys.A.id);
        Mockito.verify(r, Mockito.times(1)).keyPress(KeyboardSpecialKeys.ALT.id);
        Mockito.verify(r, Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A.id);
        Mockito.verify(r, Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ALT.id);
    }

    @Test
    void type_validation() throws InterruptedException {
        sut.type("Hi world");
        verifyKeyIdPresses(16, 72, 73, 32, 87, 79, 82, 76, 68);

    }

    private void verifyKeyIdPresses(int... keys) {
        for (int k : keys) {
            Mockito.verify(r, Mockito.times(1)).keyPress(k);
            Mockito.verify(r, Mockito.times(1)).keyRelease(k);
        }
    }
}
