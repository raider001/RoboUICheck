package com.kalynx.snagtest.control;

import com.kalynx.snagtest.MouseEvent.MouseButtonDown;
import com.kalynx.snagtest.wrappers.MouseInfoControl;
import com.kalynx.snagtest.wrappers.RobotControl;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MouseController {

    private RobotControl robot;
    private MouseInfoControl mouseInfo;
    private final List<Rectangle> displays;
    public MouseController(RobotControl robot, MouseInfoControl mouseInfo, List<Rectangle> displays) {
        this.robot = Objects.requireNonNull(robot);
        this.mouseInfo = Objects.requireNonNull(mouseInfo);
        this.displays = Objects.requireNonNull(displays);
    }
    public void moveMouseToLocation(int display, int x, int y) {
        Rectangle r = displays.get(display);
        if(x < 0 || x > r.width) throw new AssertionError("Mouse location x: " + x + " must be equal to or between " + 0 + "-" + r.width);
        if(y < 0 || y > r.height) throw new AssertionError("Mouse location y: " + y + " must be equal to or between " + 0 + "-" + r.height);

        robot.mouseMove(x + r.x, y + r.y);
    }

    public void moveMouseToLocation(int x, int y) {
        Point mousePos = mouseInfo.getMousePosition();
        Optional<Rectangle> currDisplay = displays.stream().filter(display -> mousePos.x >= display.x &&
                                 mousePos.x <= display.x + display.width &&
                                 mousePos.y >= display.y &&
                                 mousePos.y <= display.y + display.height).findFirst();

        if(currDisplay.isEmpty()) throw new AssertionError("Can't find mouse on screen! Please report this issue and provide your configuration setup to help stop this from happening again!");

        if(x < 0 || x > currDisplay.get().width || y < 0 || y > currDisplay.get().height)
            throw new AssertionError("x %s and y %s out of bounds for current display width %s and height %s".formatted(x,y, currDisplay.get().width, currDisplay.get().height));

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
