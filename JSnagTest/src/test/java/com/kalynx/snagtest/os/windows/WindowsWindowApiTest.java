package com.kalynx.snagtest.os.windows;

import com.kalynx.snagtest.manager.DisplayManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.util.List;

public class WindowsWindowApiTest {
    // These tests require Notepad open
    private static WindowsWindowApi sut;
    private static DisplayManager displayManager;

    @BeforeAll
    public static void setup() throws AWTException {
        displayManager = new DisplayManager();
        displayManager.setPrimaryReference("MAIN");
        sut = new WindowsWindowApi(displayManager);

    }

    @Test
    public void testGetAllWindows() {
        List<String> windows = sut.getAllWindows().stream().sorted().toList();
        Assertions.assertTrue(windows.stream().anyMatch(item -> item.contains("Notepad")));
    }

    @Test
    public void testMoveWindowFromOrigin() {
        Rectangle rect = sut.getWindowDimensions("Notepad");
        int relativeMoveX = 50;
        int relativeMoveY = 50;
        sut.setWindowPosition("Notepad", relativeMoveX, relativeMoveY);
        Rectangle newRect = sut.getWindowDimensions("Notepad");
        Assertions.assertEquals(rect.x + relativeMoveX, newRect.x);
        Assertions.assertEquals(rect.y + relativeMoveY, newRect.y);
        Assertions.assertEquals(rect.width + relativeMoveX, newRect.width);
        Assertions.assertEquals(rect.height + relativeMoveY, newRect.height);
        rect = newRect;

        relativeMoveX = -50;
        relativeMoveY = -50;
        sut.setWindowPosition("Notepad", relativeMoveX, relativeMoveY);
        newRect = sut.getWindowDimensions("Notepad");
        Assertions.assertEquals(rect.x + relativeMoveX, newRect.x);
        Assertions.assertEquals(rect.y + relativeMoveY, newRect.y);
        Assertions.assertEquals(rect.width + relativeMoveX, newRect.width);
        Assertions.assertEquals(rect.height + relativeMoveY, newRect.height);
    }

    @Test
    public void testWindowResize() {
        Rectangle rect = sut.getWindowDimensions("Notepad");
        int newWidth = 500;
        int newHeight = 500;
        sut.setWindowSize("Notepad", newWidth, newHeight);
        Rectangle newRect = sut.getWindowDimensions("Notepad");
        Assertions.assertEquals(rect.x, newRect.x);
        Assertions.assertEquals(rect.y, newRect.y);
        Assertions.assertEquals(newWidth + rect.x, newRect.width);
        Assertions.assertEquals(newHeight + rect.y, newRect.height);
        rect = newRect;
        newWidth = 100;
        newHeight = 100;
        sut.setWindowSize("Notepad", newWidth, newHeight);
        newRect = sut.getWindowDimensions("Notepad");
        Assertions.assertEquals(rect.x, newRect.x);
        Assertions.assertEquals(rect.y, newRect.y);
        Assertions.assertEquals(newWidth + rect.x, newRect.width);
        Assertions.assertEquals(newHeight + rect.y, newRect.height);
    }

    @Test
    public void testMoveWindowRelativeToDisplay() {
        Rectangle rect = sut.getWindowDimensions("Notepad");
        int plannedXPos = 200;
        int plannedYPos = 200;
        sut.setWindowPosition("Notepad", "MAIN", plannedXPos, plannedYPos);

        int x = displayManager.getDisplay("MAIN").x();
        int y = displayManager.getDisplay("MAIN").y();
        Rectangle newRect = sut.getWindowDimensions("Notepad");
        Assertions.assertEquals(plannedXPos + x, newRect.x);
        Assertions.assertEquals(plannedYPos + y, newRect.y);
        Assertions.assertEquals(rect.width, newRect.width);
        Assertions.assertEquals(rect.height, newRect.height);
    }

    @Test
    public void testBringToFront() {
        sut.bringToFront("Notepad");
    }
}
