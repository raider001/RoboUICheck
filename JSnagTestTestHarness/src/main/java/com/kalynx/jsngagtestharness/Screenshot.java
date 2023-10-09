package com.kalynx.jsngagtestharness;

import javax.swing.JFrame;
import java.awt.image.BufferedImage;

public class Screenshot {

    public static BufferedImage getScreenShot(JFrame jFrame) {
        BufferedImage screenshotImage = new BufferedImage(
                jFrame.getBounds().width, jFrame.getBounds().height,
                BufferedImage.TYPE_INT_RGB);
        jFrame.paint(screenshotImage.getGraphics());
        return screenshotImage;
    }
}
