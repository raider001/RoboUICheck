package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.control.MainController;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;


@RobotKeywords
public class MouseKeywords {
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
    public boolean moveMouseToImage(String image) { return false;}


    @RobotKeyword("""
            Drag And Drop
            """)
    @ArgumentNames({"x", "y"})
    public boolean dragAndDrop(String image, String toImage) {
        return false;
    }

    // Mouse Movement Settings
    @RobotKeyword("Set Mouse Move Speed")
    @ArgumentNames({"period"})
    public void setMouseMoveSpeed(long speed) {
        MainController.getInstance().getMouseController().setMouseMoveSpeed(speed);
    }
}
