package com.kalynx.uitestframework.exceptions;

public class DisplayNotFoundException extends Exception {
    public DisplayNotFoundException(String reference) {
        super("Display " + reference + " not found.");
    }
}
