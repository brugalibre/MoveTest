package com.myownb3.piranha.core.statemachine.impl.handler.defaultstate.input;

import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.input.CommonEvasionStateInput;

public class DefaultStateInput extends CommonEvasionStateInput {

   private EndPosition endPos;

   private DefaultStateInput(Moveable moveable, DetectableMoveableHelper helper, EndPosition endPos) {
      super(moveable, helper);
      this.endPos = endPos;
   }

   /**
    * Creates a new {@link DefaultStateInput}
    * @param moveable
    *        the {@link Moveable}
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param endPos
    *        the end -{@link Position} of the {@link Moveable}
    * 
    * @return a new {@link DefaultStateInput}
    */
   public static DefaultStateInput of(Moveable moveable, DetectableMoveableHelper helper, EndPosition endPos) {
      return new DefaultStateInput(moveable, helper, endPos);
   }

   public final EndPosition getEndPos() {
      return this.endPos;
   }
}
