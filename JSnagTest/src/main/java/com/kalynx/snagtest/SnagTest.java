package com.kalynx.snagtest;

import com.kalynx.lwdi.DependencyInjectionException;
import com.kalynx.lwdi.DependencyInjector;
import com.kalynx.snagtest.arg.ArgParser;
import com.kalynx.snagtest.control.KeyboardController;
import com.kalynx.snagtest.control.MouseController;
import com.kalynx.snagtest.data.DisplayList;
import com.kalynx.snagtest.data.MethodModelGenerator;
import com.kalynx.snagtest.screen.CvMonitor;
import com.kalynx.snagtest.settings.TimeSettings;
import com.kalynx.snagtest.template.TemplateRetreiver;
import com.kalynx.snagtest.wrappers.MouseInfoControl;
import com.kalynx.snagtest.wrappers.MouseInfoWrapper;
import com.kalynx.snagtest.wrappers.RobotControl;
import com.kalynx.snagtest.wrappers.RobotWrapper;
import nu.pattern.OpenCV;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.library.KeywordDocumentationRepository;
import org.robotframework.javalib.library.RobotFrameworkDynamicAPI;
import org.robotframework.remoteserver.RemoteServer;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class SnagTest implements KeywordDocumentationRepository, RobotFrameworkDynamicAPI {

    public static final DependencyInjector DI = new DependencyInjector();
    private static RemoteServer remoteServer;
    private final AnnotationLibrary annotationLibrary;

    private SnagTest() throws AWTException, DependencyInjectionException {

        DisplayList displays = new DisplayList();
        GraphicsDevice[] d = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice graphicsDevice : d) {
            Rectangle rectangle = graphicsDevice.getConfigurations()[0].getBounds();
            displays.add(rectangle);
        }

        DI.add(RobotControl.class, new RobotWrapper(new Robot()));
        DI.add(MouseInfoControl.class, new MouseInfoWrapper());
        DI.add(displays);
        DI.add(new TimeSettings());
        DI.inject(MouseController.class);
        DI.add(new CvMonitor(0.95, d));
        DI.inject(KeyboardController.class);

        annotationLibrary = new AnnotationLibrary("com/kalynx/snagtest/**/*.class");

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

        argParser.addArg("image-loc", String.class)
                .setShortKey('i')
                .setHelp("Defines the directory name for image results.")
                .setDefault("./")
                .setCommand(val -> {
                    try {
                        DI.getDependency(CvMonitor.class).setResultsLocation(Path.of(val));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        argParser.addArg("generate", Boolean.class).setShortKey('g').setHelp("Generates the keywords.resource file.").setCommand(val -> {
            MethodModelGenerator gen = new MethodModelGenerator();
            if (!val) return;
            gen.addMethods(snagTest.annotationLibrary);
            try {
                TemplateRetreiver retriever = new TemplateRetreiver();
                retriever.generateKeywords(gen.getMethods());
                System.exit(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).setDefault(false);
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
        }, 2, TimeUnit.SECONDS);
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
