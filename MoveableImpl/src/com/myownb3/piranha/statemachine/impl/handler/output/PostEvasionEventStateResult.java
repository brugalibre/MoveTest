package com.myownb3.piranha.statemachine.impl.handler.output;

import java.util.List;

import com.myownb3.piranha.moveables.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.EvasionStateHandler;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PostEvasionEventStateResult extends EvasionEventStateResult {

    private PostEvasionEventStateResult(EvasionStates nextState, List<MoveableExecutor> executors) {
	super(nextState, executors);
    }

    /**
     * Creates a new {@link PostEvasionEventStateResult} with the given
     * {@link EvasionStates} as next state
     * 
     * @param nextState
     *            the next state
     * @param executors
     *            the {@link MoveableExecutor} which where added during the
     *            {@link EvasionStatesHandler#handle(com.myownb3.piranha.statemachine.handler.input.EventStateInput)}
     *            of a {@link EvasionStateHandler}
     * 
     * @return a new {@link EvasionEventStateResult} with the given
     *         {@link EvasionStates} as next state
     */
    public static PostEvasionEventStateResult of(EvasionStates nextState, List<MoveableExecutor> executors) {
	return new PostEvasionEventStateResult(nextState, executors);
    }
}
