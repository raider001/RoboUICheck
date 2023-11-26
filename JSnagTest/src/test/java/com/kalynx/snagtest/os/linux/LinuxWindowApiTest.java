package com.kalynx.snagtest.os.linux;

import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.os.windows.WindowsWindowApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

/**
 * Requires gedit open for testing.
 */
public class LinuxWindowApiTest {
    // These tests require Notepad open
    private static LinuxWindowApi sut;

    @BeforeAll
    public static void setup() throws AWTException {
        sut = new LinuxWindowApi();

    }

    @Test
    public void moveWindow() {
        sut.setWindowPosition("Mozilla Firefox",100,100);
    }
    @Test
    public void testGetAllWindows() {
        List<String> results = sut.getAllWindows();
        Assertions.assertTrue(results.contains("jetbrains-idea"));
    }
}
