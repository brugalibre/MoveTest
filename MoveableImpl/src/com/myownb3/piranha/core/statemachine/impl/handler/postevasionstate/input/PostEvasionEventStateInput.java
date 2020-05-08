package com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.input.CommonEvasionStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.passingstate.input.PassingEventStateInput;

public class PostEvasionEventStateInput extends CommonEvasionStateInput {

   private Position positionBeforeEvasion;
   private EndPosition endPosition;

   private PostEvasionEventStateInput(Grid grid, DetectableMoveableHelper helper, Moveable moveable,
         Position positionBeforeEvasion, EndPosition endPosition) {
      super(grid, moveable, helper);
      this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
      this.endPosition = endPosition;
   }

   /**
    * Creates a new {@link PassingEventStateInput}
    * 
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the {@link Moveable}
    * @param positionBeforeEvasion
    *        the {@link Position} the moveable had before the
    *        evasion
    * @param endPosition
    *        the end-position
    * @return a {@link PassingEventStateInput}
    */
   public static PostEvasionEventStateInput of(DetectableMoveableHelper helper, Grid grid, Moveable moveable,
         Position positionBeforeEvasion, EndPosition endPosition) {
      return new PostEvasionEventStateInput(grid, helper, moveable, positionBeforeEvasion, endPosition);
   }

   /**
    * Creates a new {@link PassingEventStateInput} without an end-position
    * 
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param grid
    *        the {@link Grid}
    * @param moveable
    *        the {@link Moveable}
    * @param positionBeforeEvasion
    *        the {@link Position} the moveable had before the
    *        evasion
    * @return a {@link PassingEventStateInput}
    */
   public static PostEvasionEventStateInput of(DetectableMoveableHelper helper, Grid grid, Moveable moveable,
         Position positionBeforeEvasion) {
      return new PostEvasionEventStateInput(grid, helper, moveable, positionBeforeEvasion, null);
   }

   public final Position getPositionBeforeEvasion() {
      return this.positionBeforeEvasion;
   }

   public final EndPosition getEndPosition() {
      return this.endPosition;
   }
}
