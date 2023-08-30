package com.kalynx.robotsikuliwrapper.control;

import java.awt.event.KeyEvent;

public class Test {

    public void driver() {
        for(int i = 0; i < 5000; i++) {
            if(!KeyEvent.getKeyText(i).startsWith("Unknown keyCode:")) {
                System.out.println(i + ": " + KeyEvent.getKeyText(i) + " " + ((char)i));
            }
        }
    }
}
