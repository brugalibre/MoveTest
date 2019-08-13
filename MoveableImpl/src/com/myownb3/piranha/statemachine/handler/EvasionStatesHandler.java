package com.myownb3.piranha.statemachine.handler;

import com.myownb3.piranha.statemachine.handler.input.EvenStateInput;
import com.myownb3.piranha.statemachine.handler.output.EvenStateResult;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * An {@link EvasionStatesHandler} handles a specific state of the
 * {@link EvasionStateMachine}
 * 
 * @author DStalder
 *
 */
public interface EvasionStatesHandler<T extends EvenStateInput> {

    /**
     * Does the necessary action this {@link EvasionStatesHandler} implements
     * 
     * @param evenStateInput
     *            the input values
     * @return an {@link EvenStateResult}
     */
    public EvenStateResult handle(T evenStateInput);

    /**
     * @return the next {@link EvasionStates} to handle
     */
    public EvasionStates getNextState();
}
