package com.myownb3.piranha.core.weapon.tank.shape;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * The {@link TankShape} is the shape of a {@link Tank}
 * 
 * @author Dominic
 *
 */
public interface TankShape extends Shape {

   /**
    * @return the {@link Shape} of the {@link Turret}
    */
   Shape getTurretShape();

   /**
    * @return the hull of this Tank as {@link Shape}
    */
   Shape getHull();
}
