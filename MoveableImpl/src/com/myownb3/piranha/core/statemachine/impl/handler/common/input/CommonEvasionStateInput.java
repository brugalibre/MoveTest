package com.myownb3.piranha.core.statemachine.impl.handler.common.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.handler.input.EventStateInput;

public class CommonEvasionStateInput implements EventStateInput {

   private DetectableMoveableHelper helper;
   private Moveable moveable;
   private Position moveablePosBefore;

   protected CommonEvasionStateInput(Moveable moveable, DetectableMoveableHelper helper) {
      this.moveable = requireNonNull(moveable);
      this.helper = requireNonNull(helper);
      moveablePosBefore = moveable.getPosition();
   }

   public DetectableMoveableHelper getHelper() {
      return helper;
   }

   public Moveable getMoveable() {
      return moveable;
   }

   public Position getMoveablePosBefore() {
      return moveablePosBefore;
   }
}
