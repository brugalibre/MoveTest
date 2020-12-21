package com.myownb3.piranha.core.moveables.engine;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The engine of a {@link Moveable} is represented by a {@link MoveableEngine} which is responsible for moving forward, backward and even
 * turning
 * arround
 * 
 * @author Dominic
 *
 */
public interface MoveableEngine {

   /**
    * Will move this {@link MoveableEngine} one increment forward
    */
   void moveForward();

   /**
    * 
    * Will move this {@link MoveableEngine} to stop moving forward
    */
   void stopMoveForward();

   /**
    * 
    * @return get the {@link Moveable} of this {@link MoveableEngine}
    */
   EndPointMoveable getMoveable();
}
