package com.kalynx.uitestframework.exceptions;

public class OcrException extends Exception{
    public OcrException(String word){
        super("'" + word + "'" + " cannot be found.");
    }
}
