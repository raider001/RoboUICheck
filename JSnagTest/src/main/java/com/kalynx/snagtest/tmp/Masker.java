package com.kalynx.snagtest.tmp;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Masker {
    public static BufferedImage makeMask(BufferedImage imageToMask) throws IOException {
        BufferedImage img = ImageIO.read(new File("./images/template/img_trans.png") );
        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(),
                BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g = gray.createGraphics();
        g.drawImage(img, 0, 0, null);

        for (int y = 0; y < gray.getHeight(); y++)
        {
            for (int x = 0; x < gray.getWidth(); x++)
            {
                int color = 0;
                color = gray.getRGB(x, y);
                if(color > -16777216) {
                   gray.setRGB(x,y,16777215);
                }
            }
        }
        return gray;
    }
}
