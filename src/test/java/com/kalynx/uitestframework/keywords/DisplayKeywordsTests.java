package com.kalynx.uitestframework.keywords;

import com.kalynx.lwdi.AlreadyAddedException;
import com.kalynx.uitestframework.DI;
import com.kalynx.uitestframework.controller.DisplayManager;
import com.kalynx.uitestframework.exceptions.DisplayNotFoundException;
import com.kalynx.uitestframework.testdata.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

class DisplayKeywordsTests {

    private static DisplayKeywords sut;
    static DisplayManager displayManager = TestData.createDisplayManager();

    @BeforeAll
    static void beforeAll() throws AlreadyAddedException {
        DI.reset();
        DI.getInstance().add(displayManager);
        sut = new DisplayKeywords();
    }

    @Test
    void setMonitoredDisplay_verification() throws DisplayNotFoundException {
        sut.setDisplayById(3);
        sut.setPrimaryDisplayReference("PRIMARY");
        sut.setMonitoredDisplay("PRIMARY");
        Assertions.assertEquals(1,displayManager.getSelectedDisplay().displayId());
    }

    @Test
    void setDisplayReference_verification() throws DisplayNotFoundException {
        sut.setDisplayById(3);
        sut.setPrimaryDisplayReference("PRIMARY");
        sut.setDisplayReference("PRIMARY","RIGHT", "LEFT_MONITOR");
        sut.setMonitoredDisplay("LEFT_MONITOR");
        Assertions.assertEquals(4, displayManager.getSelectedDisplay().displayId());
    }

    @Test
    void setMonitoredArea_verification() {
        sut.setDisplayById(3);
        sut.setMonitoredArea(100,100,200, 200);
        Rectangle result = displayManager.getSelectedDisplayRegion().displayRegion();

        Assertions.assertEquals(new Rectangle(-1820,100,200,200), result);
    }

    @Test
    void resetMonitoredArea_verification() {
        sut.setDisplayById(3);
        sut.setMonitoredArea(100,100,200, 200);
        sut.resetMonitoredArea();
        Rectangle result = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(-1920,0,1920,1080), result);
    }

    @Test
    void setMonitoredAreaForDisplayId_verification() {
        sut.setDisplayById(1);
        sut.setMonitoredAreaForDisplayId(3,300,300,400,400);
        Rectangle r = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(0,0,1920,1080), r);
        sut.setDisplayById(3);
        r = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(-1620,300,400,400), r);
    }

    @Test
    void resetMonitoredAreaForId_verification() {
        sut.setDisplayById(1);
        sut.setMonitoredAreaForDisplayId(3,300,300,400,400);
        Rectangle r = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(0,0,1920,1080), r);
        sut.setDisplayById(3);
        r = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(-1620,300,400,400), r);
        sut.resetMonitoredAreaForId(3);
        Assertions.assertEquals(new Rectangle(-1920,0,1920,1080), r);
    }

    @Test
    void setMonitoredAreaForDisplay_verification() throws DisplayNotFoundException {
        sut.setDisplayById(3);
        sut.setPrimaryDisplayReference("PRIMARY");
        sut.setDisplayReference("PRIMARY","RIGHT", "LEFT_MONITOR");
        sut.setMonitoredDisplay("LEFT_MONITOR");
        sut.setMonitoredAreaForDisplay("LEFT_MONITOR", 50,50,300,300);
        sut.setMonitoredDisplay("LEFT_MONITOR");
        Rectangle result = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(1970, 50,300,300), result);
    }

    @Test
    void resetMonitoredAreaForDisplay_verification() throws DisplayNotFoundException {
        sut.setDisplayById(3);
        sut.setPrimaryDisplayReference("PRIMARY");
        sut.setDisplayReference("PRIMARY","RIGHT", "LEFT_MONITOR");
        sut.setMonitoredDisplay("LEFT_MONITOR");
        sut.setMonitoredAreaForDisplay("LEFT_MONITOR", 50,50,300,300);
        sut.resetMonitoredAreaForDisplay("LEFT_MONITOR");
        sut.setMonitoredDisplay("LEFT_MONITOR");
        Rectangle result = displayManager.getSelectedDisplayRegion().displayRegion();
        Assertions.assertEquals(new Rectangle(1920,0,1920,1080), result);
    }
}
