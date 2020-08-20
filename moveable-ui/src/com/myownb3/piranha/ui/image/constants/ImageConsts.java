package com.myownb3.piranha.ui.image.constants;

import com.myownb3.piranha.ui.image.ImageResource;
import com.myownb3.piranha.ui.image.ImageShape;

public class ImageConsts {
   private static final String IMAGE_SUFFIX = ".png";

   public static final String EXPLOSION_FRAME_PATH = "res/image/explosion/frame";

   public static final String EXPLOSION_IMAGE_SUFFIX = IMAGE_SUFFIX;

   public static final String DEFAULT_BACKGROUND = "res/image/background_1.jpg";

   public static final String TANK_IMAGE = "res/image/tank/tank.png";

   public static final String TANK_HULL_IMAGE = "res/image/tank/tank_hull.png";

   public static final String TANK_HULL_IMAGE_V2 = "res/image/tank/tank_hull_v2.png";

   public static final String TANK_HULL_SYMECTRIC_IMAGE = "res/image/tank/tank_hull_sym.png";

   public static final String TURRET_IMAGE = "res/image/turret/turret.png";

   public static final ImageResource GUN_CARRIAGE_IMAGE = new ImageResource("res/image/gun/guncarriage.png", ImageShape.RECTANGLE);

   public static final ImageResource GUN_CARRIAGE_IMAGE_V2 = new ImageResource("res/image/gun/guncarriage_v2.png", ImageShape.CIRCLE);

   public static final String GUN_IMAGE = "res/image/gun/gun.png";

   public static final String GUN_IMAGE_V2 = "res/image/gun/gun_v2.png";

   public static final String MISSILE_TAIL_FRAME_PATH = "res/image/missile/missile_tail";

   public static final String MISSILE_IMAGE_SUFFIX = IMAGE_SUFFIX;

   private ImageConsts() {
      // private
   }
}
