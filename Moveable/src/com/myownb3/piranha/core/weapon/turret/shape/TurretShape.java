package com.myownb3.piranha.core.weapon.turret.shape;

import com.myownb3.piranha.core.grid.gridelement.shape.CombinedShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;

public interface TurretShape extends CombinedShape {

   /**
    * @return the {@link Shape} of the {@link GunCarriage} of this {@link TurretShape}
    */
   Shape getGunCarriageShape();

   /**
    * @return the {@link Shape} of the {@link Gun} of this {@link TurretShape}
    */
   GunShape getGunShape();
}
