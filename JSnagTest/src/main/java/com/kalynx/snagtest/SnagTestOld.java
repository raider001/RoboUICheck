package com.kalynx.snagtest;

import com.kalynx.lwdi.DependencyInjector;
import com.kalynx.snagtest.arg.ArgParser;
import com.kalynx.snagtest.control.KeyboardController;
import com.kalynx.snagtest.control.MouseController;
import com.kalynx.snagtest.control.WindowController;
import com.kalynx.snagtest.data.DisplayList;
import com.kalynx.snagtest.manager.DisplayManager;
import com.kalynx.snagtest.manager.SnagAnnotationLibrary;
import com.kalynx.snagtest.screen.CvMonitor;
import com.kalynx.snagtest.screen.Ocr;
import com.kalynx.snagtest.settings.TimeSettings;
import com.kalynx.snagtest.wrappers.MouseInfoControl;
import com.kalynx.snagtest.wrappers.MouseInfoWrapper;
import com.kalynx.snagtest.wrappers.RobotControl;
import com.kalynx.snagtest.wrappers.RobotWrapper;
import net.sourceforge.tess4j.Tesseract;
import nu.pattern.OpenCV;
import org.robotframework.remoteserver.RemoteServer;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class SnagTestOld {

    public static final DependencyInjector DI = new DependencyInjector();

    private SnagTestOld(RemoteServer remoteServer) throws Exception {
        remoteServer.putLibrary("/", new SnagAnnotationLibrary());
        remoteServer.start();
    }

    public static void main(String... args) throws Exception {
        OpenCV.loadShared();
        RemoteServer.configureLogging();

        ArgParser argParser = new ArgParser();

        AtomicReference<RemoteServer> robotRemoteServer = new AtomicReference<>(null);

        DisplayList displays = new DisplayList();
        GraphicsDevice[] d = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        for (GraphicsDevice graphicsDevice : d) {
            Rectangle rectangle = graphicsDevice.getConfigurations()[0].getBounds();
            displays.add(rectangle);
        }

        DI.inject(DisplayManager.class);
        DI.add(RobotControl.class, new RobotWrapper(new Robot()));
        DI.add(MouseInfoControl.class, new MouseInfoWrapper());
        DI.add(displays);

        DI.add(new TimeSettings());
        DI.inject(MouseController.class);
        DI.add(new CvMonitor(0.95, DI.getDependency(DisplayManager.class)));
        DI.inject(KeyboardController.class);
        DI.inject(WindowController.class);
        DI.inject(Tesseract.class);
        Ocr ocr = DI.inject(Ocr.class);

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

        argParser.parse(args);
        SnagTestOld SNAG_TEST = new SnagTestOld(robotRemoteServer.get());
    }
}