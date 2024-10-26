package com.kalynx.uitestframework.controller;

import java.awt.*;

public class RobotWrapper implements RobotControl {

    private final Robot robot;

    public RobotWrapper(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void mouseMove(int x, int y) {
        robot.mouseMove(x, y);
    }

    @Override
    public void mousePress(int button) {
        robot.mousePress(button);
    }

    @Override
    public void mouseRelease(int button) {
        robot.mouseRelease(button);
    }

    @Override
    public void mouseWheel(int scrollAmount) {
        robot.mouseWheel(scrollAmount);
    }

    @Override
    public Robot getRobot() {
        return robot;
    }


}
