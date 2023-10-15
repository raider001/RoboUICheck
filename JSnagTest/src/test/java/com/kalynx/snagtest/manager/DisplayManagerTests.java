package com.kalynx.snagtest.manager;

import com.kalynx.snagtest.data.DisplayAttributes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DisplayManagerTests {

    private static DisplayManager sut;

    @BeforeEach
    public void beforeEach() {
        DisplayAttributes primary = new DisplayAttributes(1, null, true, 0, 0, 1920, 1080);
        DisplayAttributes top = new DisplayAttributes(0, null, false, 0, -1920, 1920, 1080);
        DisplayAttributes bottom = new DisplayAttributes(2, null, false, 0, 1920, 1920, 1080);
        DisplayAttributes left = new DisplayAttributes(3, null, false, -1920, 0, 1920, 1080);
        DisplayAttributes right = new DisplayAttributes(4, null, false, 1920, 0, 1920, 1080);
        DisplayAttributes smaller = new DisplayAttributes(5, null, false, -2920, 0, 960, 540);
        DisplayAttributes larger = new DisplayAttributes(6, null, false, 0, 3840, 3840, 2160);
        sut = new DisplayManager(primary, top, bottom, left, right, smaller, larger);
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
}
