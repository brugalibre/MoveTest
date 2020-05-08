package com.myownb3.piranha.core.statemachine.impl.handler.passingstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.input.CommonEvasionStateInput;

public class PassingEventStateInput extends CommonEvasionStateInput {

   private Position positionBeforeEvasion;

   private PassingEventStateInput(Grid grid, Moveable moveable, Position positionBeforeEvasion,
         DetectableMoveableHelper helper) {
      super(grid, moveable, helper);
      this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
   }

   public static PassingEventStateInput of(DetectableMoveableHelper helper, Grid grid, Moveable moveable,
         Position positionBeforeEvasion) {
      return new PassingEventStateInput(grid, moveable, positionBeforeEvasion, helper);
   }

   public Position getPositionBeforeEvasion() {
      return positionBeforeEvasion;
   }
}
