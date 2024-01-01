package com.kalynx.uitestframework.os.linux;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface XLib extends Library {
    XLib INSTANCE = Native.load("X11", XLib.class);

    void XMoveWindow(Pointer display, int w, int x, int y);

    void XMapWindow(Pointer display, int w);

    void XRaiseWindow(Pointer display, int window);

    void XFlush(Pointer display);

    int XGetWindowAttributes(Pointer display, int w, XSetWindowAttributes attributes);

    // Define XChangeWindowAttributes function
    int XChangeWindowAttributes(Pointer display, int w, long valuemask, XSetWindowAttributes attributes);

    void XResizeWindow(Pointer display, int window, int x, int y);

    // Define XSetWindowAttributes structure
    @Structure.FieldOrder({"override_redirect"})
    class XSetWindowAttributes extends Structure {
        public int override_redirect;

        public XSetWindowAttributes() {
            super();
        }

        public XSetWindowAttributes(Pointer p) {
            super(p);
        }
    }
}