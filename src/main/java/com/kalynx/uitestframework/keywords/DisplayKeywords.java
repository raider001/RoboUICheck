package com.kalynx.uitestframework.keywords;

import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.RelativeEnum;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

import java.awt.*;

@RobotKeywords
public class DisplayKeywords {
    private static final DisplayManager DISPLAY_MANAGER = DI.getInstance().getDependency(DisplayManager.class);

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

    @ArgumentNames({"x", "y", "width", "height"})
    public void setMonitoredArea(int x, int y, int width, int height) {
        DISPLAY_MANAGER.setCaptureRegion(new Rectangle(x, y, width, height));
    }

    @RobotKeyword("""
            Reset Monitored Area Region
                        
            Resets the monitored area for the selected display to the full display.
            """)
    public void resetMonitoredArea() {
        DisplayAttributes attr = DISPLAY_MANAGER.getSelectedDisplay();
        Rectangle displayRegion = new Rectangle(0, 0, attr.width(), attr.height());
        DISPLAY_MANAGER.setCaptureRegion(displayRegion);
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
    public void setMonitoredAreaForDisplay(String displayReference, int x, int y, int width, int height) {
        DisplayAttributes attr = DISPLAY_MANAGER.getDisplay(displayReference);
        DISPLAY_MANAGER.setDisplayRegion(attr.displayId(),x,y,width,height);
    }

    @RobotKeyword("""
            ResetMonitoredAreaForDisplay
            """)
    public void resetMonitoredAreaForDisplay(String displayReference) {
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
    public void setDisplayReference(String display) {
        DISPLAY_MANAGER.setDisplay(display);
    }
}
