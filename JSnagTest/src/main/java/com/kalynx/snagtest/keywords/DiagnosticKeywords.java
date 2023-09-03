package com.kalynx.snagtest.keywords;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class DiagnosticKeywords {
    @RobotKeyword("""
            Ping
            """)
    public String ping() {
        return "Hello";
    }

}
