package com.myownb3.piranha.ui.image.constants;

import com.myownb3.piranha.ui.image.ImageResource;
import com.myownb3.piranha.ui.image.ImageShape;

public class ImageConsts {
   private static final String IMAGE_SUFFIX = ".png";

   public static final String EXPLOSION_FRAME_PATH = "/image/explosion/frame";

   public static final String EXPLOSION_IMAGE_SUFFIX = IMAGE_SUFFIX;

   public static final String DEFAULT_BACKGROUND = "/image/background_1.jpg";

   public static final String TANK_IMAGE = "/image/tank/tank.png";

   public static final String TANK_HULL_IMAGE = "/image/tank/tank_hull.png";

   public static final String TANK_HULL_IMAGE_V2 = "/image/tank/tank_hull_v2.png";

   public static final String TANK_HULL_SYMECTRIC_IMAGE = "/image/tank/tank_hull_sym.png";

   public static final String TURRET_IMAGE = "/image/turret/turret.png";

   public static final ImageResource GUN_CARRIAGE_IMAGE = new ImageResource("/image/gun/guncarriage.png", ImageShape.RECTANGLE);

   public static final ImageResource GUN_CARRIAGE_IMAGE_V2 = new ImageResource("/image/gun/guncarriage_v2.png", ImageShape.CIRCLE);

   public static final String GUN_IMAGE = "/image/gun/gun.png";

   public static final String GUN_IMAGE_V2 = "/image/gun/gun_v2.png";

   public static final String MISSILE_TAIL_FRAME_PATH = "/image/missile/missile_tail";

   public static final String MISSILE_IMAGE_SUFFIX = IMAGE_SUFFIX;

   private ImageConsts() {
      // private
   }
}
