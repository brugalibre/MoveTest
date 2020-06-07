package com.myownb3.piranha.core.moveables.controller.forwardstrategy;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;

/**
 * The {@link ForwardStrategyHandler} is used for moving a {@link EndPointMoveable} forward depending on the choosen {@link MovingStrategy}
 * of a {@link MoveableController}
 * 
 * @author Dominic
 *
 */
public interface ForwardStrategyHandler {

   /**
    * Moves the specified {@link EndPointMoveable} foward
    * 
    * @param moveForwardRequest
    *        the request to move it's {@link Moveable} forward
    */
   void moveMoveableForward(MoveForwardRequest moveForwardRequest);

}
