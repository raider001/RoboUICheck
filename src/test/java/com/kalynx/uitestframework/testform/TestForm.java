package com.kalynx.uitestframework.testform;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class TestForm {
    public static void main(String[] args) {
        JDialog jDialog = new JDialog();
        jDialog.setSize(400,400);

        JPanel panel = new JPanel();
        JTextField textField = new JTextField();
        textField.setColumns(25);
        JButton testButton = new JButton("Test");
        JButton test2Button = new JButton("Test2");
        panel.add(textField);
        panel.add(testButton);
        panel.add(test2Button);

        GraphicsPanel graphicsPanel = new GraphicsPanel();

        panel.add(graphicsPanel);
        jDialog.setContentPane(panel);
        jDialog.setVisible(true);
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
