package com.myownb3.piranha.ui.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageReader {
   private ImageReader() {
      // private 
   }

   public static BufferedImage resizeImg(BufferedImage image, double scaledWidth, double scaledHeight) {
      // creates output image
      int w2 = (int) Math.floor((scaledWidth));
      int h2 = (int) Math.floor((scaledHeight));
      BufferedImage outputImage = new BufferedImage(w2, h2, image.getType());

      // scales the input image to the output image
      Graphics2D g2d = outputImage.createGraphics();
      g2d.drawImage(image, 0, 0, w2, h2, null);
      g2d.dispose();

      return outputImage;
   }
}
