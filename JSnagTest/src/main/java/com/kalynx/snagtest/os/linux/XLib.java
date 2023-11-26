package com.kalynx.snagtest.os.linux;

import com.sun.jna.Structure;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.X11;
import com.sun.jna.ptr.PointerByReference;

public interface XLib extends Library {
    XLib INSTANCE = Native.load("X11", XLib.class);

    void XMoveWindow(Pointer display, int w, int x, int y);

    // Define XMapWindow function
    void XMapWindow(Pointer display, int w);

    // Define XFlush function
    void XFlush(Pointer display);

    // Define XCloseDisplay function
    void XCloseDisplay(Pointer display);

    // Define XSetWindowAttributes structure
    @Structure.FieldOrder({"override_redirect"})
    public static class XSetWindowAttributes extends Structure {
        public int override_redirect;

        public XSetWindowAttributes() {
            super();
        }

        public XSetWindowAttributes(Pointer p) {
            super(p);
        }
    }

    // Define XGetWindowAttributes function
    int XGetWindowAttributes(Pointer display, int w, XSetWindowAttributes attributes);

    // Define XChangeWindowAttributes function
    int XChangeWindowAttributes(Pointer display, int w, long valuemask, XSetWindowAttributes attributes);
}