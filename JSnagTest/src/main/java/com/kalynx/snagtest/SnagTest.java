package com.kalynx.snagtest;

import org.apache.commons.io.IOUtils;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class SnagTest extends AnnotationLibrary {

    public SnagTest() {
        // tell AnnotationLibrary where to find the keywords
        super("com/kalynx/snagtest/keywords/*.class");
    }

    public static void main(String[] args) throws Exception {
        RemoteServer.configureLogging();
        RemoteServer server = new RemoteServer(8276);
        server.putLibrary("/", new SnagTest());
        server.start();
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        System.out.println(keywordName);
        // There is a known issue with robot framework remote where it can be difficult to regenrate the documentation.
        // To regenerate it for new robot files, you will need to:
        // Navigate to <drive>/Users/<username>/.robotframework-ls/specs/v2/<dev-id>/user/
        // Delete the files related top Remote
        if (keywordName.equals("__intro__"))
            return getIntro();
        return super.getKeywordDocumentation(keywordName);
    }

    // The introduction is stored in a text file resource because it is easier to edit than String constants.
    private String getIntro() {
        try {
            InputStream introStream = SnagTest.class.getResourceAsStream("__intro__.txt");
            StringWriter writer = new StringWriter();
            IOUtils.copy(introStream, writer, Charset.defaultCharset());
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
