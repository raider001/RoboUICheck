package com.kalynx.snagtest;


import com.kalynx.snagtest.arg.ArgParser;
import com.kalynx.snagtest.control.MainController;
import nu.pattern.OpenCV;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.library.KeywordDocumentationRepository;
import org.robotframework.javalib.library.RobotFrameworkDynamicAPI;
import org.robotframework.remoteserver.RemoteServer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class SnagTest implements KeywordDocumentationRepository, RobotFrameworkDynamicAPI {
    
    private final AnnotationLibrary annotationLibrary;
    private static RemoteServer remoteServer;
    private SnagTest() {
        annotationLibrary = new AnnotationLibrary("com/kalynx/snagtest/**/*.class");
        MainController.getInstance();
    }
    public static void main(String... args) throws Exception {
        OpenCV.loadShared();
        RemoteServer.configureLogging();
        SnagTest snagTest = new SnagTest();
        ArgParser argParser = new ArgParser();

        AtomicReference<RemoteServer> robotRemoteServer = new AtomicReference<>(null);

        argParser.addArg("port", Integer.class)
                .setShortKey('p')
                .setCommand(port -> robotRemoteServer.set(new RemoteServer(port)))
                .setDefault(1337)
                .setHelp("Sets the port number for the service.");

        argParser.addArg("gen", Boolean.class)
                .setShortKey('g')
                .setDefault(false)
                .setHelp("Generates a python file with the related keywords in the Java library.")
                .ignoreWhenNotProvided().setCommand(val -> {
                    List<String> keyWords = snagTest.getKeywordNames();
                    System.out.println(keyWords);
                    System.exit(0);
                });
        argParser.parse(args);

        robotRemoteServer.get().putLibrary("/", snagTest);
        robotRemoteServer.get().start();
        remoteServer = robotRemoteServer.get();
    }

    public static void shutdown() {
        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                remoteServer.stop();
                System.exit(0);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        },2, TimeUnit.SECONDS);
    }

    @Override
    public String getKeywordDocumentation(String keywordName) {
        return this.annotationLibrary.getKeywordDocumentation(keywordName);
    }

    @Override
    public List<String> getKeywordArguments(String keywordName) {
        return this.annotationLibrary.getKeywordArguments(keywordName);
    }

    @Override
    public List<String> getKeywordNames() {
        return this.annotationLibrary.getKeywordNames();
    }

    @Override
    public Object runKeyword(String name, List arguments) {
        return this.annotationLibrary.runKeyword(name, arguments);
    }

    @Override
    public Object runKeyword(String name, List arguments, Map kwargs) {
        return this.annotationLibrary.runKeyword(name, arguments, kwargs);
    }
}
