package com.myownb3.piranha.statemachine.impl.handler.defaultstate.input;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;

public class DefaultStateInput extends CommonEventStateInput {

   private Position endPos;

   private DefaultStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper, Position endPos) {
      super(grid, moveable, helper);
      this.endPos = endPos;
   }

   /**
    * Creates a new {@link DefaultStateInput}
    * 
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the {@link Moveable}
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param endPos
    *        the end -{@link Position} of the {@link Moveable}
    * @return a new {@link DefaultStateInput}
    */
   public static DefaultStateInput of(Grid grid, Moveable moveable, DetectableMoveableHelper helper, Position endPos) {
      return new DefaultStateInput(grid, moveable, helper, endPos);
   }

   public final Position getEndPos() {
      return this.endPos;
   }
}
