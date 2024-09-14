package com.kalynx.uitestframework.testform;

import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseLatencyTestForm {
    public static void main(String[] args) {
        // Create a new instance of the form
        JDialog jDialog = new JDialog();
        jDialog.setSize(1000, 200);
        jDialog.setTitle("Mouse Latency Test Form");
        JPanel panel = new JPanel();
        jDialog.setVisible(true);
        jDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        jDialog.add(panel);
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Mouse Latency Test Form"));
        panel.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                Point loc = e.getPoint();
                System.out.println(loc.getX() + " , " + loc.getY());
            }
        });
    }
}
