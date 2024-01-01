package com.kalynx.uitestframework.controller;


import com.kalynx.uitestframework.data.DisplayAttributes;
import com.kalynx.uitestframework.data.RelativeEnum;
import com.kalynx.uitestframework.testdata.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class DisplayManagerTests {
    private static DisplayManager sut;

    @BeforeEach
    public void beforeEach() {
        sut = TestData.createDisplayManager();
        sut.setDisplay(0);
    }

    @Test
    public void referenced_topScreen_givesTopScreen() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("TOP_SCREEN")
                .relative(RelativeEnum.ABOVE)
                .of("PRIMARY_SCREEN");
        DisplayAttributes attr1 = sut.getDisplay("PRIMARY_SCREEN");
        DisplayAttributes attr2 = sut.getDisplay("TOP_SCREEN");
        Assertions.assertEquals(1, attr1.displayId());
        Assertions.assertEquals(0, attr2.displayId());
    }

    @Test
    public void setDisplay_correctDisplayReference() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("TOP_SCREEN")
                .relative(RelativeEnum.ABOVE)
                .of("PRIMARY_SCREEN");

        sut.setDisplay("PRIMARY_SCREEN");
        Assertions.assertEquals(1, sut.getSelectedDisplay().displayId());
        sut.setDisplay("TOP_SCREEN");
        Assertions.assertEquals(0, sut.getSelectedDisplay().displayId());

    }

    @Test
    public void referenced_bottomScreen_givesBottomScreen() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("BOTTOM_SCREEN")
                .relative(RelativeEnum.BELOW)
                .of("PRIMARY_SCREEN");
        DisplayAttributes attr1 = sut.getDisplay("PRIMARY_SCREEN");
        DisplayAttributes attr2 = sut.getDisplay("BOTTOM_SCREEN");
        Assertions.assertEquals(1, attr1.displayId());
        Assertions.assertEquals(2, attr2.displayId());
    }

    @Test
    public void referenced_leftScreen_givesLeftScreen() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("LEFT_SCREEN")
                .relative(RelativeEnum.LEFT)
                .of("PRIMARY_SCREEN");
        DisplayAttributes attr1 = sut.getDisplay("PRIMARY_SCREEN");
        DisplayAttributes attr2 = sut.getDisplay("LEFT_SCREEN");
        Assertions.assertEquals(1, attr1.displayId());
        Assertions.assertEquals(3, attr2.displayId());
    }

    @Test
    public void referenced_rightScreen_givesRightScreen() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("RIGHT_SCREEN")
                .relative(RelativeEnum.RIGHT)
                .of("PRIMARY_SCREEN");
        DisplayAttributes attr1 = sut.getDisplay("PRIMARY_SCREEN");
        DisplayAttributes attr2 = sut.getDisplay("RIGHT_SCREEN");
        Assertions.assertEquals(1, attr1.displayId());
        Assertions.assertEquals(4, attr2.displayId());
    }

    @Test
    public void referenced_largerScreen_givesLargerScreen() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("LARGER_SCREEN")
                .relative(RelativeEnum.LARGER_THAN)
                .of("PRIMARY_SCREEN");
        DisplayAttributes attr1 = sut.getDisplay("PRIMARY_SCREEN");
        DisplayAttributes attr2 = sut.getDisplay("LARGER_SCREEN");
        Assertions.assertEquals(1, attr1.displayId());
        Assertions.assertEquals(6, attr2.displayId());
    }

    @Test
    public void referenced_largerScreen_givesSmallerScreen() {
        sut.setPrimaryReference("PRIMARY_SCREEN");
        sut.setReference("SMALLER_SCREEN")
                .relative(RelativeEnum.SMALLER_THAN)
                .of("PRIMARY_SCREEN");
        DisplayAttributes attr1 = sut.getDisplay("PRIMARY_SCREEN");
        DisplayAttributes attr2 = sut.getDisplay("SMALLER_SCREEN");
        Assertions.assertEquals(1, attr1.displayId());
        Assertions.assertEquals(5, attr2.displayId());
    }

    @Test
    public void setCaptureRegion_setsCapturedRegionForSelectedDisplay() {
        // Test 1
        Rectangle r = new Rectangle(1,1,2,2);
        sut.setDisplay(0);
        sut.setCaptureRegion(r);
        Assertions.assertEquals(new Rectangle(1,-1919,2,2),sut.getSelectedDisplayRegion().displayRegion());

        //  Test 2
        r = new Rectangle(100,100,250,250);
        sut.setDisplay(2);
        sut.setCaptureRegion(r);
        Assertions.assertEquals(new Rectangle(100,2020,250,250),sut.getSelectedDisplayRegion().displayRegion());

    }

    @Test
    public void setCaptureRegion_xIsLessThan0_throwAssertionError() {
        AssertionError e = Assertions.assertThrows(AssertionError.class, () -> {
            Rectangle r = new Rectangle(-1,1,2,2);
            sut.setCaptureRegion(r);
        });
        Assertions.assertEquals("screenRegion x must be greater than or equal to 0", e.getMessage());
    }

    @Test
    public void setCaptureRegion_yIsLessThan0_throwAssertionError() {
        AssertionError e = Assertions.assertThrows(AssertionError.class, () ->
        {
            Rectangle r = new Rectangle(0,-1,2,2);
            sut.setCaptureRegion(r);
        });
        Assertions.assertEquals("screenRegion y must be greater than or equal to 0", e.getMessage());
    }

    @Test
    public void setCaptureRegion_xIsGreaterThanScreenWidth_throwAssertionError() {
        AssertionError e = Assertions.assertThrows(AssertionError.class, () -> {
            Rectangle r = new Rectangle(1921,1,2,2);
            sut.setCaptureRegion(r);
        });
        Assertions.assertEquals("screenRegion x(1921) + width(2) must be less than the selected screen width(1920)", e.getMessage());
    }

    @Test
    public void setCaptureRegion_widthIsLessThanOrEqualTo0_throwAssertionError() {
        AssertionError e = Assertions.assertThrows(AssertionError.class, () -> {
            Rectangle r = new Rectangle(0,1,0,2);
            sut.setCaptureRegion(r);
        });
        Assertions.assertEquals("screenRegion width must be greater than 0", e.getMessage());
    }

    @Test
    public void setCaptureRegion_heightIsLessThanOrEqualTo0_throwAssertionError() {
        AssertionError e = Assertions.assertThrows(AssertionError.class, () -> {
            Rectangle r = new Rectangle(0,1,2,0);
            sut.setCaptureRegion(r);
        });
        Assertions.assertEquals("screenRegion height must be greater than 0", e.getMessage());
    }

    @Test
    public void setCaptureRegion_heightPlusYIsGreaterThanScreenWidth_throwAssertionError() {
        AssertionError e = Assertions.assertThrows(AssertionError.class, () -> {
            Rectangle r = new Rectangle(0,1,2,1080);
            sut.setCaptureRegion(r);
        });
        Assertions.assertEquals("screenRegion y(1) + height(1080) must be less than the selected screen height(1080)", e.getMessage());
    }

}
