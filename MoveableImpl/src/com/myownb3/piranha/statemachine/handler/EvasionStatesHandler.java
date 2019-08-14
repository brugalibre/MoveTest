package com.myownb3.piranha.statemachine.handler;

import com.myownb3.piranha.statemachine.handler.input.EventStateInput;
import com.myownb3.piranha.statemachine.handler.output.EventStateResult;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;

/**
 * An {@link EvasionStatesHandler} handles a specific state of the
 * {@link EvasionStateMachine}
 * 
 * @author DStalder
 *
 */
public interface EvasionStatesHandler<T extends EventStateInput> {

    /**
     * Does the necessary action this {@link EvasionStatesHandler} implements
     * 
     * @param evenStateInput
     *            the input values
     * @return an {@link EventStateResult}
     */
    public EventStateResult handle(T evenStateInput);
}
