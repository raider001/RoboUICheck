package com.kalynx.jsngagtestharness;

import net.miginfocom.swing.MigLayout;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomForm extends JFrame {

    private static final List<String> cells = new ArrayList<>();
    private static final int rows = 24;
    private static final int columns = 5;

    public RandomForm() {

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                cells.add("cell " + x + " " + y);
            }
        }

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLayout(new MigLayout("", "[]".repeat(columns), "[]".repeat(rows)));

        Random r = new Random();
        int componentsToAdd = r.nextInt(1, columns * rows);
        for (int i = 0; i < componentsToAdd; i++) {
            String cell = cells.get(r.nextInt(0, cells.size()));
            this.add(ComponentUtility.getRandomComponent(), cell);
            cells.remove(cell);
        }

        setVisible(true);
        setSize(1000, 1000);
        setPreferredSize(new Dimension(1000, 1000));
        setMinimumSize(new Dimension(1000, 1000));
        setMaximumSize(new Dimension(1000, 1000));

        revalidate();


    }
}
