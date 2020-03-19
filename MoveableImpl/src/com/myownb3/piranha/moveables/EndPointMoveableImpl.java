/**
 * 
 */
package com.myownb3.piranha.moveables;

import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.MathUtil.round;
import static java.util.Objects.requireNonNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.util.vector.VectorUtil;

/**
 * @author Dominic
 *
 */
public class EndPointMoveableImpl extends AbstractMoveable implements EndPointMoveable {

   private static double DISTANCE_PRECISION = 0.001;
   private Position endPos;
   private int movingIncrement;
   private double prevDistance;

   EndPointMoveableImpl(Grid grid, Position position, MoveablePostActionHandler handler, Position endPosition,
         int movingIncrement, Shape shape) {
      super(grid, position, handler, shape);
      this.movingIncrement = movingIncrement;
      this.endPos = endPosition;
   }

   public EndPointMoveableImpl(Grid grid, Position position, MoveablePostActionHandler handler, Position endPosition,
         int movingIncrement) {
      super(grid, position, handler);
      this.movingIncrement = movingIncrement;
      this.endPos = endPosition;
   }

   @Override
   public void setEndPosition(Position position) {
      this.endPos = requireNonNull(position, "End-pos must not be null!");
      if (handler instanceof EvasionStateMachine) {
         ((EvasionStateMachine) handler).setEndPosition(endPos);
      }
   }

   @Override
   public void prepare() {
      double diffAngle = position.calcAngleRelativeTo(endPos);
      makeTurn(diffAngle);
      prevDistance = endPos.calcDistanceTo(position);
   }

   @Override
   public MoveResult moveForward2EndPos() {
      double distance = endPos.calcDistanceTo(position);
      if (distance >= getSmallestStepWith()) {
         moveForward(movingIncrement);
         distance = endPos.calcDistanceTo(position);
         if (isDone(distance)) {
            return new MoveResultImpl(distance, prevDistance, true);
         }
         prevDistance = distance;
         return new MoveResultImpl(distance, prevDistance);
      }
      return new MoveResultImpl(distance, prevDistance, true);
   }

   /*
    * We are done, when we - a) reach the destination - b) have already reached the
    * destination and now we are getting further away again
    */
   private boolean isDone(double distance) {
      return hasReachedEndPos(distance) || isBeyond(distance);
   }

   private boolean isBeyond(double distance) {
      return round(distance, 1) > round(prevDistance, 1) && isPositionOnLine();
   }

   private boolean hasReachedEndPos(double distance) {
      return distance <= DISTANCE_PRECISION && distance >= -DISTANCE_PRECISION;
   }

   private boolean isPositionOnLine() {
      Float64Vector lineFromOldToNew = VectorUtil.getVector(position.getDirection());
      double avoidableDistanceToLine = round(calcDistanceFromPositionToLine(endPos, position, lineFromOldToNew), 10);
      return avoidableDistanceToLine == 0.0d;
   }
}
