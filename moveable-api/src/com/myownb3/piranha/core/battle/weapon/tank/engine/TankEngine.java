package com.myownb3.piranha.core.battle.weapon.tank.engine;

import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;

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
    * Will move this {@link TankEngine} to stop moving forward
    */
   void stopMoveForward();

   /**
    * 
    * @return get the {@link Moveable} of this {@link TankEngine}
    */
   EndPointMoveable getMoveable();
}
