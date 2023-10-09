package com.kalynx.snagtest.wrappers;

import java.awt.Robot;

public interface RobotControl {

    void mouseMove(int x, int y);

    void mousePress(int button);

    void mouseRelease(int button);

    void mouseWheel(int scrollAmount);

    Robot getRobot();
}
