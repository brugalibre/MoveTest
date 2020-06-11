package com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.input.CommonEvasionStateInput;

public class ReturningEventStateInput extends CommonEvasionStateInput {

   private Position positionBeforeEvasion;
   private EndPosition endPosition;

   private ReturningEventStateInput(DetectableMoveableHelper helper, Position positionBeforeEvasion, Moveable moveable,
         EndPosition endPosition) {
      super(moveable, helper);
      this.positionBeforeEvasion = requireNonNull(positionBeforeEvasion);
      this.endPosition = endPosition;
   }

   /**
    * Creates a new {@link ReturningEventStateInput}
    * 
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param moveable
    *        the {@link Moveable}
    * @param positionBeforeEvasion
    *        the {@link Position} when the {@link Detector}
    *        detected an evasion
    * @param endPosition
    *        the {@link Position} the {@link Moveable} is
    *        heading to
    * @return a new {@link ReturningEventStateInput}
    */
   public static ReturningEventStateInput of(DetectableMoveableHelper helper, Moveable moveable, Position positionBeforeEvasion,
         EndPosition endPosition) {
      return new ReturningEventStateInput(helper, positionBeforeEvasion, moveable, endPosition);
   }

   /**
    * Creates a new {@link ReturningEventStateInput} without an end position
    * 
    * @param helper
    *        the {@link DetectableMoveableHelper}
    * @param moveable
    *        the {@link Moveable}
    * @param positionBeforeEvasion
    *        the {@link Position} when the {@link Detector}
    *        detected an evasion
    * @return a new {@link ReturningEventStateInput}
    */
   public static ReturningEventStateInput of(DetectableMoveableHelper helper, Moveable moveable, Position positionBeforeEvasion) {
      return new ReturningEventStateInput(helper, positionBeforeEvasion, moveable, null);
   }

   public final Position getPositionBeforeEvasion() {
      return this.positionBeforeEvasion;
   }

   public final EndPosition getEndPosition() {
      return this.endPosition;
   }
}
