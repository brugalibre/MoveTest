/**
 * 
 */
package com.myownb3.piranha.core.statemachine.impl.handler.returningstate;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.EvasionStateMachine;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

enum ReturnStates {

   /**
    * This is the default state. The first time the {@link EvasionStateMachine}
    * handles the {@link EvasionStates#RETURNING} this state is set
    */
   ENTER_RETURNING,

   /**
    * After the second time we enter the {@link EvasionStates#RETURNING} and until
    * the angle of the {@link Moveable} is corrected
    */
   ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL,

   /**
    * As soon as the {@link Moveable} has a 90 angle to it's target-position-line
    * or if it reached half the distance, we enter this state
    */
   ANGLE_CORRECTION_PHASE_FROM_ORDONAL,

   /**
    * After the moveable is facing again it's original path we may have to correct the relative angle to the end {@link Position}
    */
   RELATIVE_ANGLE_CORRECTION_TO_END_POS,

   /** Dummy State only for testing purpose */
   NONE,
}
