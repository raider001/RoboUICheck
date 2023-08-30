package com.kalynx.robotsikuliwrapper.control;

import com.kalynx.robotsikuliwrapper.settings.TimeSettings;
import org.sikuli.script.Region;
import org.sikuli.script.Screen;

import java.awt.*;

public class MainController {
    private Robot robot;
    private Screen screen = new Screen();
    private Region region = new Region(screen);
    private MouseController mouseController;
    private KeyboardController keyboardController;
    private TimeSettings timeSettings = new TimeSettings();

    private MainController() {
        try {
            robot = new Robot();
            mouseController = new MouseController(robot);
            keyboardController = new KeyboardController(timeSettings, robot);
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
}
