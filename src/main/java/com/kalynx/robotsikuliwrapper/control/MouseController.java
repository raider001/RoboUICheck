package com.kalynx.robotsikuliwrapper.control;

import com.kalynx.robotsikuliwrapper.MouseEvent.MouseButtonDown;

import java.awt.*;
import java.awt.event.InputEvent;

public class MouseController {

    private Robot robot;
    public MouseController(Robot robot) {
        this.robot = robot;
    }
    public void moveMouseToLocation(int x, int y) {
        robot.mouseMove(x, y);
    }

    public void moveMouseRelative(int xOffset, int yOffset) {
        Point p = MouseInfo.getPointerInfo().getLocation();
        robot.mouseMove(p.x + xOffset, p.y + yOffset);
    }

    public void mousePress(MouseButtonDown button) {
        robot.mousePress(button.getButton());
    }

    public void mouseRelease(MouseButtonDown button) {
        robot.mouseRelease(button.getButton());
    }

    public void mouseClick(MouseButtonDown button, int totalClicks) {
        for(int i = 0; i < totalClicks; i++) {
            mousePress(button);
            mouseRelease(button);
        }
    }

    public void mouseScroll(int scrollAmount) {
        robot.mouseWheel(scrollAmount);
    }
}
