package com.myownb3.piranha.statemachine.handler.postevasion;

import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * Common Interface for all {@link EvasionStatesHandler} for the
 * {@link EvasionStates#POST_EVASION}
 * 
 * @author Dominic
 *
 */
public interface PostEvasionStateHandler
      extends EvasionStatesHandler<PostEvasionEventStateInput, CommonEvasionStateResult> {
   // empty
}
