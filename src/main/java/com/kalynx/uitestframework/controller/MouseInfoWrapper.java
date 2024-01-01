package com.kalynx.uitestframework.controller;

import java.awt.*;

public class MouseInfoWrapper implements MouseInfoControl {
    public Point getMousePosition() {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
