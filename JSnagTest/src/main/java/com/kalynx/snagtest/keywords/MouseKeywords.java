package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.MouseEvent.MouseButtonDown;
import com.kalynx.snagtest.control.MainController;
import com.kalynx.snagtest.data.Result;
import com.kalynx.snagtest.screen.ScreenshotData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;


@RobotKeywords
public class MouseKeywords {
    private static final Logger LOGGER = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @RobotKeyword("""
            Move Mouse
            Moves the mouse a relative distance based on its original location.
            """)
    @ArgumentNames({"xRelative", "yRelative"})
    public void moveMouse(int xRelative, int yRelative) {
        MainController.getInstance().getMouseController().moveMouse(xRelative,yRelative);
    }

    @RobotKeyword("""
            Move Mouse To Display
            """)
    @ArgumentNames({"display", "x", "y"})
    public void moveMouseToDisplay(int display, int x, int y) throws Exception {
        MainController.getInstance().getMouseController().moveMouseTo(display, x, y);
    }

    @RobotKeyword("""
            Move Mouse To
            """)
    @ArgumentNames({"x", "y"})
    public void moveMouseTo(int x, int y) throws Exception {
        MainController.getInstance().getMouseController().moveMouseTo(x,y);
    }

    @RobotKeyword("""
            Move Mouse To Image
            Moves the mouse to the center of the matched image on the screen.
            """)
    @ArgumentNames({"image"})
    public void moveMouseToImage(String image) throws Exception {
        Result<ScreenshotData> res = MainController.getInstance().getCvMonitor().monitorFor(image);
        if(res.isFailure()) throw new Exception("*HTML*" + res.getInfo());
        LOGGER.info(res.getInfo());
        Rectangle p = res.getData().foundLocation();
        MainController.getInstance().getMouseController().moveMouseTo(p.x + p.width / 2, p.y + + p.height / 2);
    }

    @RobotKeyword("""
            Simulates a Mouse click
            Available options are:
            <ul>
            <li>LEFT</li><li>MIDDLE</li><li>RIGHT</li>
            </ul>
            
            """)
    @ArgumentNames({"button"})
    public void click(String button) throws Exception {
        try{
            MouseButtonDown mask = MouseButtonDown.valueOf(button.toUpperCase());
            MainController.getInstance().getMouseController().mouseClick(mask, 1);
        } catch (Exception e) {
            throw new Exception("Invalid Click option %s given.".formatted(button));
        }

    }

    // Mouse Movement Settings
    @RobotKeyword("Set Mouse Move Speed")
    @ArgumentNames({"speed"})
    public void setMouseMoveSpeed(long speed) {
        MainController.getInstance().getMouseController().setMouseMoveSpeed(speed);
    }
}
