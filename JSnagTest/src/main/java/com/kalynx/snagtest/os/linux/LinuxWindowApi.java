package com.kalynx.snagtest.os.linux;

import com.kalynx.snagtest.os.Window;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.sun.jna.platform.unix.X11;
public class LinuxWindowApi implements Window {
    private static final X11 x11 = X11.INSTANCE;
    private static final XLib X_LIB = XLib.INSTANCE;
    @Override
    public List<String> getAllWindows() {
        Map<String, Long> windows = new HashMap<>();
        X11.Display display = x11.XOpenDisplay(null);
        X11.Window root = x11.XDefaultRootWindow(display);
        rescurseWindows(windows, display, root);
        x11.XCloseDisplay(display);
        return new ArrayList<>(windows.keySet());
    }

    @Override
    public Rectangle getWindowDimensions(String windowName) {
        return null;
    }

    @Override
    public boolean setWindowPosition(String windowName, int x, int y) {
        Map<String, Long> windows = new HashMap<>();
        X11.Display display = x11.XOpenDisplay(null);
        X11.Window root = x11.XDefaultRootWindow(display);
        rescurseWindows(windows,display,root);
        Optional<String> fullName = windows.keySet().stream().filter(key -> key.contains(windowName)).findFirst();
        if(fullName.isEmpty()) return false;
        Long selectedWindow = windows.get(fullName.get());

        if(selectedWindow != null) {
            XLib.XSetWindowAttributes attributes = new XLib.XSetWindowAttributes();
            attributes.override_redirect = 1;
            X_LIB.XChangeWindowAttributes(display.getPointer(), selectedWindow.intValue(), X11.CWOverrideRedirect, attributes);
            X_LIB.XMoveWindow(display.getPointer(), selectedWindow.intValue(), x, y);
            X_LIB.INSTANCE.XMapWindow(display.getPointer(), selectedWindow.intValue());

            // Flush the display to ensure changes take effect
            X_LIB.INSTANCE.XFlush(display.getPointer());
        }
        return true;
    }

    @Override
    public boolean setWindowPosition(String windowName, String displayReference, int x, int y) {
        return false;
    }

    @Override
    public boolean setWindowSize(String windowName, int width, int height) {
        return false;
    }

    @Override
    public boolean bringToFront(String windowName) {
        return false;
    }

    private List<String> getWindowName(X11.Display display, X11.Window window) {
        return null;
    }

    private void findWindowByName(String windowName) {

    }
    private void rescurseWindows(Map<String, Long> names, X11.Display display, X11.Window root) {
        X11.WindowByReference windowRef = new X11.WindowByReference();
        X11.WindowByReference parentRef = new X11.WindowByReference();
        PointerByReference childrenRef = new PointerByReference();
        IntByReference childCountRef = new IntByReference();

        x11.XQueryTree(display, root, windowRef, parentRef,childrenRef,childCountRef);

        if(childrenRef.getValue() == null) {
            return;
        }

        long[] ids;

        if(Native.LONG_SIZE == Long.BYTES) {
            ids = childrenRef.getValue().getLongArray(0, childCountRef.getValue());
        } else if (Native.LONG_SIZE == Integer.BYTES) {
            int[] intIds = childrenRef.getValue().getIntArray(0, childCountRef.getValue());
            ids = new long[intIds.length];
            for(int i = 0; i < intIds.length; i++) {
                ids[i] = intIds[i];
            }
        } else {
            throw new IllegalStateException("Unexpected size for Native.LONG_SIZE" + Native.LONG_SIZE);
        }

        for (long id : ids) {
            if(id == 0) {
                continue;
            }
            X11.Window window = new X11.Window(id);

            X11.XTextProperty name = new X11.XTextProperty();
            x11.XGetWMName(display, window, name);

            if (name.value != null && !name.value.trim().isEmpty()) {
                names.put(name.value, id);
            }

            x11.XFree(name.getPointer());
            rescurseWindows(names, display, window);
        }
    }
}
