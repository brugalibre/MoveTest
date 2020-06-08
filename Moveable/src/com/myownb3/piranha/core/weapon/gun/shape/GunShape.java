package com.myownb3.piranha.core.weapon.gun.shape;

import java.util.Optional;

import com.myownb3.piranha.core.grid.gridelement.shape.CombinedShape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;

public interface GunShape extends CombinedShape {

   /**
    * @return the length of this {@link GunShape}
    */
   double getLength();

   /**
    * @return the optional muzzle break of this {@link GunShape}
    */
   Optional<Rectangle> getMuzzleBreak();

   /**
    * @return the barrel of this {@link GunShape}
    */
   Rectangle getBarrel();
}
