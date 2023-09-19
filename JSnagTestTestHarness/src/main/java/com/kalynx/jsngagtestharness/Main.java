package com.kalynx.jsngagtestharness;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class Main {

    public static void main(String... args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());
        JFrame jFrame = new JFrame();
        jFrame.setTitle("JSnagTest test UI Test Harness");
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setLayout(new GridLayout(0, 5,5,5));
        jFrame.add(new JButton("Button A"));
        jFrame.add(new JButton("Button B"));

        JTextField textField = new JTextField();
        textField.setColumns(10);
        jFrame.add(textField);

        jFrame.add(new JCheckBox("A Check Box"));
        Vector<String> comboVect =new Vector<>();
        comboVect.add("Red");
        comboVect.add("Green");
        comboVect.add("Blue");
        comboVect.add("Yellow");
        jFrame.add(new JComboBox<>(comboVect));

        for(String item : comboVect) {
            jFrame.add(new JRadioButton(item));
        }
        jFrame.pack();
        jFrame.setMinimumSize(new Dimension(jFrame.getWidth(), jFrame.getHeight()));
    }
}
