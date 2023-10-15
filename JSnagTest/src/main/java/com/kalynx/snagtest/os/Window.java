package com.kalynx.snagtest.os;

import java.awt.Rectangle;
import java.util.List;

public interface Window {

    List<String> getAllWindows();

    Rectangle getWindowDimensions(String windowName);

    void setWindowPosition(String windowName, int x, int y);

    void setWindowPosition(String windowName, int display, int x, int y);
}
