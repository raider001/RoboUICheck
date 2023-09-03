package com.kalynx.snagtest.wrappers;

import java.awt.*;

public class MouseInfoWrapper implements MouseInfoControl {
    public Point getMousePosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
