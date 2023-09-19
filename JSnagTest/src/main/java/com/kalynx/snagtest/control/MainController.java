package com.kalynx.snagtest.control;

import com.kalynx.snagtest.wrappers.MouseInfoWrapper;
import com.kalynx.snagtest.wrappers.RobotWrapper;
import com.kalynx.snagtest.screen.CvMonitor;
import com.kalynx.snagtest.settings.TimeSettings;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private Robot robot;
    private MouseController mouseController;
    private KeyboardController keyboardController;
    private TimeSettings timeSettings = new TimeSettings();
    private CvMonitor cvMonitor;

    private MainController() {
        try {
            robot = new Robot();
            RobotWrapper robotWrapper = new RobotWrapper(robot);

            List<Rectangle> displays = new ArrayList<>();
            GraphicsDevice[] d = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
            for (GraphicsDevice graphicsDevice : d) {
                Rectangle rectangle = graphicsDevice.getConfigurations()[0].getBounds();
                displays.add(rectangle);
            }

            mouseController = new MouseController(robotWrapper, new MouseInfoWrapper(),displays);
            keyboardController = new KeyboardController(timeSettings, this.robot);
            cvMonitor = new CvMonitor(0.95, d);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static MainController INSTANCE;
    public static synchronized MainController getInstance() {
        if(INSTANCE  == null) {
            INSTANCE = new MainController();
        }
        return INSTANCE;
    }

    public TimeSettings getTimeSettings() {
        return timeSettings;
    }

    public MouseController getMouseController() {
        return mouseController;
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    public CvMonitor getCvMonitor() {
        return cvMonitor;
    }
}
