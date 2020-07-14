package com.myownb3.piranha.core.battle.weapon.turret.shape;

import com.myownb3.piranha.core.battle.weapon.gun.Gun;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

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
