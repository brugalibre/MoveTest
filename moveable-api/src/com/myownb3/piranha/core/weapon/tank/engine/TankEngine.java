package com.myownb3.piranha.core.weapon.tank.engine;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.tank.Tank;

/**
 * The engine of a {@link Tank} is represented by a {@link TankEngine} which is responsible for moving forward, backward and even turning
 * arround
 * 
 * @author Dominic
 *
 */
public interface TankEngine {

   /**
    * Will move this {@link TankEngine} one increment forward
    */
   void moveForward();

   /**
    * 
    * @return get the {@link Moveable} of this {@link TankEngine}
    */
   EndPointMoveable getMoveable();
}
