package com.myownb3.piranha.statemachine.impl.handler.output;

import com.myownb3.piranha.statemachine.states.EvasionStates;

public class EvasionEventStateResult extends CommonEventStateResult{

    protected EvasionEventStateResult(EvasionStates nextState) {
	super(nextState);
    }

    /**
     * Creates a new {@link EvasionEventStateResult} with the given
     * {@link EvasionStates} as next state
     * 
     * @param nextState
     *            the next state
     * 
     * @return a new {@link EvasionEventStateResult} with the given
     *         {@link EvasionStates} as next state
     */
    public static EvasionEventStateResult of(EvasionStates nextState) {
	return new EvasionEventStateResult(nextState);
    }
}
