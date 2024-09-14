package com.kalynx.uitestframework.os.linux;

import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import com.kalynx.uitestframework.os.Window;
import com.sun.jna.Native;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

import java.awt.Rectangle;
import java.util.*;

public class LinuxWindowApi implements Window {
    private static final X11 x11 = X11.INSTANCE;
    private static final XLib X_LIB = XLib.INSTANCE;
    private final DisplayManager displayManager;

    public LinuxWindowApi(DisplayManager displayManager) {
        this.displayManager = displayManager
    }

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
        X11.Display display = x11.XOpenDisplay(null);
        int selectedWindow = getWindowId(display, windowName);
        X11.Window window = new X11.Window(selectedWindow);

        if(selectedWindow != -1) {
            X11.XWindowAttributes attributes = new X11.XWindowAttributes();
            x11.XGetWindowAttributes(display, window,attributes);
            return new Rectangle(attributes.x, attributes.y, attributes.width, attributes.height);
        }
        return null;
    }

    @Override
    public boolean setWindowPosition(String windowName, int x, int y) {

        X11.Display display = x11.XOpenDisplay(null);
        int selectedWindow = getWindowId(display, windowName);

        if(selectedWindow != -1) {
            XLib.XSetWindowAttributes attributes = new XLib.XSetWindowAttributes();
            attributes.override_redirect = 1;
            X_LIB.XChangeWindowAttributes(display.getPointer(), selectedWindow, X11.CWOverrideRedirect, attributes);
            X_LIB.XMoveWindow(display.getPointer(), selectedWindow, x, y);
            X_LIB.INSTANCE.XMapWindow(display.getPointer(), selectedWindow);
            X_LIB.INSTANCE.XFlush(display.getPointer());
        }
        return true;
    }

    @Override
    public boolean setWindowPosition(String windowName, String displayReference, int x, int y) {
        try {
            int dispX = displayManager.getDisplay(displayReference).x();
            int dispY = displayManager.getDisplay(displayReference).y();
            return setWindowPosition(windowName, x + dispX, y + dispY);
        } catch (DisplayNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean setWindowSize(String windowName, int width, int height) {
        X11.Display display = x11.XOpenDisplay(null);
        int selectedWindow = getWindowId(display, windowName);
        if(selectedWindow != -1) {
            XLib.XSetWindowAttributes attributes = new XLib.XSetWindowAttributes();
            attributes.override_redirect = 1;
            X_LIB.XChangeWindowAttributes(display.getPointer(), selectedWindow, X11.CWOverrideRedirect, attributes);
            X_LIB.XResizeWindow(display.getPointer(), selectedWindow, width, height);
            X_LIB.INSTANCE.XMapWindow(display.getPointer(), selectedWindow);
            X_LIB.INSTANCE.XFlush(display.getPointer());
        }
        return true;
    }

    @Override
    public boolean bringToFront(String windowName) {
        X11.Display display = x11.XOpenDisplay(null);
        int selectedWindow = getWindowId(display, windowName);
        if(selectedWindow != -1) {
            XLib.XSetWindowAttributes attributes = new XLib.XSetWindowAttributes();
            attributes.override_redirect = 1;
            X_LIB.XChangeWindowAttributes(display.getPointer(), selectedWindow, X11.CWOverrideRedirect, attributes);
            X_LIB.XRaiseWindow(display.getPointer(), selectedWindow);
            X_LIB.INSTANCE.XMapWindow(display.getPointer(), selectedWindow);
            X_LIB.INSTANCE.XFlush(display.getPointer());
        }
        return true;
    }

    private int getWindowId(X11.Display display, String name) {
        Map<String, Long> windows = new HashMap<>();
        X11.Window root = x11.XDefaultRootWindow(display);
        rescurseWindows(windows,display,root);
        Optional<String> fullName = windows.keySet().stream().filter(key -> key.contains(name)).findFirst();
        return fullName.map(s -> windows.get(s).intValue()).orElse(-1);
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
