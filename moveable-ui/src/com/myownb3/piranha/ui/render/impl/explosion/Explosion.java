package com.myownb3.piranha.ui.render.impl.explosion;

import static com.myownb3.piranha.ui.constants.ImageConstants.EXPLOSION_FRAME_PATH;
import static com.myownb3.piranha.ui.constants.ImageConstants.EXPLOSION_IMAGE_SUFFIX;

import com.myownb3.piranha.ui.render.impl.image.ImageSeries;

/**
 * Represents an explosion on the ui
 * 
 * @author Dominic
 *
 */
public class Explosion extends ImageSeries {

   /**
    * Default constructor for default explosion
    */
   private Explosion(double dimensionRadius, boolean resizeSmaller) {
      super(50l, false);
      this.images = loadImageSerie(dimensionRadius, resizeSmaller);
   }

   /**
    * @return <code>true</code> if there is currently an explosion or <code>false</code> if not
    */
   public boolean isExploding() {
      return isPlayback();
   }

   public static Explosion buildDefaultExplosion(double dimensionRadius, boolean withResizeSmaller) {
      return new Explosion(dimensionRadius, withResizeSmaller);
   }

   @Override
   protected String getNextImagePathName(int i) {
      return EXPLOSION_FRAME_PATH + i + EXPLOSION_IMAGE_SUFFIX;
   }

   @Override
   protected int getImageSerieSize() {
      return 18;
   }
}
