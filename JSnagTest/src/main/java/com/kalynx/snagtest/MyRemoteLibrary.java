package com.kalynx.snagtest;

import org.apache.commons.io.IOUtils;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class MyRemoteLibrary extends AnnotationLibrary {
    public MyRemoteLibrary() {
        // tell AnnotationLibrary where to find the keywords
//        super("com/kalynx/snagtest/test/*.class");
    }

    /* Starts jrobotremoteserver with an example library and returns. The application will shutdown when all of the
     * web server's threads exit.
     */
    public static void main(String[] args) throws Exception {
        RemoteServer.configureLogging();
        RemoteServer server = new RemoteServer(1338);
        server.putLibrary("/", new MyRemoteLibrary());
        server.start();
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        System.out.println(keywordName);
        if (keywordName.equals("__intro__"))
            return getIntro();
        return super.getKeywordDocumentation(keywordName);
    }

    // The introduction is stored in a text file resource because it is easier to edit than String constants.
    private String getIntro() {
        try {
            InputStream introStream = MyRemoteLibrary.class.getResourceAsStream("__intro__.txt");
            StringWriter writer = new StringWriter();
            IOUtils.copy(introStream, writer, Charset.defaultCharset());
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
