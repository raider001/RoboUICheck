package com.kalynx.jsngagtestharness;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Main {
    private static final int samples = 10_000;
    private static final Path trainingLocation = Path.of("training-data");
    private static final Path imageLocation = Path.of("images");
    private static final List<FieldMapper<?>> mappers = new ArrayList<>();

    public static void main(String... args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());
        mappers.add(new FieldMapper<>(trainingLocation, JTextField.class));
        mappers.add(new FieldMapper<>(trainingLocation, JComboBox.class));
        mappers.add(new FieldMapper<>(trainingLocation, JTextArea.class));
        mappers.add(new FieldMapper<>(trainingLocation, JButton.class));
        mappers.add(new FieldMapper<>(trainingLocation, JRadioButton.class));
        mappers.add(new FieldMapper<>(trainingLocation, JCheckBox.class));
        mappers.add(new FieldMapper<>(trainingLocation, JLabel.class));
        mappers.add(new FieldMapper<>(trainingLocation, JList.class));
        testHarnessForm();
//        trainingDataGenerator();

    }

    private static void trainingDataGenerator() {

        for (int i = 0; i < samples; i++) {
            RandomForm rf = new RandomForm();
            BufferedImage img = Screenshot.getScreenShot(rf);

            try {
                Path file = Path.of("sample_%s.jpg".formatted(i));
                ImageIO.write(img, "jpg", trainingLocation.resolve(imageLocation).resolve(file).toFile());
                mappers.forEach(item -> item.generateResults(rf, file));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            rf.dispatchEvent(new WindowEvent(rf, WindowEvent.WINDOW_CLOSING));
            rf.dispose();
            System.out.print((double) i / samples * 100 + "%\r");
        }
        System.exit(0);
    }

    private static void testHarnessForm() {
        JFrame jFrame = new JFrame();
        jFrame.setTitle("JSnagTest test UI Test Harness");
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setLayout(new GridLayout(0, 5, 5, 5));
        jFrame.add(new JButton("Button A"));
        jFrame.add(new JButton("Button B"));

        JTextField textField = new JTextField();
        textField.setColumns(10);
        jFrame.add(textField);

        jFrame.add(new JCheckBox("A Check Box"));
        Vector<String> comboVect = new Vector<>();
        comboVect.add("Red");
        comboVect.add("Green");
        comboVect.add("Blue");
        comboVect.add("Yellow");
        jFrame.add(new JComboBox<>(comboVect));

        for (String item : comboVect) {
            jFrame.add(new JRadioButton(item));
        }
        jFrame.pack();
        jFrame.setMinimumSize(new Dimension(jFrame.getWidth(), jFrame.getHeight()));

    }
}
