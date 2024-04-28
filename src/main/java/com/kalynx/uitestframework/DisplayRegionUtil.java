package com.kalynx.uitestframework;

import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.controller.WindowController;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.exceptions.WindowException;

import java.awt.Rectangle;
import java.util.Optional;

public class DisplayRegionUtil {

    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
    private static final WindowController WINDOW_CONTROLLER = DI.getInstance().getDependency(WindowController.class);

    public Regions getWindowDisplayRegions(String window) throws WindowException {
        Rectangle formRegion = WINDOW_CONTROLLER.getWindowDimensions(window);
        if(formRegion == null) throw new WindowException("Window: " + window + " not found. Available windows:" + WINDOW_CONTROLLER.getAllWindows().toString());
        Optional<DisplayAttributes> attr = DISPLAY_MANAGER.getDisplays().stream().filter(display -> formRegion.x >= display.x()
                && formRegion.x < display.x() + display.width()
                && formRegion.y >= display.y()
                && formRegion.y < display.y() + display.height()).findFirst();

        if(attr.isEmpty()) throw new WindowException("Window not found on any display. Is the form currently hidden or partially off a screen?");
        int originalDisplayId = DISPLAY_MANAGER.getSelectedDisplay().displayId();
        int temporaryDisplayId = attr.get().displayId();

        // get original data
        DisplayAttributes origDispAttr = DISPLAY_MANAGER.getDisplay(originalDisplayId);
        Rectangle origDispDim = new Rectangle(origDispAttr.x(), origDispAttr.y(), origDispAttr.width(), origDispAttr.height());
        Rectangle origView = new Rectangle(DISPLAY_MANAGER.getDisplayDisplayRegion(origDispAttr).displayRegion());
        normalize(origView, origDispDim);

        // get temporary data
        DisplayAttributes tempDispAttr = DISPLAY_MANAGER.getDisplay(temporaryDisplayId);
        Rectangle tempDispDim = new Rectangle(tempDispAttr.x(), tempDispAttr.y(), tempDispAttr.width(), tempDispAttr.height());
        normalize(formRegion, tempDispDim);

        return new Regions(originalDisplayId, origView, temporaryDisplayId, formRegion);
    }

    private void normalize(Rectangle displayRegion, Rectangle display) {
        displayRegion.x = display.x - displayRegion.x;
        displayRegion.y = display.y - displayRegion.y;
    }

    public class Regions {

        public final int originalDisplay;
        public final Rectangle original;

        public final int temporaryDisplay;
        public final Rectangle temporary;

        private Regions(int originalDisplay, Rectangle original, int temporaryDisplay,  Rectangle temporary) {
            this.originalDisplay = originalDisplay;
            this.original = original;
            this.temporaryDisplay = temporaryDisplay;
            this.temporary = temporary;
        }

        public void switchToOriginalDisplay() {
            DISPLAY_MANAGER.setDisplay(originalDisplay);
            DISPLAY_MANAGER.setCaptureRegion(original);
        }

        public void switchToTemporaryDisplay() {
            DISPLAY_MANAGER.setDisplay(temporaryDisplay);
            DISPLAY_MANAGER.setCaptureRegion(temporary);
        }
    }
}
