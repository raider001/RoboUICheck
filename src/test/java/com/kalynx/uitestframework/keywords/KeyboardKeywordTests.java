package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.KeyboardController;
import com.kalynx.uitestframework.data.KeyboardSpecialKeys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KeyboardKeywordTests {

    private KeyboardKeywords sut;
    private KeyboardController controller;
    @BeforeEach
    public void beforeEach() throws AlreadyAddedException {
        controller = Mockito.mock(KeyboardController.class);
        DI.getInstance().add(controller);
        sut = new KeyboardKeywords();
    }

    @Test
    public void type_verification() throws Exception {
        sut.type("Hello World");
        Mockito.verify(controller, Mockito.times(1)).type("Hello World");
    }

    @Test
    public void holdKey_verification() throws Exception {
        sut.holdKey("ENTER");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.holdKey("blarg"));
        Assertions.assertEquals("Key blarg is not a valid key", t.getMessage());
    }

    @Test
    public void releaseKey_verification() throws Exception {

    }
}
