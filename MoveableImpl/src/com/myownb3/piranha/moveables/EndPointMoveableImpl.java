/**
 * 
 */
package com.myownb3.piranha.moveables;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;

/**
 * @author Dominic
 *
 */
public class EndPointMoveableImpl extends AbstractMoveable implements EndPointMoveable {

   private EndPosition endPos;
   private int movingIncrement;
   private double prevDistance;

   EndPointMoveableImpl(Grid grid, Position position, MoveablePostActionHandler handler, int movingIncrement,
         Shape shape) {
      super(grid, position, handler, shape);
      this.movingIncrement = movingIncrement;
   }

   public EndPointMoveableImpl(Grid grid, Position position, MoveablePostActionHandler handler, int movingIncrement) {
      super(grid, position, handler);
      this.movingIncrement = movingIncrement;
   }

   @Override
   public void setEndPosition(EndPosition endPos) {
      this.endPos = requireNonNull(endPos, "End-pos must not be null!");
      prevDistance = endPos.calcDistanceTo(position);
      if (handler instanceof EvasionStateMachine) {
         ((EvasionStateMachine) handler).setEndPosition(endPos);
      }
   }

   @Override
   public MoveResult moveForward2EndPos() {
      double distance = endPos.calcDistanceTo(position);
      if (distance >= getSmallestStepWith()) {
         moveForward(movingIncrement);
         if (endPos.checkIfHasReached(this)) {
            return new MoveResultImpl(distance, prevDistance, true);
         }
         prevDistance = distance;
         return new MoveResultImpl(distance, prevDistance);
      }
      return new MoveResultImpl(distance, prevDistance, true);
   }

   @Override
   public EndPosition getCurrentEndPos() {
      return endPos;
   }
}
