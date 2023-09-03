package com.kalynx.snagtest.tmp;

import javax.swing.*;
import java.awt.*;

public class RandomComponent {
    static String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
    public static Component getRandomComponent() {
        int v = (int) (Math.random() * 8);


        return switch (v) {
            case 0 -> new JRadioButton(getRandomText());
            case 1 -> new JLabel(getRandomText());
            case 2 -> new JButton(getRandomText());
            case 3 -> new JCheckBox(getRandomText());
            case 4 ->
                    new JList<>(new String[]{getRandomText(), getRandomText(), getRandomText(), getRandomText(), getRandomText()});
            case 5 ->
                    new JComboBox<>(new String[]{getRandomText(), getRandomText(), getRandomText(), getRandomText(), getRandomText()});
            case 7 -> new JPanel();
            default -> new JSlider();
        };
    }
    private static String getRandomText() {
        String str = "";
        int strLength = (int) (Math.random() * 20);
        for(int strIdx = 0; strIdx < strLength; strIdx++) {
            int charIndex
                    = (int)(AlphaNumericString.length()
                    * Math.random());
            str = str + AlphaNumericString
                    .charAt(charIndex);
        }
        return str;
    }
}
