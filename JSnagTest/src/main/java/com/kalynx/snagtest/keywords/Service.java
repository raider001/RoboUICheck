package com.kalynx.snagtest.keywords;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class Service {
    @RobotKeyword("""
            Hello
            Provides a simple Utility to check the system is actively responding.
            """)
    public String ping() {
        return "Hello";
    }
}
