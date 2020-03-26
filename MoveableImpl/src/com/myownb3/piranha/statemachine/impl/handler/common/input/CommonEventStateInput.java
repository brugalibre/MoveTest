package com.myownb3.piranha.statemachine.impl.handler.common.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.input.EventStateInput;

public class CommonEventStateInput implements EventStateInput {

   private Grid grid;
   private DetectableMoveableHelper helper;
   private Moveable moveable;
   private Position moveablePosBefore;

   protected CommonEventStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper) {
      this.grid = requireNonNull(grid);
      this.moveable = requireNonNull(moveable);
      this.helper = requireNonNull(helper);
      moveablePosBefore = Positions.of(moveable.getPosition());
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
