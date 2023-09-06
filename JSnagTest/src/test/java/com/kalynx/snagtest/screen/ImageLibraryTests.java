package com.kalynx.snagtest.screen;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageLibraryTests {

    @Test
    void imageLibrary() {
       Path mainPath = Paths.get("C:\\Users\\Daniel\\Documents\\GitHub\\RoboUICheck\\RobotSnagTest\\images");
       Path imagePath = Paths.get("debug.png");
       Path fullPath = mainPath.resolve(imagePath);
       System.out.println(fullPath.toString());
    }
}
