package com.kalynx.snagtest.jni;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class jniTest {

    @Test
    public void testJNI() {
        final List<WindowInfo> inflList = new ArrayList<>();
        final List<Integer> order = new ArrayList<>();
        int top = User32.instance.GetTopWindow(0);
        while (top != 0) {
            order.add(top);
            top = User32.instance.GetWindow(top, User32.GW_HWNDNEXT);
        }
        User32.instance.EnumWindows((hWnd, lParam) -> {
            if (User32.instance.IsWindowVisible(hWnd)) {
                RECT r = new RECT();
                User32.instance.GetWindowRect(hWnd, r);
                if (r.left > -32000) {     // If it's not minimized
                    byte[] buffer = new byte[1024];
                    User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                    String title = Native.toString(buffer);
                    inflList.add(new WindowInfo(hWnd, r, title));
                }
            }
            return true;
        }, 0);

        Collections.sort(inflList, Comparator.comparingInt(o -> order.indexOf(o.hwnd)));

        for (WindowInfo w : inflList) {
            System.out.println(w);
        }
    }

    public interface WndEnumProc extends StdCallLibrary.StdCallCallback {
        boolean callback(int hWnd, int lParam);
    }

    public interface User32 extends StdCallLibrary {
        User32 instance = Native.loadLibrary("user32", User32.class);
        int GW_HWNDNEXT = 2;

        boolean EnumWindows(WndEnumProc wndenumproc, int lParam);

        boolean IsWindowVisible(int hWnd);

        int GetWindowRect(int hWnd, RECT r);

        void GetWindowTextA(int hWnd, byte[] buffer, int buflen);

        int GetTopWindow(int hWnd);

        int GetWindow(int hWnd, int flag);
    }

    @Structure.FieldOrder({"left", "top", "right", "bottom"})
    public static class RECT extends Structure {
        public int left, top, right, bottom;
    }

    public static class WindowInfo {
        public final int hwnd;
        public final RECT rect;
        public final String title;

        public WindowInfo(int hwnd, RECT rect, String title) {
            this.hwnd = hwnd;
            this.rect = rect;
            this.title = title;
        }

        public String toString() {
            return String.format("(%d,%d)-(%d,%d) : \"%s\"",
                    rect.left, rect.top,
                    rect.right, rect.bottom,
                    title);
        }
    }
}
