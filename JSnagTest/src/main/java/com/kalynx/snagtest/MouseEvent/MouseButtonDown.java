package com.kalynx.snagtest.MouseEvent;

import java.awt.event.InputEvent;

public enum MouseButtonDown {
    // TODO - verify these are correct.
    LEFT(InputEvent.BUTTON1_DOWN_MASK),
    MIDDLE(InputEvent.BUTTON2_DOWN_MASK),
    RIGHT(InputEvent.BUTTON3_DOWN_MASK);

    private int id = 0;
    private MouseButtonDown(int mask) {
        id = mask;
    }

    public int getButton() {
        return id;
    }
}
