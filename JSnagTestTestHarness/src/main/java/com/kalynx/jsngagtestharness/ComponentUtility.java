package com.kalynx.jsngagtestharness;

import javax.swing.*;
import java.awt.Component;
import java.util.Random;
import java.util.Vector;

public class ComponentUtility {

    private static final Words words = new Words();
    private static final Random r = new Random();

    private static JLabel generateLabel() {
        return new JLabel(words.getRandomWord());
    }

    private static JCheckBox generateCheckBox() {
        JCheckBox checkBox = new JCheckBox(words.getRandomWord());
        checkBox.setSelected(r.nextBoolean());
        return checkBox;
    }

    private static JTextField generateTextField() {
        JTextField textField = new JTextField(words.getRandomWord());
        textField.setColumns(r.nextInt(1, 5));
        return textField;
    }

    private static JButton generateJButton() {
        JButton jButton = new JButton(words.getRandomWord());
        return jButton;
    }

    private static JTextArea generateTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        int words = r.nextInt(0, 50);
        for (int i = 0; i < words; i++) {
            textArea.append(ComponentUtility.words.getRandomWord() + " ");
        }
        textArea.setColumns(r.nextInt(1, 60));
        textArea.setRows(r.nextInt(1, 20));
        return textArea;
    }

    private static JList<String> generateJList() {
        JList<String> jList = new JList<>();
        Vector<String> data = new Vector<>();
        int items = r.nextInt(1, 10);
        for (int i = 0; i < items; i++) {
            data.add(words.getRandomWord());
        }
        if (r.nextBoolean()) {
            jList.setSelectedIndex(r.nextInt(1, 10));
        }
        return jList;
    }

    private static JRadioButton generateRadioButton() {
        JRadioButton radioButton = new JRadioButton(words.getRandomWord());
        return radioButton;
    }

    public static Component getRandomComponent() {
        int rand = r.nextInt(0, 8);
        return switch (rand) {
            case 0 -> generateLabel();
            case 1 -> generateCheckBox();
            case 2 -> generateComboBox();
            case 3 -> generateRadioButton();
            case 4 -> generateJList();
            case 5 -> generateTextArea();
            case 6 -> generateTextField();
            case 7 -> generateJButton();
            default -> throw new IllegalStateException("Unexpected value: " + rand);
        };
    }

    private static JComboBox generateComboBox() {
        Vector<String> vect = new Vector<>();
        vect.add(words.getRandomWord());
        vect.add(words.getRandomWord());
        vect.add(words.getRandomWord());
        vect.add(words.getRandomWord());
        JComboBox<String> comboBox = new JComboBox<>(vect);
        return comboBox;
    }
}
