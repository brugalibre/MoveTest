package com.myownb3.piranha.statemachine.impl.handler.output;

import java.util.List;

import com.myownb3.piranha.moveables.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.EvasionStateHandler;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class EvasionEventStateResult extends CommonEventStateResult{

    private List<MoveableExecutor> executors;

    protected EvasionEventStateResult(EvasionStates nextState, List<MoveableExecutor> executors) {
	super(nextState);
	this.executors = executors;
    }

    /**
     * Creates a new {@link EvasionEventStateResult} with the given
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
    public static EvasionEventStateResult of(EvasionStates nextState, List<MoveableExecutor> executors) {
	return new EvasionEventStateResult(nextState, executors);
    }

    public final List<MoveableExecutor> getExecutors() {
        return this.executors;
    }
}
