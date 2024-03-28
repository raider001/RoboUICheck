package com.kalynx.uitestframework.testform;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class TestForm {
    public static void main(String[] args) {
        JDialog jDialog = new JDialog();
        jDialog.setSize(400,400);
        JLabel label = new JLabel();
        JPanel panel = new JPanel();
        JTextField textField = new JTextField();
        textField.setColumns(25);
        JButton testButton = new JButton("Test");
        testButton.addActionListener((e) -> {
            if(label.getText().isEmpty()) {
                label.setText("Test Button Clicked");
            } else { label.setText("");}
        });
        JButton test2Button = new JButton("Test2");
        panel.add(testButton);
        panel.add(textField);
        panel.add(test2Button);

        GraphicsPanel graphicsPanel = new GraphicsPanel();

        JButton clickCounter = new JButton("Click Counter");
        clickCounter.addActionListener((e) -> {
            try {
                int count = Integer.parseInt(label.getText());
                count++;
                label.setText(String.valueOf(count));
            } catch (NumberFormatException ex) {
                label.setText("1");
            }
        });


        JButton pressAndRelease = new JButton();
        pressAndRelease.setText("PAndRTest");
        pressAndRelease.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                label.setText("Pressed");
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                label.setText("Released");
            }
        });
        panel.add(graphicsPanel);
        panel.add(label);
        panel.add(clickCounter);
        panel.add(pressAndRelease);
        jDialog.setTitle("Test Form");
        jDialog.setContentPane(panel);
        jDialog.setVisible(true);
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JDialog blockingDialog  = new JDialog();
        blockingDialog.setTitle("Blocking Form");
        blockingDialog.setSize(400,400);
        blockingDialog.setVisible(true);
    }

    private static class GraphicsPanel extends JPanel {

        public GraphicsPanel() {

            setPreferredSize(new Dimension(200,200));
            setSize(200,200);
            setMinimumSize(new Dimension(200,200));
            setMaximumSize(new Dimension(200,200));
        }
        @Override
        public void paint(java.awt.Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g.setColor(Color.BLUE);
            Ellipse2D.Double circle = new Ellipse2D.Double(10, 10, 100, 100);
            g2d.fill(circle);

            g.setColor(Color.RED);
            circle = new Ellipse2D.Double(75, 75, 20, 20);
            g2d.fill(circle);

        }
    }
}
