package com.kalynx.robotsikuliwrapper.keywords;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.sikuli.script.ImagePath;

@RobotKeywords
public class SettingsKeywords {
    @RobotKeyword("""
                    Add Image Path
                    Adds the image path relative to the file location calling it.
                    """)
    @ArgumentNames({"path"})
    public boolean addImagePath(String path) {
        return ImagePath.add(path);
    }

    @RobotKeyword("""
                    Add a HTTP Image Path
                    Adds the image path relative to the file location calling it.
                    """)
    @ArgumentNames({"path"})
    public boolean addHttpImagePath(String path) {
        return false;
    }

    @ArgumentNames({"similarity"})
    public boolean setSimilarity(double similarity) {
        return false;
    }
}
