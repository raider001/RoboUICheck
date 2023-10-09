package com.kalynx.jsngagtestharness;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class FieldMapper<T> {
    private final Path basePath;
    private final Class<T> item;
    private final Path posLocation;
    private final Path negLocation;

    public FieldMapper(Path baseLocation, Class<T> item) {
        basePath = baseLocation;
        this.item = item;
        posLocation = basePath.resolve(item.getSimpleName()).resolve("pos.txt");
        negLocation = basePath.resolve(item.getSimpleName()).resolve("neg.txt");
        createFolder();
        createFiles();
    }

    public void generateResults(JFrame frame, Path imageLocation) {
        generateHits(frame, imageLocation);
        generateMisses(frame, imageLocation);
    }

    private void createFiles() {
        try {
            Files.deleteIfExists(posLocation);
            Files.deleteIfExists(negLocation);
            Files.createFile(posLocation);
            Files.createFile(negLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMisses(JFrame frame, Path imageLocation) {
        List<Component> items = Arrays.stream(frame.getContentPane().getComponents()).filter(comp -> comp.getClass().equals(item)).toList();
        if (items.isEmpty()) {
            try (FileWriter writer = new FileWriter(negLocation.toFile(), true)) {
                writer.append("../images/").append(imageLocation.toString()).append("\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void generateHits(JFrame frame, Path imageLocation) {
        try (FileWriter writer = new FileWriter(posLocation.toFile(), true)) {
            List<Component> items = Arrays.stream(frame.getContentPane().getComponents()).filter(comp -> comp.getClass().equals(item)).toList();
            int size = items.size();

            String list = "";
            if (items.isEmpty()) return;
            for (Component component : items) {
                Rectangle item = component.getBounds();
                if (item.getX() < 0 || item.getX() >= frame.getWidth()) {
                    size--;
                    continue;
                }
                if (item.getY() < 0 || item.getY() > frame.getHeight()) {
                    size--;
                    continue;
                }
                if (item.getY() + item.getHeight() + 30 > frame.getHeight()) {
                    size--;
                    continue;
                }
                if (item.getX() + item.getWidth() + 8 > frame.getWidth()) {
                    size--;
                    continue;
                }
                int width = item.getX() + item.getWidth() > frame.getWidth() ? frame.getWidth() : (int) item.getWidth() - 1;
                int height = item.getY() + item.getHeight() > frame.getHeight() ? frame.getHeight() : (int) item.getHeight() - 1;
                // 8 is the starting pixel of the border,
                // 30 is the starting pixel below the header
                list += " %s %s %s %s".formatted((int) item.getX() + 8, (int) item.getY() + 30, width, height);
            }
            String listStart = "%s %s".formatted("../images/" + imageLocation.toString(), size);
            writer.append(listStart).append(list).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void createFolder() {
        Path writePath = basePath.resolve(item.getSimpleName());
        try {
            if (!Files.exists(writePath)) {
                Files.createDirectory(writePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
