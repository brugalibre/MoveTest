package com.myownb3.piranha.ui.render.impl.missile;

import static com.myownb3.piranha.ui.image.constants.ImageConsts.MISSILE_IMAGE_SUFFIX;
import static com.myownb3.piranha.ui.image.constants.ImageConsts.MISSILE_TAIL_FRAME_PATH;

import com.myownb3.piranha.ui.render.impl.image.ImageSeries;

/**
 * Represents the fire tail of a missile
 * 
 * @author Dominic
 *
 */
public class MissileTail extends ImageSeries {

   private double dimensionRadius;

   /**
    * Default constructor for default explosion
    */
   public MissileTail(double dimensionRadius, boolean resizeSmaller) {
      super(50l, true);
      this.images = loadImageSerie(dimensionRadius, resizeSmaller);
      this.dimensionRadius = dimensionRadius;
   }

   /**
    * @return <code>true</code> if there is currently an explosion or <code>false</code> if not
    */
   public boolean isExploding() {
      return isPlayback();
   }

   public static MissileTail buildDefaultExplosionWitzResizeSmaller(double dimensionRadius) {
      return new MissileTail(dimensionRadius, true);
   }

   public static MissileTail buildDefaultExplosion(double dimensionRadius) {
      return new MissileTail(dimensionRadius, false);
   }

   @Override
   protected String getNextImagePathName(int i) {
      return MISSILE_TAIL_FRAME_PATH + i + MISSILE_IMAGE_SUFFIX;
   }

   @Override
   protected int getImageSerieSize() {
      return 5;
   }

   public double getDimensionRadius() {
      return dimensionRadius;
   }
}
