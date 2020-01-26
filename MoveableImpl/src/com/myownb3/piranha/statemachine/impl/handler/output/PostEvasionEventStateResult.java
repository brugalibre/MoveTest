package com.myownb3.piranha.statemachine.impl.handler.output;

import com.myownb3.piranha.statemachine.states.EvasionStates;

public class PostEvasionEventStateResult extends CommonEventStateResult {

    private PostEvasionEventStateResult(EvasionStates nextState) {
	super(nextState);
    }

    /**
     * Creates a new {@link PostEvasionEventStateResult} with the given
     * {@link EvasionStates} as next state
     * 
     * @param nextState
     *            the next state
     * @return a new {@link EvasionEventStateResult} with the given
     *         {@link EvasionStates} as next state
     */
    public static PostEvasionEventStateResult of(EvasionStates nextState) {
	return new PostEvasionEventStateResult(nextState);
    }
}
