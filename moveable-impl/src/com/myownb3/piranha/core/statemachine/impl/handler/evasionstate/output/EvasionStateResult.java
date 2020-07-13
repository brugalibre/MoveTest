package com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.output;

import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class EvasionStateResult extends CommonEvasionStateResult {

   private boolean isEvasion;

   private EvasionStateResult(EvasionStates prevState, EvasionStates resultState, boolean isEvasion) {
      super(prevState, resultState, null);
      this.isEvasion = isEvasion;
   }

   /**
    * Creates a new {@link EvasionStateResult}
    * 
    * @param prevState
    *        the previous {@link EvasionStates}
    * @param resultState
    *        the current {@link EvasionStates}
    * @param isEvasion
    *        <code>true</code> if there is currently an evasion. Otherwise <code>false</code>
    * @return a {@link EvasionStateResult}
    */
   public static EvasionStateResult of(EvasionStates prevState, EvasionStates resultState, boolean isEvasion) {
      return new EvasionStateResult(prevState, resultState, isEvasion);
   }

   /**
    * 
    * @return the <code>isEvasion</code> field
    */
   public final boolean isEvasion() {
      return this.isEvasion;
   }
}
