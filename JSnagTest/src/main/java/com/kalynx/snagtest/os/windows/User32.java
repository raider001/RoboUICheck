package com.kalynx.snagtest.os.windows;

import com.sun.jna.*;

public interface User32 extends Library {
    User32 INSTANCE = Native.loadLibrary("user32", User32.class);
    int MB_ICONERROR = 0x00000010;
    int SM_CXSCREEN = 0;
    int SM_CYSCREEN = 1;

    int MessageBoxW(Pointer hWnd, String lpText, String lpCaption, int uType);

    boolean MessageBeep(int uType);

    boolean SetForegroundWindow(Pointer hWnd);

    boolean GetCursorPos(Point lpPoint);

    int GetSystemMetrics(int nIndex);

    boolean EnumWindows(WNDENUMPROC lpEnumFunc, Pointer userData);

    int GetWindowTextW(Pointer hWnd, char[] lpString, int nMaxCount);

    int GetWindowRect(Pointer hWnd, RECT lpRect);

    boolean MoveWindow(Pointer hWnd, int x, int y, int nWidth, int nHeight, boolean bRepaint);

    interface WNDENUMPROC extends Callback {
        boolean callback(Pointer hWnd, Pointer arg);
    }

    class Point extends Structure {
        public int x, y;

        protected java.util.List getFieldOrder() {
            return java.util.Arrays.asList("x", "y");
        }
    }

    class RECT extends Structure {
        public int left, top, right, bottom;

        protected java.util.List getFieldOrder() {
            return java.util.Arrays.asList("left", "top", "right", "bottom");
        }
    }
}