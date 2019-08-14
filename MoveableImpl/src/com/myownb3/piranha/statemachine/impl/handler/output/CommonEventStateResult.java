package com.myownb3.piranha.statemachine.impl.handler.output;

import com.myownb3.piranha.statemachine.handler.output.EventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class CommonEventStateResult implements EventStateResult {

    private EvasionStates nextState;

    protected CommonEventStateResult(EvasionStates nextState) {
	this.nextState = nextState;
    }
    
    public final EvasionStates getNextState() {
        return this.nextState;
    }

    /**
     * Creates a new {@link CommonEventStateResult} with the given {@link EvasionStates} as next state
     * @param nextState the next state
     * @return a new {@link CommonEventStateResult} with the given {@link EvasionStates} as next state
     */
    public static CommonEventStateResult of(EvasionStates nextState) {
	return new CommonEventStateResult(nextState);
    }
}
