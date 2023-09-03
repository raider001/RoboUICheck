package com.kalynx.snagtest.data;

import java.util.HashMap;
import java.util.Map;

import static java.awt.event.KeyEvent.*;

public class KeyboardMap {

    Map<Character, int[]> charMap = new HashMap<>();
    {

        charMap.put('a', new int[] { VK_A });
        charMap.put('b', new int[] { VK_B });
        charMap.put('c', new int[] { VK_C });
        charMap.put('d', new int[] { VK_D });
        charMap.put('e', new int[] { VK_E });
        charMap.put('f', new int[] { VK_F });
        charMap.put('g', new int[] { VK_G });
        charMap.put('h', new int[] { VK_H });
        charMap.put('i', new int[] { VK_I });
        charMap.put('j', new int[] { VK_J });
        charMap.put('k', new int[] { VK_K });
        charMap.put('l', new int[] { VK_L });
        charMap.put('m', new int[] { VK_M });
        charMap.put('n', new int[] { VK_N });
        charMap.put('o', new int[] { VK_O });
        charMap.put('p', new int[] { VK_P });
        charMap.put('q', new int[] { VK_Q });
        charMap.put('r', new int[] { VK_R });
        charMap.put('s', new int[] { VK_S });
        charMap.put('t', new int[] { VK_T });
        charMap.put('u', new int[] { VK_U });
        charMap.put('v', new int[] { VK_V });
        charMap.put('w', new int[] { VK_W });
        charMap.put('x', new int[] { VK_X });
        charMap.put('y', new int[] { VK_Y });
        charMap.put('z', new int[] { VK_Z });
        charMap.put('A', new int[] { VK_SHIFT, VK_A });
        charMap.put('B', new int[] { VK_SHIFT, VK_B });
        charMap.put('C', new int[] { VK_SHIFT, VK_C });
        charMap.put('D', new int[] { VK_SHIFT, VK_D });
        charMap.put('E', new int[] { VK_SHIFT, VK_E });
        charMap.put('F', new int[] { VK_SHIFT, VK_F });
        charMap.put('G', new int[] { VK_SHIFT, VK_G });
        charMap.put('H', new int[] { VK_SHIFT, VK_H });
        charMap.put('I', new int[] { VK_SHIFT, VK_I });
        charMap.put('J', new int[] { VK_SHIFT, VK_J });
        charMap.put('K', new int[] { VK_SHIFT, VK_K });
        charMap.put('L', new int[] { VK_SHIFT, VK_L });
        charMap.put('M', new int[] { VK_SHIFT, VK_M });
        charMap.put('N', new int[] { VK_SHIFT, VK_N });
        charMap.put('O', new int[] { VK_SHIFT, VK_O });
        charMap.put('P', new int[] { VK_SHIFT, VK_P });
        charMap.put('Q', new int[] { VK_SHIFT, VK_Q });
        charMap.put('R', new int[] { VK_SHIFT, VK_R });
        charMap.put('S', new int[] { VK_SHIFT, VK_S });
        charMap.put('T', new int[] { VK_SHIFT, VK_T });
        charMap.put('U', new int[] { VK_SHIFT, VK_U });
        charMap.put('V', new int[] { VK_SHIFT, VK_V });
        charMap.put('W', new int[] { VK_SHIFT, VK_W });
        charMap.put('X', new int[] { VK_SHIFT, VK_X });
        charMap.put('Y', new int[] { VK_SHIFT, VK_Y });
        charMap.put('Z', new int[] { VK_SHIFT, VK_Z });
        charMap.put('`', new int[] { VK_BACK_QUOTE });
        charMap.put('~', new int[] { VK_SHIFT, VK_BACK_QUOTE });
        charMap.put('1', new int[] { VK_1 });
        charMap.put('!', new int[] { VK_SHIFT, VK_1 });
        charMap.put('2', new int[] { VK_2 });
        charMap.put('@', new int[] { VK_SHIFT, VK_2 });
        charMap.put('3', new int[] { VK_3 });
        charMap.put('#', new int[] { VK_SHIFT, VK_3 });
        charMap.put('4', new int[] { VK_4 });
        charMap.put('$', new int[] { VK_SHIFT, VK_4 });
        charMap.put('5', new int[] { VK_5 });
        charMap.put('%', new int[] { VK_SHIFT, VK_5 });
        charMap.put('6', new int[] { VK_6 });
        charMap.put('^', new int[] { VK_SHIFT, VK_6 });
        charMap.put('7', new int[] { VK_7 });
        charMap.put('&', new int[] { VK_SHIFT, VK_7 });
        charMap.put('8', new int[] { VK_8 });
        charMap.put('*', new int[] { VK_SHIFT, VK_8 });
        charMap.put('9', new int[] { VK_9 });
        charMap.put('(', new int[] { VK_SHIFT, VK_9 });
        charMap.put('0', new int[] { VK_0 });
        charMap.put(')', new int[] { VK_SHIFT, VK_0 });
        charMap.put('-', new int[] { VK_MINUS });
        charMap.put('_', new int[] { VK_SHIFT, VK_MINUS });
        charMap.put('=', new int[] { VK_EQUALS });
        charMap.put('+', new int[] { VK_SHIFT, VK_EQUALS });
        charMap.put('[', new int[] { VK_OPEN_BRACKET });
        charMap.put('{', new int[] { VK_SHIFT, VK_OPEN_BRACKET });
        charMap.put(']', new int[] { VK_CLOSE_BRACKET });
        charMap.put('}', new int[] { VK_SHIFT, VK_CLOSE_BRACKET });
        charMap.put('\\', new int[] { VK_BACK_SLASH });
        charMap.put('|', new int[] { VK_SHIFT, VK_BACK_SLASH });
        charMap.put(';', new int[] { VK_SEMICOLON });
        charMap.put(':', new int[] { VK_SHIFT, VK_SEMICOLON });
        charMap.put('\'', new int[] { VK_QUOTE });
        charMap.put('\"', new int[] { VK_SHIFT, VK_QUOTE });
        charMap.put(',', new int[] { VK_COMMA });
        charMap.put('<', new int[] { VK_SHIFT, VK_COMMA });
        charMap.put('.', new int[] { VK_PERIOD });
        charMap.put('>', new int[] { VK_SHIFT, VK_PERIOD });
        charMap.put('/', new int[] { VK_SLASH });
        charMap.put('?', new int[] { VK_SHIFT, VK_SLASH });
        charMap.put(' ', new int[] { VK_SPACE });
    }

    public int[] getKeyboardAction(char key) {
        return charMap.get(key);
    }
}
