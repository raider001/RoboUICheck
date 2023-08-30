package com.kalynx.robotsikuliwrapper.MouseEvent;

import java.awt.event.InputEvent;

public enum MouseButtonDown {
    // TODO - verify these are correct.
    Left(InputEvent.BUTTON1_DOWN_MASK),
    Middle(InputEvent.BUTTON2_DOWN_MASK),
    Right(InputEvent.BUTTON3_DOWN_MASK);

    private int id = 0;
    private MouseButtonDown(int mask) {
        id = mask;
    }

    public int getButton() {
        return id;
    }
}
