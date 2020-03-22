package com.myownb3.piranha.statemachine.impl.handler.orientatingstate.input;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;

public class OrientatingStateInput extends CommonEventStateInput {

   private Position endPos;

   private OrientatingStateInput(Grid grid, Moveable moveable, DetectableMoveableHelper helper, Position endPos) {
      super(grid, moveable, helper);
      this.endPos = endPos;
   }

   /**
    * Creates a new {@link OrientatingStateInput}
    * 
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the {@link Moveable}
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param endPos
    *        the end-Position to folow
    * @return a new {@link OrientatingStateInput}
    */
   public static OrientatingStateInput of(Grid grid, Moveable moveable, DetectableMoveableHelper helper, Position endPos) {
      return new OrientatingStateInput(grid, moveable, helper, endPos);
   }

   public final Position getEndPos() {
      return this.endPos;
   }
}
