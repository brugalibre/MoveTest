package com.myownb3.piranha.core.statemachine.handler.returningstate;

import com.myownb3.piranha.core.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;

/**
 * The {@link ReturningStateHandler} is responsible to return to a direction which leads to an end-position
 * 
 * @author Dominic
 *
 */
public interface ReturningStateHandler extends EvasionStatesHandler<ReturningEventStateInput, CommonEvasionStateResult> {
   // no-op
}
