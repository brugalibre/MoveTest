package com.myownb3.piranha.ui.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * The {@link ImageRotator} rotates BufferedImages
 * 
 * @author Dominic
 *
 */
public class ImageRotator {

   private ImageRotator() {
      // private
   }

   public static BufferedImage rotateImage(BufferedImage bufferedImage, double randomAngle) {
      double rads = Math.toRadians(randomAngle);
      double sin = Math.abs(Math.sin(rads));
      double cos = Math.abs(Math.cos(rads));
      int w = bufferedImage.getWidth();
      int h = bufferedImage.getHeight();
      int newWidth = (int) Math.floor(w * cos + h * sin);
      int newHeight = (int) Math.floor(h * cos + w * sin);

      BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = rotated.createGraphics();
      AffineTransform at = new AffineTransform();
      at.translate((newWidth - w) / 2d, (newHeight - h) / 2d);

      int x = w / 2;
      int y = h / 2;

      at.rotate(rads, x, y);
      g2d.setTransform(at);
      g2d.drawImage(bufferedImage, 0, 0, null);
      g2d.dispose();
      return rotated;
   }

}
