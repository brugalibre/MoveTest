package com.myownb3.piranha.ui.render.impl.explosion;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.ui.image.ImageScaler;

/**
 * Represents an explosion on the ui
 * 
 * @author Dominic
 *
 */
public class Explosion {
   private List<BufferedImage> explosionImages;
   private int counter;
   private Object lock;
   private long timePerFrame;
   private long lastTimeStamp;

   public Explosion(List<BufferedImage> explosionImages, long timePerFrame) {
      this.explosionImages = explosionImages;
      this.counter = 0;
      this.lock = new Object();
      this.timePerFrame = timePerFrame;
      this.lastTimeStamp = 0l;
   }

   public BufferedImage getNextExplosionImage() {
      synchronized (lock) {
         if (isTimeUp()) {
            setTimeStamp();
            counter++;
         }
         return explosionImages.get(counter);
      }
   }

   private boolean isTimeUp() {
      return System.currentTimeMillis() - lastTimeStamp >= timePerFrame;
   }

   private void setTimeStamp() {
      lastTimeStamp = System.currentTimeMillis();
   }

   public boolean hasNextImage() {
      synchronized (lock) {
         return counter < explosionImages.size() - 1;
      }
   }

   public static Explosion buildDefaultExplosion(GridElement gridElement) {
      List<BufferedImage> explosionImages = new ArrayList<>();
      for (int i = 1; i < 18; i++) {
         try {
            BufferedImage bufferedImage = ImageIO.read(new File("res/image/explosion/frame" + i + ".png"));
            bufferedImage = resizeImageIfNecessary(bufferedImage, gridElement.getDimensionInfo().getDimensionRadius());
            explosionImages.add(bufferedImage);
         } catch (IOException e) {
            throw new IllegalStateException(e);
         }
      }
      return new Explosion(explosionImages, 50l);
   }

   private static BufferedImage resizeImageIfNecessary(BufferedImage bufferedImage, double dimensionRadius) {
      double max = Math.max(bufferedImage.getWidth(), bufferedImage.getHeight());
      double ratio = (dimensionRadius / max) * 1.5; // a little bit bigger, because the dimension of GridElements are not quite measured in pixels..
      if (ratio > 1.0) {
         return ImageScaler.scaleImage(bufferedImage, ratio * bufferedImage.getWidth(), ratio * bufferedImage.getHeight());
      }
      return bufferedImage;
   }

   /**
    * @return <code>true</code> if there is currently an explosion or <code>false</code> if not
    */
   public boolean isExploding() {
      synchronized (lock) {
         return counter > 0 && counter < explosionImages.size();
      }
   }
}
