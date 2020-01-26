package com.myownb3.piranha.statemachine.handler;

import com.myownb3.piranha.statemachine.handler.input.EventStateInput;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;

/**
 * An {@link StateFullEvasionStatesHandler} handles a specific state of the
 * {@link EvasionStateMachine}
 * 
 * @author DStalder
 *
 */
public interface StateFullEvasionStatesHandler<T extends EventStateInput> extends EvasionStatesHandler<T> {

    /**
     * Reinitializes this statefull {@link EvasionStatesHandler}
     */
    void init ();
}
