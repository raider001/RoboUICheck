package com.kalynx.uitestframework;

import com.kalynx.lwdi.DependencyInjectionException;
import com.kalynx.uitestframework.arg.ArgParser;
import com.kalynx.uitestframework.controller.*;
import com.kalynx.uitestframework.screen.CvMonitor;
import nu.pattern.OpenCV;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.remoteserver.RemoteServer;

import java.awt.*;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends AnnotationLibrary {

    public Main() {
        super("com/kalynx/uitestframework/keywords/*.class");

    }

    private static RemoteServer loadArgs(String... args) {
        AtomicReference<RemoteServer> robotRemoteServer = new AtomicReference<>(null);

        ArgParser argParser = new ArgParser();
        argParser.addArg("port", Integer.class)
                .setShortKey('p')
                .setCommand(port ->
                {
                    RemoteServer remoteServer = new RemoteServer(port);
                    robotRemoteServer.set(remoteServer);
                })
                .setDefault(1337)
                .setHelp("Sets the port number for the service.");

        argParser.addArg("log-location", String.class).setShortKey('l')
                .setHelp("Sets the location for the log file.")
                .setDefault("./log")
                .setCommand(val -> DI.getInstance().getDependency(CvMonitor.class).setLogLocation(Path.of(val)));

        argParser.addArg("image-loc", String.class)
                .setShortKey('i')
                .setHelp("Defines the directory name for image results.")
                .setDefault("image_results")
                .setCommand(val -> {
                    try {
                        DI.getInstance().getDependency(CvMonitor.class).setResultsLocation(Path.of(val));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        argParser.parse(args);
        return robotRemoteServer.get();
    }
    @Override
    public String getKeywordDocumentation(String keywordName) {
        if (keywordName.equals("__intro__"))
            return """
                    This is a UI Test Framework bought to you by kalynx
                    """;
        return super.getKeywordDocumentation(keywordName);
    }

    private static void injectDependencies() throws AWTException, DependencyInjectionException {
        OpenCV.loadShared();
        DI.getInstance().inject(Settings.class);
        DI.getInstance().add(RobotControl.class, new RobotWrapper(new Robot()));
        DI.getInstance().add(MouseInfoControl.class, new MouseInfoWrapper());
        DI.getInstance().inject(DisplayManager.class);
        DI.getInstance().inject(WindowController.class);
        DI.getInstance().inject(MouseController.class);
        DI.getInstance().add(new CvMonitor(0.95, DI.getInstance().getDependency(DisplayManager.class), DI.getInstance().getDependency(Settings.class)));
    }
    public static void main(String... args ) throws Exception {
        RemoteServer.configureLogging();
        injectDependencies();
        RemoteServer server = loadArgs(args);
        server.putLibrary("/", new Main());
        server.start();
    }
}
