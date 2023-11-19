package com.kalynx.snagtest.manager;

import com.kalynx.lwdi.DI;
import com.kalynx.snagtest.data.DisplayAttributes;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages the display referencing functionality.
 */
public class DisplayManager {

    private final Map<Integer, DisplayAttributes> displayIdToDimensionMap = new HashMap<>();
    private final Map<String, DisplayAttributes> displayNameToDimensionMap = new HashMap<>();
    private final Map<DisplayAttributes, DisplayData> displayData = new HashMap<>();
    private DisplayAttributes selectedDisplay;

    @DI
    public DisplayManager() throws AWTException {
        GraphicsDevice[] d = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (int i = 0; i < d.length; i++) {
            Rectangle rectangle = d[i].getConfigurations()[0].getBounds();
            GraphicsDevice defaultDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            DisplayAttributes attr = new DisplayAttributes(i, d[i], defaultDevice.equals(d[i]), rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            displayIdToDimensionMap.put(i, attr);
            if (attr.primary()) {
                selectedDisplay = attr;
            }

            int cores = Runtime.getRuntime().availableProcessors();
            ConcurrentLinkedQueue<Robot> robots = new ConcurrentLinkedQueue<>();
            for (int j = 0; j < cores * 10; j++) {

                robots.add(new Robot(defaultDevice));
            }
            displayData.put(attr, new DisplayData(rectangle, robots));
        }
    }

    public DisplayManager(DisplayAttributes... attrs) {
        Arrays.stream(attrs).toList().forEach(attr -> {
            displayIdToDimensionMap.put(attr.displayId(), attr);
            int cores = Runtime.getRuntime().availableProcessors();
            ConcurrentLinkedQueue<Robot> robots = new ConcurrentLinkedQueue<>();
            for (int j = 0; j < cores; j++) {
                try {
                    robots.add(new Robot(attr.graphicsDevice()));
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                }
            }
            displayData.put(attr, new DisplayData(new Rectangle(attr.x(), attr.y(), attr.width(), attr.height()), robots));
        });
    }

    public void setCores(int cores) {
        displayData.keySet().forEach(key -> {
            displayData.get(key).robots().clear();
            for (int i = 0; i < cores; i++) {
                try {
                    displayData.get(key).robots().add(new Robot(key.graphicsDevice()));
                } catch (AWTException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public DisplayAttributes getDisplay(String reference) {
        return displayNameToDimensionMap.get(reference);
    }

    public DisplayAttributes getDisplay(int reference) {
        return displayIdToDimensionMap.get(reference);
    }

    public List<DisplayAttributes> getDisplays() {
        return displayIdToDimensionMap.values().stream().toList();
    }

    public void setDisplay(String display) {
        DisplayAttributes attributes = getDisplay(display);
        if (attributes == null) throw new IllegalArgumentException("Display " + display + " does not exist");
        selectedDisplay = attributes;
    }

    public void setDisplay(int display) {
        selectedDisplay = displayData.keySet().stream()
                .filter(data -> data.displayId() == display).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Display " + display + " does not exist"));
    }

    public void setPrimaryReference(String referenceName) {
        displayIdToDimensionMap.values().stream()
                .filter(DisplayAttributes::primary)
                .findFirst()
                .ifPresent(displayAttributes -> displayNameToDimensionMap.put(Objects.requireNonNull(referenceName), displayAttributes));
    }

    public DisplayData getSelectedDisplayRegion() {
        return displayData.get(selectedDisplay);
    }

    public Relative setReference(String referenceName) {
        return new Relative(referenceName);
    }

    private void determineDisplayClosestToLeft(String referenced, DisplayAttributes referenceAttr, String displayName) {
        // calculation
        // must be within or equal the reference monitor vertical range (x, x + height)
        // must be the closest to the left of the reference monitor without it being the same 'x' position
        // where more than one monitor is to the left, use the closest to the reference monitor.
        displayIdToDimensionMap.values().stream()
                .filter(dispAttr -> {
                            return dispAttr.displayId() != referenceAttr.displayId()                                         // Is not the same display
                                    && dispAttr.y() >= referenceAttr.y() && dispAttr.y() <= referenceAttr.y() + referenceAttr.height()    // y is within the horizontal bounds
                                    && dispAttr.x() < referenceAttr.x();                                                             // x is to the left of the reference monitor
                        }
                ).max(Comparator.comparingInt(DisplayAttributes::x))                                                          // find the closest to the left
                .ifPresentOrElse(res -> displayNameToDimensionMap.put(displayName, res), () -> {
                });

    }

    private void determineDisplayClosestToRight(String referenced, DisplayAttributes referenceAttr, String displayName) {
        // calculation
        // must be within or equal the reference monitor vertical range (x, x + height)
        // must be the closest to the left of the reference monitor without it being the same 'x' position
        // where more than one monitor is to the left, use the closest to the reference monitor.
        displayIdToDimensionMap.values().stream()
                .filter(dispAttr -> {
                            return dispAttr.displayId() != referenceAttr.displayId()                                         // Is not the same display
                                    && dispAttr.y() >= referenceAttr.y() && dispAttr.y() <= referenceAttr.y() + referenceAttr.height()    // y is within the horizontal bounds
                                    && dispAttr.x() > referenceAttr.x();                                                              // x is to the left of the reference monitor
                        }
                ).min(Comparator.comparingInt(DisplayAttributes::x))                                                          // find the closest to the left
                .ifPresentOrElse(res -> displayNameToDimensionMap.put(displayName, res), () -> {
                });

    }

    public DisplayAttributes getSelectedDisplay() {
        return selectedDisplay;
    }

    private void determineDisplayClosestToTop(String referenced, DisplayAttributes referenceAttr, String displayName) {
        displayIdToDimensionMap.values().stream()
                .filter(dispAttr -> {
                            return dispAttr.displayId() != referenceAttr.displayId()                                     // Is not the same display
                                    && dispAttr.x() >= referenceAttr.x() && dispAttr.x() <= referenceAttr.x() + referenceAttr.width() // x is within the reference vertical bounds
                                    && dispAttr.y() < referenceAttr.y();
                        }// y is above the reference monitor

                ).max(Comparator.comparingInt(DisplayAttributes::y))
                .ifPresentOrElse(res -> displayNameToDimensionMap.put(displayName, res), () -> {
                });
    }

    private void determineDisplayClosestToBottom(String referenced, DisplayAttributes referenceAttr, String displayName) {
        displayIdToDimensionMap.values().stream()
                .filter(dispAttr -> {
                            return dispAttr.displayId() != referenceAttr.displayId()                                     // Is not the same display
                                    && dispAttr.x() >= referenceAttr.x() && dispAttr.x() <= referenceAttr.x() + referenceAttr.width() // x is within the reference vertical bounds
                                    && dispAttr.y() > referenceAttr.y();
                        }// y is below the reference monitor

                ).min(Comparator.comparingInt(DisplayAttributes::y))
                .ifPresentOrElse(res -> displayNameToDimensionMap.put(displayName, res), () -> {
                });
    }

    private void determineLargerScreen(String referenced, DisplayAttributes referenceAttr, String displayName) {
        displayIdToDimensionMap.values().stream()
                .filter(dispAttr -> {
                    return dispAttr.displayId() != referenceAttr.displayId()                                    // Is not the same display
                            && dispAttr.width() * dispAttr.height() > referenceAttr.width() * referenceAttr.height();   // Is larger than the reference monitor
                }).min(Comparator.comparingInt(distAttr -> distAttr.width() * distAttr.height()))
                .ifPresentOrElse(res -> displayNameToDimensionMap.put(displayName, res), () -> {
                });
    }

    private void determineSmallerScreen(String referenced, DisplayAttributes referenceAttr, String displayName) {
        displayIdToDimensionMap.values().stream()
                .filter(dispAttr -> {
                    return dispAttr.displayId() != referenceAttr.displayId()                                    // Is not the same display
                            && dispAttr.width() * dispAttr.height() < referenceAttr.width() * referenceAttr.height();   // Is larger than the reference monitor
                }).max(Comparator.comparingInt(distAttr -> distAttr.width() * distAttr.height()))
                .ifPresentOrElse(res -> displayNameToDimensionMap.put(displayName, res), () -> {
                });
    }

    public record DisplayData(Rectangle displayRegion, ConcurrentLinkedQueue<Robot> robots) {
    }

    public class Relative {
        private final String referenceName;

        private Relative(String referenceName) {
            this.referenceName = referenceName;
        }

        public Existing relative(RelativeEnum rel) {
            return new Existing(referenceName, rel);
        }
    }

    public class Existing {
        private final String newReference;
        private final RelativeEnum rel;

        private Existing(String newReference, RelativeEnum rel) {
            this.newReference = newReference;
            this.rel = rel;
        }

        public void of(String referenceName) {
            DisplayAttributes referenceAttr = DisplayManager.this.displayNameToDimensionMap.get(referenceName);
            displayNameToDimensionMap.put(newReference, displayNameToDimensionMap.get(referenceName));
            switch (rel) {
                case LEFT -> determineDisplayClosestToLeft(referenceName, referenceAttr, newReference);
                case RIGHT -> determineDisplayClosestToRight(referenceName, referenceAttr, newReference);
                case ABOVE -> determineDisplayClosestToTop(referenceName, referenceAttr, newReference);
                case BELOW -> determineDisplayClosestToBottom(referenceName, referenceAttr, newReference);
                case SMALLER_THAN -> determineSmallerScreen(referenceName, referenceAttr, newReference);
                case LARGER_THAN -> determineLargerScreen(referenceName, referenceAttr, newReference);
            }
        }
    }
}
