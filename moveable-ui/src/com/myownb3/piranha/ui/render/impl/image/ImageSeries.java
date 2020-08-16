package com.myownb3.piranha.ui.render.impl.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.myownb3.piranha.ui.image.ImageScaler;

/**
 * Represents a serie of BufferedImages
 * 
 * @author Dominic
 *
 */
public abstract class ImageSeries {
   protected List<BufferedImage> images;
   private int counter;
   private Object lock;
   private long timePerFrame;
   private long lastTimeStamp;

   protected ImageSeries(long timePerFrame) {
      this.counter = 0;
      this.lock = new Object();
      this.timePerFrame = timePerFrame;
      this.lastTimeStamp = 0l;
   }

   public BufferedImage getNextImage() {
      synchronized (lock) {
         if (isTimeUp()) {
            setTimeStamp();
            counter++;
         }
         return images.get(counter);
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
         return counter < images.size() - 1;
      }
   }

   protected List<BufferedImage> loadImageSerie(double dimensionRadius, boolean resizeSmaller) {
      List<BufferedImage> newImages = new ArrayList<>();
      for (int i = 1; i < getImageSerieSize(); i++) {
         try {
            String path = getNextImagePathName(i);
            BufferedImage bufferedImage = ImageIO.read(new File(path));
            bufferedImage = resizeImageIfNecessary(bufferedImage, dimensionRadius, resizeSmaller);
            newImages.add(bufferedImage);
         } catch (IOException e) {
            throw new IllegalStateException(e);
         }
      }
      return newImages;
   }

   /**
    * @param i
    *        the current image number
    * @return the path to the image for the given counter
    */
   protected abstract String getNextImagePathName(int i);

   /**
    * @return the amount of images this series contains
    */
   protected abstract int getImageSerieSize();

   private static BufferedImage resizeImageIfNecessary(BufferedImage bufferedImage, double dimensionRadius, boolean resizeSmaller) {
      double max = Math.max(bufferedImage.getWidth(), bufferedImage.getHeight());
      double ratio = (dimensionRadius / max) * 1.5; // a little bit bigger, because the dimension of GridElements are not quite measured in pixels..
      if (ratio > 1.0 || resizeSmaller) {
         return ImageScaler.scaleImage(bufferedImage, ratio * bufferedImage.getWidth(), ratio * bufferedImage.getHeight());
      }
      return bufferedImage;
   }

   /**
    * @return <code>true</code> if this {@link ImageSeries} is currently played or <code>false</code> if not
    */
   public boolean isPlayback() {
      synchronized (lock) {
         return counter > 0 && counter < images.size();
      }
   }
}
