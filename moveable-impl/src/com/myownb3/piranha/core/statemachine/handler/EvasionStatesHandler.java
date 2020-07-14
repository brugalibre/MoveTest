package com.myownb3.piranha.core.statemachine.handler;

import com.myownb3.piranha.core.statemachine.EvasionStateMachine;
import com.myownb3.piranha.core.statemachine.handler.input.EventStateInput;
import com.myownb3.piranha.core.statemachine.handler.output.EventStateResult;

/**
 * An {@link EvasionStatesHandler} handles a specific state of the
 * {@link EvasionStateMachine}
 * 
 * @author DStalder
 *
 */
public interface EvasionStatesHandler<T extends EventStateInput, R extends EventStateResult> {

   /**
    * Initializes this {@link EvasionStatesHandler}
    */
   void init();

   /**
    * Does the necessary action this {@link EvasionStatesHandler} implements
    * 
    * @param evenStateInput
    *        the input values
    * @return an {@link EventStateResult}
    */
   public R handle(T evenStateInput);
}
