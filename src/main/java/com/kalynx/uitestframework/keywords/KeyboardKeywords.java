package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.KeyboardController;
import com.kalynx.uitestframework.data.KeyboardSpecialKeys;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.ArrayList;
import java.util.List;

@RobotKeywords
public class KeyboardKeywords {

    private static final KeyboardController KEYBOARD_CONTROLLER = DI.getInstance().getDependency(KeyboardController.class);
    public static final String KEY_S_IS_NOT_A_VALID_KEY = "Key %s is not a valid key";

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
        if (key != null && !key.isEmpty()) {
            try {
                KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key);
                KEYBOARD_CONTROLLER.keyPress(specialKey);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(KEY_S_IS_NOT_A_VALID_KEY.formatted(key));
            }
        }
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
                throw new IllegalArgumentException(KEY_S_IS_NOT_A_VALID_KEY.formatted(key));
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
        List<String> pressedKeys = new ArrayList<>();
        testKeyPress(key1, pressedKeys);
        testKeyPress(key2, pressedKeys);
        testKeyPress(key3, pressedKeys);
        testKeyPress(key4, pressedKeys);
        testKeyPress(key5, pressedKeys);
        releaseKeys(pressedKeys);
    }

    public void testKeyPress(String key, List<String> pressedKeys) throws Exception {
        try{
            pressKey(key);
            pressedKeys.add(key);
        } catch(IllegalArgumentException err) {
            releaseKeys(pressedKeys);
            throw err;
        }
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

    private void releaseKeys(List<String> pressedKeys) throws Exception {
        for(String key: pressedKeys){
            releaseKey(key);
        }
    }

    private void pressKey(String key) throws InterruptedException {
        if (key != null && !key.isEmpty()) {
            try {
                KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key.toUpperCase());
                KEYBOARD_CONTROLLER.keyPress(specialKey);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(KEY_S_IS_NOT_A_VALID_KEY.formatted(key));
            }
        }
    }
}
