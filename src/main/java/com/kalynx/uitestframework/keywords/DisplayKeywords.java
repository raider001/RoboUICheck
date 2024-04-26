package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.RelativeEnum;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.Rectangle;
import java.util.Map;

@RobotKeywords
public class DisplayKeywords {
    public static final String WIDTH = "width";
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);
    public static final String HEIGHT = "height";

    @RobotKeyword("""
            Sets the primary display to the given reference name
            """)
    @ArgumentNames({"referenceName"})
    public void setPrimaryDisplayReference(String referenceName) {
        DISPLAY_MANAGER.setPrimaryReference(referenceName);
    }

    @RobotKeyword("""
            Set Display
                        
            | variable       | default  | unit                  |
            | displayId      |   0      | integer|String        |
                        
            Sets the display to look at.
            """)
    @ArgumentNames({"displayId"})
    public void setDisplayById(int display) {
        DISPLAY_MANAGER.setDisplay(display);
    }

    @RobotKeyword("""
                Sets the reference display to the given reference name
                Options:
                 LEFT
                 RIGHT
                 ABOVE
                 BELOW
                 SMALLER_THAN
                 LARGER_THAN
            """)
    public void setDisplayReference(String originReference, String relativeState, String newReferenceName) {

        RelativeEnum e = RelativeEnum.valueOf(relativeState.toUpperCase());
        DISPLAY_MANAGER.setReference(newReferenceName)
                .relative(e)
                .of(originReference);
    }

    @RobotKeyword
    @ArgumentNames({"x", "y", WIDTH, HEIGHT})
    public void setMonitoredArea(int x, int y, int width, int height) {
        DISPLAY_MANAGER.setCaptureRegion(new Rectangle(x, y, width, height));
    }

    @RobotKeyword("""
            Reset Monitored Area
                        
            Resets the monitored area for the selected display to the full display.
            """)
    public void resetMonitoredArea() {
        DisplayAttributes attr = DISPLAY_MANAGER.getSelectedDisplay();
        DISPLAY_MANAGER.setDisplayRegion(attr.displayId(), 0, 0, attr.width(), attr.height());
    }

    @RobotKeyword("""
            Set Monitored Area For Display Id
            Sets the capture region for the currently selected display.

            """)
    public void setMonitoredAreaForDisplayId(int displayId, int x, int y, int width, int height) {
        DISPLAY_MANAGER.setDisplayRegion(displayId,x,y,width,height);
    }

    @RobotKeyword("""
            Reset Monitored Area Region For ID
                        
            Resets the monitored area for the selected display to the full display.
            """)
    public void resetMonitoredAreaForId(int displayId) {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(displayId);
        Rectangle displayRegion = new Rectangle(0, 0, attr.width(), attr.height());
        DISPLAY_MANAGER.setCaptureRegion(displayRegion);
    }

    @RobotKeyword("""
            Set Monitored Area For Display
            """)
    public void setMonitoredAreaForDisplay(String displayReference, int x, int y, int width, int height) throws DisplayNotFoundException {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(displayReference);
        DISPLAY_MANAGER.setDisplayRegion(attr.displayId(),x,y,width,height);
    }

    @RobotKeyword("""
            ResetMonitoredAreaForDisplay
            """)
    public void resetMonitoredAreaForDisplay(String displayReference) throws DisplayNotFoundException {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(displayReference);
        Rectangle displayRegion = new Rectangle(0, 0, attr.width(), attr.height());
        DISPLAY_MANAGER.setCaptureRegion(displayRegion);
    }

    @RobotKeyword("""
            Set Display Reference
                        
            | variable       | default  | unit                  |
            | display        |   N/A    | string                |
                        
            Sets the display to look at.
            """
    )
    @ArgumentNames({"display"})
    public void setMonitoredDisplay(String display) throws DisplayNotFoundException {
        DISPLAY_MANAGER.setDisplay(display);
    }

    @RobotKeyword("""
            Get Selected Display Dimensions
            """)
    public Map<String, Integer> getSelectedDisplayDimensions() {
        DisplayAttributes r = DISPLAY_MANAGER.getSelectedDisplay();
       return Map.of("x", r.x(), "y", r.y(), WIDTH, r.width(), HEIGHT, r.height());
    }

    @RobotKeyword("""
            Get Selected Display Monitored Area
            """)
    public Map<String, Integer> getSelectedDisplayMonitoredArea() {
        Rectangle r = DISPLAY_MANAGER.getSelectedDisplayRegion().displayRegion();
        return Map.of("x", r.x, "y", r.y, WIDTH, r.width, HEIGHT, r.height);
    }

    @RobotKeyword("""
            Get Display Dimensions
            """)
    @ArgumentNames("display")
    public Map<String, Integer> getDisplayDimensions(String display) throws DisplayNotFoundException {
        DisplayAttributes r = DISPLAY_MANAGER.getDisplay(display);
        return Map.of("x", r.x(), "y", r.y(), WIDTH, r.width(), HEIGHT, r.height());
    }

    @RobotKeyword("""
            Get Display Monitored Area
            """)
    @ArgumentNames("display")
    public Map<String, Integer> getDisplayMonitoredArea(String display) throws DisplayNotFoundException {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(display);
        Rectangle r = DISPLAY_MANAGER.getDisplayDisplayRegion(attr).displayRegion();
        return Map.of("x", r.x, "y", r.y, WIDTH, r.width, HEIGHT, r.height);
    }

}
