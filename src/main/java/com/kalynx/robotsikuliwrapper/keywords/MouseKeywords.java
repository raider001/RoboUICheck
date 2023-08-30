package com.kalynx.robotsikuliwrapper.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
@RobotKeywords
public class MouseKeywords {
    @RobotKeyword("""
            Move Mouse
            """)
    @ArgumentNames({"xRelative", "yRelative"})
    public boolean moveMouse(int xRelative, int yRelative) {
        return false;
    }

    @RobotKeyword("""
            Move Mouse To
            """)
    @ArgumentNames({"x", "y"})
    public boolean moveMouseTo(int x, int y) {
        return false;
    }

    @RobotKeyword("""
            Drag And Drop
            """)
    @ArgumentNames({"x", "y"})
    public boolean dragAndDrop(String image, String toImage) {
        return false;
    }
}
