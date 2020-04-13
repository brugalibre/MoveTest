package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import com.myownb3.piranha.statemachine.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * Declares the different (sub)-states of the State
 * {@link EvasionStates#POST_EVASION}
 * 
 * @author DStalder
 *
 */
public enum PostEvasionStates {

   /**
    * This is the state which is set the first time the
    * {@link PostEvasionStateHandler} is called
    */
   ENTERING_POST_EVASION,

   /** The actual Post-evasion state */
   POST_EVASION,

   /** Dummy State only for testing purpose */
   NONE,
}
