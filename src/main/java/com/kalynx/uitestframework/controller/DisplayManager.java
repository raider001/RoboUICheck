package com.kalynx.uitestframework.controller;

import com.kalynx.lwdi.DI;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.RelativeEnum;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
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

    // Used for Testing purposes
    @SuppressWarnings("unused")
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

    /**
     * Sets the capture region for the currently selected display.
     * The bounds provided are treated as relative to the selected display.
     * @param screenRegion The Region of the screen to capture.
     */
    public void setCaptureRegion(Rectangle screenRegion) {
        Objects.requireNonNull(screenRegion);
        validateRegionData(screenRegion, getSelectedDisplay());
        getSelectedDisplayRegion().displayRegion().setBounds((int) (screenRegion.getX() + selectedDisplay.x()),
                (int) (selectedDisplay.y() + screenRegion.getY()),
                screenRegion.width,
                screenRegion.height);
    }

    /**
     * Sets the display region for a region with the specific display ID.
     * @param reference Id of the display
     * @param x horizontal position of the screen
     * @param y vertical position of the screen
     * @param width total width to monitor or capture
     * @param height total height to monitor or capture
     */
    public void setDisplayRegion(int reference, int x, int y, int width, int height) {
        DisplayAttributes attr = getDisplay(reference);
        validateRegionData(new Rectangle(x,y,width,height), attr);
        displayData.get(attr).displayRegion().setBounds((x + attr.x()),
                (attr.y() + y),
                width,
                height);
    }

    private void validateRegionData(Rectangle screenRegion, DisplayAttributes selectedDisplay ) {if(screenRegion.x < 0) throw new AssertionError("screenRegion x must be greater than or equal to 0");
        if(screenRegion.y < 0)  throw new AssertionError("screenRegion y must be greater than or equal to 0");
        if (screenRegion.width <= 0) throw new AssertionError("screenRegion width must be greater than 0");
        if (screenRegion.height <= 0) throw new AssertionError("screenRegion height must be greater than 0");
        if(screenRegion.x + screenRegion.width > selectedDisplay.width())
            throw new AssertionError("screenRegion x(" + screenRegion.x + ") + width(" + screenRegion.width + ") must be less than the selected screen width(" + selectedDisplay.width() + ")");
        if(screenRegion.y + screenRegion.height > selectedDisplay.height())
            throw new AssertionError("screenRegion y(" + screenRegion.y + ") + height(" + screenRegion.height + ") must be less than the selected screen height(" + selectedDisplay.height() + ")");

    }

    public BufferedImage capture() {
        DisplayManager.DisplayData dd = getSelectedDisplayRegion();

        Robot robot = getSelectedDisplayRegion().robots().poll();
        assert robot != null;
        BufferedImage img = robot.createScreenCapture(dd.displayRegion());
        getSelectedDisplayRegion().robots().add(robot);
        return img;
    }

    public BufferedImage capture(Rectangle rectangle) throws DisplayNotFoundException {
        DisplayAttributes attr = getDisplays().stream().filter(displayAttributes ->
            displayAttributes.x() <=rectangle.x && displayAttributes.y() <= rectangle.y &&
                    displayAttributes.x() + displayAttributes.width() >= rectangle.x + rectangle.width &&
                    displayAttributes.y() + displayAttributes.height() >= rectangle.y + rectangle.height
        ).findFirst().orElseThrow(() -> new DisplayNotFoundException("Rectangle is not within any display"));
        Robot robot = getDisplayDisplayRegion(attr).robots().poll();
        assert robot != null;
        BufferedImage img = robot.createScreenCapture(rectangle);
        getSelectedDisplayRegion().robots().add(robot);
        return img;
    }

    public DisplayAttributes getDisplay(String reference) throws DisplayNotFoundException {
        DisplayAttributes attr = displayNameToDimensionMap.get(reference);
        if(attr == null) throw new DisplayNotFoundException(reference);
        return attr;
    }

    public DisplayAttributes getDisplay(int reference) {
        return displayIdToDimensionMap.get(reference);
    }



    public List<DisplayAttributes> getDisplays() {
        return displayIdToDimensionMap.values().stream().toList();
    }

    public void setDisplay(String display) throws DisplayNotFoundException {
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

    public DisplayData getDisplayDisplayRegion(DisplayAttributes dispAttr) {
        return displayData.get(dispAttr);
    }

    public Relative setReference(String referenceName) {
        return new Relative(referenceName);
    }

    public DisplayAttributes getSelectedDisplay() {
        return selectedDisplay;
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
                case LEFT -> determineDisplayClosestToLeft(referenceAttr, newReference);
                case RIGHT -> determineDisplayClosestToRight(referenceAttr, newReference);
                case ABOVE -> determineDisplayClosestToTop(referenceAttr, newReference);
                case BELOW -> determineDisplayClosestToBottom(referenceAttr, newReference);
                case SMALLER_THAN -> determineSmallerScreen(referenceAttr, newReference);
                case LARGER_THAN -> determineLargerScreen(referenceAttr, newReference);
            }
        }

        private void determineDisplayClosestToTop(DisplayAttributes referenceAttr, String displayName) {
            displayIdToDimensionMap.values().stream()
                    .filter(dispAttr ->
                            dispAttr.displayId() != referenceAttr.displayId()                                     // Is not the same display
                                    && dispAttr.x() >= referenceAttr.x() && dispAttr.x() <= referenceAttr.x() + referenceAttr.width() // x is within the reference vertical bounds
                                    && dispAttr.y() < referenceAttr.y()// y is above the reference monitor
                    ).max(Comparator.comparingInt(DisplayAttributes::y))
                    .ifPresent(res -> displayNameToDimensionMap.put(displayName, res));
        }

        private void determineDisplayClosestToBottom(DisplayAttributes referenceAttr, String displayName) {
            displayIdToDimensionMap.values().stream()
                    .filter(dispAttr ->
                            dispAttr.displayId() != referenceAttr.displayId()                                     // Is not the same display
                                    && dispAttr.x() >= referenceAttr.x() && dispAttr.x() <= referenceAttr.x() + referenceAttr.width() // x is within the reference vertical bounds
                                    && dispAttr.y() > referenceAttr.y()

                    ).min(Comparator.comparingInt(DisplayAttributes::y))
                    .ifPresent(res -> displayNameToDimensionMap.put(displayName, res));
        }

        private void determineLargerScreen(DisplayAttributes referenceAttr, String displayName) {
            displayIdToDimensionMap.values().stream()
                    .filter(dispAttr -> dispAttr.displayId() != referenceAttr.displayId()                                    // Is not the same display
                            && dispAttr.width() * dispAttr.height() > referenceAttr.width() * referenceAttr.height())
                    .min(Comparator.comparingInt(distAttr -> distAttr.width() * distAttr.height()))
                    .ifPresent(res -> displayNameToDimensionMap.put(displayName, res));
        }

        private void determineSmallerScreen(DisplayAttributes referenceAttr, String displayName) {
            displayIdToDimensionMap.values().stream()
                    .filter(dispAttr -> dispAttr.displayId() != referenceAttr.displayId()                                    // Is not the same display
                            && dispAttr.width() * dispAttr.height() < referenceAttr.width() * referenceAttr.height()).max(Comparator.comparingInt(distAttr -> distAttr.width() * distAttr.height()))
                    .ifPresent(res -> displayNameToDimensionMap.put(displayName, res));
        }

        private void determineDisplayClosestToLeft(DisplayAttributes referenceAttr, String displayName) {
            // calculation
            // must be within or equal the reference monitor vertical range (x, x + height)
            // must be the closest to the left of the reference monitor without it being the same 'x' position
            // where more than one monitor is to the left, use the closest to the reference monitor.
            displayIdToDimensionMap.values().stream()
                    .filter(dispAttr ->
                            dispAttr.displayId() != referenceAttr.displayId()                                         // Is not the same display
                                    && dispAttr.y() >= referenceAttr.y() && dispAttr.y() <= referenceAttr.y() + referenceAttr.height()    // y is within the horizontal bounds
                                    && dispAttr.x() < referenceAttr.x()
                    ).max(Comparator.comparingInt(DisplayAttributes::x))                                                          // find the closest to the left
                    .ifPresent(res -> displayNameToDimensionMap.put(displayName, res));

        }

        private void determineDisplayClosestToRight(DisplayAttributes referenceAttr, String displayName) {
            // calculation
            // must be within or equal the reference monitor vertical range (x, x + height)
            // must be the closest to the left of the reference monitor without it being the same 'x' position
            // where more than one monitor is to the left, use the closest to the reference monitor.
            displayIdToDimensionMap.values().stream()
                    .filter(dispAttr -> dispAttr.displayId() != referenceAttr.displayId()                                         // Is not the same display
                            && dispAttr.y() >= referenceAttr.y() && dispAttr.y() <= referenceAttr.y() + referenceAttr.height()    // y is within the horizontal bounds
                            && dispAttr.x() > referenceAttr.x()
                    ).min(Comparator.comparingInt(DisplayAttributes::x))                                                          // find the closest to the left
                    .ifPresent(res -> displayNameToDimensionMap.put(displayName, res));

        }
    }
}
