package com.myownb3.piranha.ui.image;

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
