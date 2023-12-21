package com.kalynx.snagtest.test;

import com.kalynx.lwdi.DependencyInjector;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;

public class Test extends AnnotationLibrary {
    public static final DependencyInjector DI = new DependencyInjector();

    public Test() {
        // tell AnnotationLibrary where to find the keywords
        super("com/kalynx/snagtest/test/*.class");
    }

    /* Starts jrobotremoteserver with an example library and returns. The application will shutdown when all of the
     * web server's threads exit.
     */
    public static void main(String[] args) throws Exception {
        RemoteServer.configureLogging();
        RemoteServer server = new RemoteServer(8270);
        server.putLibrary("/", new Test());
        server.start();
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__"))
            return getIntro();
        return super.getKeywordDocumentation(keywordName);
    }

    // The introduction is stored in a text file resource because it is easier to edit than String constants.
    private String getIntro() {
        return """
                JSnagTest is a Robot Framework library for testing Java Swing applications.
                                It uses OpenCV to find and click on components and Tesseract to read text from the screen.
                                """;
    }
}