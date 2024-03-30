package com.kalynx.uitestframework.os.windows;

import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import com.sun.jna.*;

import java.util.Objects;

public interface User32 extends Library {
    User32 INSTANCE = Native.load("user32", User32.class);
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
        boolean callback(Pointer hWnd, Pointer arg) throws DisplayNotFoundException;
    }

    class Point extends Structure {
        public int x, y;

        @Override
        protected java.util.List getFieldOrder() {
            return java.util.Arrays.asList("x", "y");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point point)) return false;
            if (!super.equals(o)) return false;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), x, y);
        }
    }

    class RECT extends Structure {
        public int left, top, right, bottom;

        @Override
        protected java.util.List getFieldOrder() {
            return java.util.Arrays.asList("left", "top", "right", "bottom");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RECT rect)) return false;
            if (!super.equals(o)) return false;
            return left == rect.left && top == rect.top && right == rect.right && bottom == rect.bottom;
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), left, top, right, bottom);
        }
    }
}