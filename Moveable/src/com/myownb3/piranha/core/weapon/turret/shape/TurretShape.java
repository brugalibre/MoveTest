package com.myownb3.piranha.core.weapon.turret.shape;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;

/**
 * Defines the Shape of a {@link TurretShape}. The {@link Shape#getCenter()} of a {@link TurretShape} is defined by it's
 * {@link TurretShape#getGunCarriageShape()}
 * 
 * @author Dominic
 *
 */
public interface TurretShape extends Shape {

   /**
    * @return the {@link Shape} of the {@link GunCarriage} of this {@link TurretShape}
    */
   Shape getGunCarriageShape();

   /**
    * @return the {@link Shape} of the {@link Gun} of this {@link TurretShape}
    */
   GunShape getGunShape();
}
