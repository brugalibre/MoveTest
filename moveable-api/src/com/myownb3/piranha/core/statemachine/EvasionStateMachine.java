package com.myownb3.piranha.core.statemachine;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

/**
 * The {@link EvasionStateMachine} is there to handle an evasion maneuvre as a state machine.
 * It is also a {@link MoveablePostActionHandler} in order to perform actions post moving a {@link Moveable}
 * 
 * @author Dominic
 *
 */
public interface EvasionStateMachine extends MoveablePostActionHandler {

   /**
    * Sets the given {@link EndPosition} as current {@link EndPosition} of this {@link EvasionStateMachine}
    * 
    * @param endPos
    *        the new {@link EndPosition}
    */
   void setEndPosition(EndPosition endPos);
}
