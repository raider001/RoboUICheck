package com.kalynx.robotsikuliwrapper;


import com.kalynx.robotsikuliwrapper.control.MainController;
import com.kalynx.robotsikuliwrapper.screen.CvMonitor;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.robotframework.javalib.library.AnnotationLibrary;
import org.robotframework.javalib.library.KeywordDocumentationRepository;
import org.robotframework.javalib.library.RobotFrameworkDynamicAPI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;


public class Main implements KeywordDocumentationRepository, RobotFrameworkDynamicAPI {
    private final AnnotationLibrary annotationLibrary;
    private static final MainController mainController = MainController.getInstance();
    private static final Rectangle rectangle;

    static {
       GraphicsDevice[] d = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();

       for(int i = 0; i < d.length; i++) {
           System.out.println(d[i].getConfigurations()[0].getBounds());
           System.out.println("Screen " + i + " - " + d[i].getDisplayMode().getWidth() + ":" + d[i].getDisplayMode().getHeight() + "~" + d[i].getDisplayMode().getRefreshRate());
       }
       rectangle = d[0].getConfigurations()[0].getBounds();

    }

    private Main() throws AWTException {
        annotationLibrary = new AnnotationLibrary("com/kalynx/robotsikuliwrapper/**/*.class");
    }
    public static void main(String... args) throws Exception {
//        RemoteServer.configureLogging();
//        ArgParser argParser = new ArgParser();
//        AtomicReference<RemoteServer> server = new AtomicReference<>();
//        argParser.addArg("port", Integer.class)
//                .setShortKey('p')
//                .setHelp("Sets the port number for SikuliLibrary.")
//                .setDefault(1234)
//                .setCommand( val -> server.set(new RemoteServer(val)));
//        argParser.addArg("scan-rate", Integer.class)
//                .setShortKey('s')
//                .setHelp("Image scan rate in hertz. Setting this too high may lead to inconsistent performance.")
//                .setDefault(6)
//                .setCommand(System.out::println);
//        argParser.addArg("wait-time", Integer.class)
//                .setShortKey('w')
//                .setHelp("The total time Sikuli will take to see an image in milliseconds.")
//                .setDefault(2000)
//                .setCommand(System.out::println);
//        argParser.parse(args);
//
//        server.get().putLibrary("/", new Main());
//        server.get().start();
//        new Test().driver();
//        mainController.getKeyboardController().type("~");
        Thread.sleep(2000);
        OpenCV.loadShared();

        CvMonitor monitor = new CvMonitor(rectangle, 0.95);

        monitor.monitorFor(Duration.ofSeconds(1), "./images/template/img_1.png");
        //mainController.getTimeSettings().setTypeDelay(1000);
        //mainController.getKeyboardController().type("~`!1@2#3$4%5^6&7*8(9)0_-+=qQwWeErRtTyYuUiIoOpP[{]}\\|aAsSdDfFgGhHjJkKlL;:\'\"zZxXcCvVbBnNmM,<.>/?");
        //mainController.getKeyboardController().keyClick(KeyboardSpecialKeys.CONTROL, KeyboardSpecialKeys.A);
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
