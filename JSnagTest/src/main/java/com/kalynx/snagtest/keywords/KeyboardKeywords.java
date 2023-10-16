package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.SnagTest;
import com.kalynx.snagtest.control.KeyboardController;
import com.kalynx.snagtest.data.KeyboardSpecialKeys;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class KeyboardKeywords {

    private static final KeyboardController KEYBOARD_CONTROLLER = SnagTest.DI.getDependency(KeyboardController.class);

    @RobotKeyword("""
            Types the given message
            """)
    @ArgumentNames({"message"})
    public void type(String message) throws Exception {
        KEYBOARD_CONTROLLER.type(message);
    }

    @RobotKeyword("""
            Holds the requested key. It wont be released until the release key has been called
                        
            Available Options:
             - ENTER
             - BACK_SPACE
             - TAB
             - CANCEL
             - CLEAR
             - SHIFT
             - CONTROL
             - ALT
             - PAUSE
             - CAPSLOCK
             - ESCAPE
             - SPACE
             - PAGE_UP
             - PAGE_DOWN
             - END
             - HOME
             - LEFT
             - UP
             - RIGHT
             - DOWN
             - DELETE
             - F1
             - F2
             - F3
             - F4
             - F5
             - F6
             - F7
             - F8
             - F9
             - F10
             - F11
             - F12
             - F13
             - F14
             - F15
             - F16
             - F17
             - F18
             - F19
             - F20
             - F21
             - F22
             - F23
             - F24
             - PRINT_SCREEN
             - INSERT
             - HELP
             - META
             - A
             - B
             - C
             - D
             - E
             - F
             - G
             - H
             - I
             - J
             - K
             - L
             - M
             - N
             - O
             - P
             - Q
             - R
             - S
             - T
             - U
             - V
             - W
             - X
             - Y
             - Z
            """)
    @ArgumentNames({"key"})
    public void holdKey(String key) throws Exception {
        KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key);
        KEYBOARD_CONTROLLER.keyPress(specialKey);
    }

    @RobotKeyword("""
            Releases the requested key
                        
            Available Options:
             - ENTER
             - BACK_SPACE
             - TAB
             - CANCEL
             - CLEAR
             - SHIFT
             - CONTROL
             - ALT
             - PAUSE
             - CAPSLOCK
             - ESCAPE
             - SPACE
             - PAGE_UP
             - PAGE_DOWN
             - END
             - HOME
             - LEFT
             - UP
             - RIGHT
             - DOWN
             - DELETE
             - F1
             - F2
             - F3
             - F4
             - F5
             - F6
             - F7
             - F8
             - F9
             - F10
             - F11
             - F12
             - F13
             - F14
             - F15
             - F16
             - F17
             - F18
             - F19
             - F20
             - F21
             - F22
             - F23
             - F24
             - PRINT_SCREEN
             - INSERT
             - HELP
             - META
             - A
             - B
             - C
             - D
             - E
             - F
             - G
             - H
             - I
             - J
             - K
             - L
             - M
             - N
             - O
             - P
             - Q
             - R
             - S
             - T
             - U
             - V
             - W
             - X
             - Y
             - Z
            """)
    @ArgumentNames({"key"})
    public void releaseKey(String key) throws Exception {
        if (key != null && !key.isEmpty()) {
            try {
                KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key);
                KEYBOARD_CONTROLLER.keyRelease(specialKey);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Key %s is not a valid key".formatted(key));
            }
        }
    }

    @RobotKeyword("""
            presses a set of keys, then releases them immediately afterwords.
                        
            Available Options:
             - ENTER
             - BACK_SPACE
             - TAB
             - CANCEL
             - CLEAR
             - SHIFT
             - CONTROL
             - ALT
             - PAUSE
             - CAPSLOCK
             - ESCAPE
             - SPACE
             - PAGE_UP
             - PAGE_DOWN
             - END
             - HOME
             - LEFT
             - UP
             - RIGHT
             - DOWN
             - DELETE
             - F1
             - F2
             - F3
             - F4
             - F5
             - F6
             - F7
             - F8
             - F9
             - F10
             - F11
             - F12
             - F13
             - F14
             - F15
             - F16
             - F17
             - F18
             - F19
             - F20
             - F21
             - F22
             - F23
             - F24
             - PRINT_SCREEN
             - INSERT
             - HELP
             - META
             - A
             - B
             - C
             - D
             - E
             - F
             - G
             - H
             - I
             - J
             - K
             - L
             - M
             - N
             - O
             - P
             - Q
             - R
             - S
             - T
             - U
             - V
             - W
             - X
             - Y
             - Z
            """)
    @ArgumentNames({"key1", "key2=None", "key3=None", "key4=None", "key5=None"})
    public void pressKeys(String key1, String key2, String key3, String key4, String key5) throws Exception {
        pressKey(key1);
        pressKey(key2);
        pressKey(key3);
        pressKey(key4);
        pressKey(key5);
        releaseKey(key1);
        releaseKey(key2);
        releaseKey(key3);
        releaseKey(key4);
        releaseKey(key5);
    }

    @RobotKeywordOverload
    public void pressKeys(String key1, String key2, String key3, String key4) throws Exception {
        pressKeys(key1, key2, key3, key4, null);
    }

    @RobotKeywordOverload
    public void pressKeys(String key1, String key2, String key3) throws Exception {
        pressKeys(key1, key2, key3, null, null);
    }

    @RobotKeywordOverload
    public void pressKeys(String key1, String key2) throws Exception {
        pressKeys(key1, key2, null, null, null);
    }

    @RobotKeywordOverload
    public void pressKeys(String key1) throws Exception {
        pressKeys(key1, null, null, null, null);
    }

    private void rKey(String key) throws InterruptedException {
        if (key != null && !key.isEmpty()) {
            try {
                KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key.toUpperCase());
                KEYBOARD_CONTROLLER.keyRelease(specialKey);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Key %s is not a valid key".formatted(key));
            }
        }
    }

    private void pressKey(String key) throws InterruptedException {
        if (key != null && !key.isEmpty()) {
            try {
                KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key.toUpperCase());
                KEYBOARD_CONTROLLER.keyPress(specialKey);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Key %s is not a valid key".formatted(key));
            }
        }
    }
}
