package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.KeyboardController;
import com.kalynx.uitestframework.data.KeyboardSpecialKeys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class KeyboardKeywordTests {

    private KeyboardKeywords sut;
    private static KeyboardController controller;

    @BeforeAll
    public static void before() throws AlreadyAddedException {
        controller = Mockito.mock(KeyboardController.class);
        DI.reset();
        DI.getInstance().add(controller);
    }
    @BeforeEach
    public void beforeEach() {
        Mockito.clearInvocations(controller);
        sut = new KeyboardKeywords();
    }

    @Test
    void type_verification() throws Exception {
        sut.type("Hello World");
        Mockito.verify(controller, Mockito.times(1)).type("Hello World");
    }

    @Test
    void holdKey_verification() throws Exception {
        sut.holdKey("ENTER");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.holdKey("blarg"));
        Assertions.assertEquals("Key blarg is not a valid key", t.getMessage());
    }

    @Test
    void releaseKey_verification() throws Exception {
        sut.releaseKey("ENTER");
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Throwable t = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.releaseKey("blarg"));
        Assertions.assertEquals("Key blarg is not a valid key", t.getMessage());
    }

    @Test
    void pressKeys5_verification() throws Exception {
        sut.pressKeys("ENTER","A","B","C","D");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.D);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.D);
    }

    @Test
    void pressKeys4_verification() throws Exception {
        sut.pressKeys("ENTER","A","B","C");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.C);

    }

    @Test
    void pressKeys3_verification() throws Exception {
        sut.pressKeys("ENTER","A","B");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.B);
    }
    @Test
    void pressKeys2_verification() throws Exception {
        sut.pressKeys("ENTER","A");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
    }

    @Test
    void pressKeys1_verification() throws Exception {
        sut.pressKeys("ENTER");
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
    }

    @Test
    void pressKeys_badKey_verification() throws InterruptedException {
        Throwable e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.pressKeys("ENTER","A","B", "C", "BAD"));
        Assertions.assertEquals("Key BAD is not a valid key", e.getMessage());
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.D);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.D);
        Mockito.clearInvocations(controller);

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.pressKeys("ENTER","A","B", "SAD", "D"));
        Assertions.assertEquals("Key SAD is not a valid key", e.getMessage());
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.D);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.D);
        Mockito.clearInvocations(controller);

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.pressKeys("ENTER","A","GLAD", "C", "D"));
        Assertions.assertEquals("Key GLAD is not a valid key", e.getMessage());
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.D);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.D);
        Mockito.clearInvocations(controller);Mockito.clearInvocations(controller);

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.pressKeys("ENTER","BLAND","B", "C", "D"));
        Assertions.assertEquals("Key BLAND is not a valid key", e.getMessage());
        Mockito.verify(controller,Mockito.times(1)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.D);
        Mockito.verify(controller,Mockito.times(1)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.D);
        Mockito.clearInvocations(controller);

        e = Assertions.assertThrows(IllegalArgumentException.class, () -> sut.pressKeys("SAND","A","B", "C", "D"));
        Assertions.assertEquals("Key SAND is not a valid key", e.getMessage());
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyPress(KeyboardSpecialKeys.D);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.ENTER);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.A);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.B);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.C);
        Mockito.verify(controller,Mockito.times(0)).keyRelease(KeyboardSpecialKeys.D);

    }
}
