package com.kalynx.robotsikuliwrapper.screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface Screenshot {

   public BufferedImage screenshot();

   public void close();
}
