package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.control.MainController;
import com.kalynx.snagtest.data.KeyboardSpecialKeys;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.util.Collections;
import java.util.List;

@RobotKeywords
public class KeyboardKeywords {

    @RobotKeyword("""
            Types the given message
            """)
    @ArgumentNames({"message"})
    public void type(String message) throws Exception {
        MainController.getInstance().getKeyboardController().type(message);
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
        MainController.getInstance().getKeyboardController().keyPress(specialKey);
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
        KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key);
        MainController.getInstance().getKeyboardController().keyRelease(specialKey);
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
    public void pressKeys(List<Object> keys) throws Exception {
        List<String> trans = keys.stream().map(Object::toString).toList();
        for (String key : trans) {
            KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key);
            MainController.getInstance().getKeyboardController().keyPress(specialKey);
        }

        for (String key : trans.stream().sorted(Collections.reverseOrder()).toList()) {
            KeyboardSpecialKeys specialKey = KeyboardSpecialKeys.valueOf(key);
            MainController.getInstance().getKeyboardController().keyRelease(specialKey);
        }
    }
}
