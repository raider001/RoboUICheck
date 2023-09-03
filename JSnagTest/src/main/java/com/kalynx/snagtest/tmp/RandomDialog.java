package com.kalynx.snagtest.tmp;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class RandomDialog {

    private static JFrame jFrame;
    static String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "0123456789"
            + "abcdefghijklmnopqrstuvxyz";
    public static void main(String... args) throws IOException {
        jFrame = new JFrame();

        jFrame.setSize(800,700);
        JPanel panel = new JPanel(null);
        jFrame.setContentPane(panel);

        jFrame.setVisible(true);

        BufferedImage img = new BufferedImage(jFrame.getWidth(), jFrame.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        StringBuilder builder = new StringBuilder();
        JTextField jTextField = new JTextField();
        panel.add(jTextField);
        for(int i = 0; i < 10000; i++) {
            int x = (int) (Math.random() * 200) + 1;
            int y = (int) (Math.random() * 370) + 1;
            int width = (int) (Math.random()* 300) + 50 + 1;
            int height = 30 + 1;
            int strLength = (int) (Math.random() * 50);
            String str = "";

            for(int strIdx = 0; strIdx < strLength; strIdx++) {
                int charIndex
                        = (int)(AlphaNumericString.length()
                        * Math.random());
                str = str + AlphaNumericString
                        .charAt(charIndex);
            }


            jTextField.setText(str);
            jTextField.setBounds(x,y,width,height);
            builder.append("positives/img" + i + ".jpg 1 " + jTextField.getBounds().x + " " + jTextField.getBounds().y + " " + jTextField.getBounds().width + " " + jTextField.getBounds().height + "\n");
            jFrame.paint(g2d);
            ImageIO.write(img, "jpg", new File("positives/img" + i + ".jpg"));
        }
        try {
            FileWriter f2 = new FileWriter("pos.dat", false);
            f2.write(builder.toString());
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
