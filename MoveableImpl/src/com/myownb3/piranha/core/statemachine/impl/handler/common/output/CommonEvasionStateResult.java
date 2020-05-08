package com.myownb3.piranha.core.statemachine.impl.handler.common.output;

import java.util.Optional;

import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.statemachine.handler.output.EventStateResult;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class CommonEvasionStateResult implements EventStateResult {

   private EvasionStates prevState;
   private EvasionStates nextState;
   private Optional<Position> moveableBeforeEvasionOpt;

   protected CommonEvasionStateResult(EvasionStates prevState, EvasionStates nextState, Position moveableBeforeEvasion) {
      this.nextState = nextState;
      this.prevState = prevState;
      moveableBeforeEvasionOpt = Optional.ofNullable(moveableBeforeEvasion);
   }

   public Optional<Position> getPositionBeforeEvasion() {
      return moveableBeforeEvasionOpt;
   }

   public final EvasionStates getNextState() {
      return this.nextState;
   }

   public final EvasionStates getPrevState() {
      return this.prevState;
   }

   /**
    * Creates a new {@link CommonEvasionStateResult} with the given
    * {@link EvasionStates} as next state
    * 
    * @param prevState
    *        the state which was set before
    * @param nextState
    *        the next state
    * @param moveableBeforeEvasion
    *        the position of the Moveable before there was an
    *        evasion. <code>null</code> when there was none
    * @return a new {@link CommonEvasionStateResult} with the given
    *         {@link EvasionStates} as next state
    */
   public static CommonEvasionStateResult of(EvasionStates prevState, EvasionStates nextState,
         Position moveableBeforeEvasion) {
      return new CommonEvasionStateResult(prevState, nextState, moveableBeforeEvasion);
   }
}
