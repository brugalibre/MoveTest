package com.myownb3.piranha.core.statemachine.impl.handler.returningstate;

import static com.myownb3.piranha.core.statemachine.states.EvasionStates.RETURNING;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.statemachine.handler.returningstate.ReturningStateHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.common.CommonEvasionStateHandlerImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

/**
 * The {@link ReturningStateHandlerWithoutEndPos} does not actually return to a {@link EndPosition} but it returns directly the next
 * following to
 * {@link EvasionStates#RETURNING}
 * 
 * @author Dominic
 *
 */
public class ReturningStateHandlerWithoutEndPos extends CommonEvasionStateHandlerImpl<ReturningEventStateInput, CommonEvasionStateResult>
      implements ReturningStateHandler {

   @Override
   public CommonEvasionStateResult handle(ReturningEventStateInput evenStateInput) {
      return CommonEvasionStateResult.of(RETURNING, evalNextState(evenStateInput, RETURNING.nextState()), null);
   }
}
