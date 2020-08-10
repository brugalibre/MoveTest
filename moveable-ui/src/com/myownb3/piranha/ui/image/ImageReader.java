package com.myownb3.piranha.ui.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageReader {
   private ImageReader() {
      // private 
   }

   public static BufferedImage scaleBicubic(BufferedImage image, double scaleWidth, double scaleHeight) {
      return scale(image, scaleWidth, scaleHeight, AffineTransformOp.TYPE_BICUBIC);
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

   private static BufferedImage scale(BufferedImage image, double scaleWidth, double scaleHeight, int type) {
      int w = image.getWidth();
      int h = image.getHeight();
      int w2 = (int) (w * scaleWidth);
      int h2 = (int) (h * scaleHeight);
      BufferedImage after = new BufferedImage(w2, h2, image.getType());
      AffineTransform scaleInstance = AffineTransform.getScaleInstance(scaleWidth, scaleWidth);
      AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, type);
      scaleOp.filter(image, after);
      RescaleOp rescaleOp = new RescaleOp(1.5f, 15, null);
      rescaleOp.filter(after, after); // Source and destination are the same.
      return after;
   }
}
