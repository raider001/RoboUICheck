package com.kalynx.uitestframework.controller;
import com.kalynx.uitestframework.data.KeyboardMap;
import com.kalynx.uitestframework.data.KeyboardSpecialKeys;
import com.kalynx.uitestframework.settings.TimeSettings;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class KeyboardController {

    private final Robot robot;
    private final KeyboardMap keyboardMap = new KeyboardMap();
    private int TYPE_DELAY_MS = 20;

    public KeyboardController(TimeSettings timeSettings, RobotControl robot) {
        this.robot = robot.getRobot();
        timeSettings.addTypeDelayListener(typeDelay -> TYPE_DELAY_MS = typeDelay);
    }

    public void keyPress(KeyboardSpecialKeys... specialKeys) throws InterruptedException {
        for (KeyboardSpecialKeys specialKey : specialKeys) {
            robot.keyPress(specialKey.id);
            TimeUnit.MILLISECONDS.sleep(TYPE_DELAY_MS);
        }
    }

    public void keyRelease(KeyboardSpecialKeys... specialKeys) throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(TYPE_DELAY_MS);
        for (KeyboardSpecialKeys specialKey : specialKeys) {
            robot.keyRelease(specialKey.id);
            TimeUnit.MILLISECONDS.sleep(TYPE_DELAY_MS);
        }
    }

    public void keyClick(KeyboardSpecialKeys... modifiers) throws InterruptedException {
        keyPress(modifiers);
        keyRelease(modifiers);
    }

    public void type(String message) throws InterruptedException {

        for (int i = 0; i < message.length(); i++) {
            int[] keys = keyboardMap.getKeyboardAction(message.charAt(i));
            pressKeySet(keys);
            releaseKeySet(keys);
            TimeUnit.MILLISECONDS.sleep(TYPE_DELAY_MS);
        }
    }

    private void pressKeySet(int[] keyset) {
        for (int i = 0; i < keyset.length; i++) {
            robot.keyPress(keyset[i]);
        }
    }

    private void releaseKeySet(int[] keyset) {
        for (int i = keyset.length - 1; i >= 0; i--) {
            robot.keyRelease(keyset[i]);
        }
    }
}
