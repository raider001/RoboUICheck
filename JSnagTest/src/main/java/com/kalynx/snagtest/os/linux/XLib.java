package com.kalynx.snagtest.os.linux;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public interface XLib extends Library {
    XLib INSTANCE = Native.load("X11", XLib.class);

    int XGetWindowProperty(Pointer display, Pointer window, Pointer property, long longOffset, long longLength, boolean delete, long reqType, PointerByReference actualTypeReturn, PointerByReference actualFormatReturn, PointerByReference nItemsReturn, PointerByReference bytesAfterReturn, PointerByReference propReturn);

    Pointer XOpenDisplay(String display_name);

    Pointer XDefaultRootWindow(Pointer display);

    int XQueryTree(Pointer display, Pointer w, PointerByReference root_return, PointerByReference parent_return, PointerByReference children_return, PointerByReference nchildren_return);
}