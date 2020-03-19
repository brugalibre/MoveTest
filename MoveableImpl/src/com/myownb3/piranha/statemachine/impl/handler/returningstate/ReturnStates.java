/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler.returningstate;

import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.states.EvasionStates;

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
}
