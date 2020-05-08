package com.myownb3.piranha.core.statemachine.impl.handler.common.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.handler.input.EventStateInput;

public class CommonEvasionStateInput implements EventStateInput {

   private Grid grid;
   private DetectableMoveableHelper helper;
   private Moveable moveable;
   private Position moveablePosBefore;

   protected CommonEvasionStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
      this.grid = requireNonNull(grid);
      this.moveable = requireNonNull(moveable);
      this.helper = requireNonNull(helper);
      moveablePosBefore = moveable.getPosition();
   }

   public DetectableMoveableHelper getHelper() {
      return helper;
   }

   public Grid getGrid() {
      return grid;
   }

   public Moveable getMoveable() {
      return moveable;
   }

   public Position getMoveablePosBefore() {
      return moveablePosBefore;
   }
}
