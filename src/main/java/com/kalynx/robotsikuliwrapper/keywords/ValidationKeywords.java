package com.kalynx.robotsikuliwrapper.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
@RobotKeywords
public class ValidationKeywords {
    @RobotKeyword("""
            Click
            """)
    @ArgumentNames({"image"})
    public boolean click(String image) {
        return false;
    }

    @RobotKeyword("""
            Double Click
            """)
    @ArgumentNames({"image"})
    public boolean doubleClick(String image) {
        return false;
    }

    @RobotKeyword("""
            Right Click
            """)
    @ArgumentNames({"image"})
    public boolean rightClick(String image) {
        return false;
    }
}
