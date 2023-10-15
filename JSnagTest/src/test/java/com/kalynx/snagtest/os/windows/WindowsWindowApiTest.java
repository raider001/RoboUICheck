package com.kalynx.snagtest.os.windows;

import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.util.List;

public class WindowsWindowApiTest {

    @Test
    public void testGetAllWindows() {
        WindowsWindowApi windowsWindowApi = new WindowsWindowApi();
        List<String> windows = windowsWindowApi.getAllWindows();
        windowsWindowApi.getAllWindows();
    }

    @Test
    public void testGetWindowDimensions() {
        WindowsWindowApi windowsWindowApi = new WindowsWindowApi();
        Rectangle rect = windowsWindowApi.getWindowDimensions("FAF Client");
        windowsWindowApi.getAllWindows();
    }
}
