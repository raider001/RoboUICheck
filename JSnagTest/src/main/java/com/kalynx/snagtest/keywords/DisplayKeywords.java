package com.kalynx.snagtest.keywords;

import com.kalynx.snagtest.SnagTest;
import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.manager.RelativeEnum;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;

@RobotKeywords
public class DisplayKeywords {
    private static final DisplayManager DISPLAY_MANAGER = SnagTest.DI.getDependency(DisplayManager.class);

    @RobotKeyword("""
            Sets the primary display to the given reference name
            """)
    @ArgumentNames({"referenceName"})
    public void setPrimaryDisplayReference(String referenceName) {
        DISPLAY_MANAGER.setPrimaryReference(referenceName);
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
        DISPLAY_MANAGER.setReference(originReference)
                .relative(e)
                .of(newReferenceName);
    }

    @RobotKeyword("""
            Set Display
                        
            | variable       | default  | unit                  |
            | displayId      |   0      | integer|String        |
                        
            Sets the display to look at.
            """)
    @ArgumentNames({"displayId"})
    public void setDisplay(int display) {
        DISPLAY_MANAGER.setDisplay(display);
    }

    @RobotKeyword("""
            Set Display Reference
                        
            | variable       | default  | unit                  |
            | display        |   N/A    | string                |
                        
            Sets the display to look at.
            """
    )
    @ArgumentNames({"display"})
    public void setDisplayByReference(String display) {
        DISPLAY_MANAGER.setDisplay(display);
    }
}
