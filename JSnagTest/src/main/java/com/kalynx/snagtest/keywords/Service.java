package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.SnagTest;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class Service {
    @RobotKeyword("""
            Ping
            """)
    public String ping() {
        return "Hello";
    }

    @RobotKeyword("""
            Shutdown
            Shuts down JSnagTest
            """)
    public void shutdown() throws Exception {
        SnagTest.shutdown();
    }

}
