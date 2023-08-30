package com.kalynx.robotsikuliwrapper.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class CaptureKeywords {
    @RobotKeyword("""
            Screenshot
            """)
    @ArgumentNames({"x", "y", "width", "height"})
    public boolean screenshot(int x, int y, int width, int height) {
        return false;
    }
}
