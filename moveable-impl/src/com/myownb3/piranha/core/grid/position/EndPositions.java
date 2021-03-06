package com.myownb3.piranha.core.grid.position;

import static com.myownb3.piranha.util.MathUtil.round;

import java.util.HashMap;
import java.util.Map;

import com.myownb3.piranha.core.grid.position.Positions.PositionImpl;
import com.myownb3.piranha.core.moveables.Moveable;

public class EndPositions {

   private EndPositions() {
      // private
   }

   /**
    * Creates a new {@link EndPosition} with the given coordinates
    * 
    * @param x
    *        the x-axis coordinate
    * @param y
    *        the y-axis coordinate
    * @return a new created {@link EndPosition}
    */
   public static EndPosition of(double x, double y) {
      return new EndPositionImpl(x, y);
   }

   /**
    * Creates a new {@link EndPosition} with the given coordinates
    * 
    * @param x
    *        the x-axis coordinate
    * @param y
    *        the y-axis coordinate
    * @return a new created {@link EndPosition}
    */
   public static EndPosition of(double x, double y, boolean adHocVerification) {
      return new EndPositionImpl(x, y, adHocVerification);
   }

   /**
    * 
    * Creates a new {@link EndPosition} with the given coordinates
    * 
    * @param position
    *        the {@link Position} to create a new {@link EndPosition} of
    * @param distancePrecision
    *        defines how exact a {@link Moveable} must match the coordinates of this {@link EndPosition} in order to 'reach' it
    * @return a new created {@link EndPosition}
    */

   public static EndPosition of(Position position, double endPositionPrecision) {
      return of(position.getX(), position.getY(), endPositionPrecision);
   }

   /**
    * 
    * Creates a new {@link EndPosition} with the given coordinates
    * 
    * @param x
    *        the x-axis coordinate
    * @param y
    *        the y-axis coordinate
    * @param distancePrecision
    *        defines how exact a {@link Moveable} must match the coordinates of this {@link EndPosition} in order to 'reach' it
    * @return a new created {@link EndPosition}
    */

   public static EndPosition of(double x, double y, double endPositionPrecision) {
      return new EndPositionImpl(x, y, endPositionPrecision);
   }

   /**
    * Creates a new {@link EndPosition} of the given {@link Position}
    * 
    * @param position
    *        the given {@link Position}
    * @return a new {@link EndPosition} of the given {@link Position}
    */
   public static EndPosition of(Position position) {
      return of(position.getX(), position.getY());
   }

   private static class EndPositionImpl extends PositionImpl implements EndPosition {

      private static final long serialVersionUID = 1L;
      private double distancePrecision = 0.001;
      private transient Map<Moveable, Boolean> moveable2HasReachedMap;
      private boolean adHocVerification;// Defines if the 'hasReached' method does a fresh calculation or just return a previously calculated value

      private EndPositionImpl(double x, double y) {
         super(x, y, 0.0);
         moveable2HasReachedMap = new HashMap<>();
         adHocVerification = false;
         distancePrecision = 0.001;
      }

      private EndPositionImpl(double x, double y, boolean adHocVerification) {
         this(x, y);
         this.adHocVerification = adHocVerification;
      }

      private EndPositionImpl(double x, double y, double distancePrecision) {
         this(x, y);
         this.distancePrecision = distancePrecision;
      }

      @Override
      public boolean hasReached(Moveable moveable) {
         if (adHocVerification) {
            checkIfHasReached(moveable);
         }
         return moveable2HasReachedMap.containsKey(moveable) && moveable2HasReachedMap.get(moveable);
      }

      /*
       * We are done, when we - a) reach the destination - b) have already reached the
       * destination and now we are getting further away again
       */
      @Override
      public boolean checkIfHasReached(Moveable moveable) {
         double distance = this.calcDistanceTo(moveable.getPosition());
         boolean isDone = hasReachedEndPos(distance) || isBeyond(distance, moveable.getPositionBefore(), moveable.getPosition());
         moveable2HasReachedMap.put(moveable, isDone);
         return isDone;
      }

      private boolean isBeyond(double distance2EndPos, Position posBefore, Position currentPos) {
         double movedDistance = round(posBefore.calcDistanceTo(currentPos), 10);
         return movedDistance > distance2EndPos && isPositionOnLine(posBefore, currentPos, movedDistance);
      }

      private boolean hasReachedEndPos(double distance) {
         return distance <= distancePrecision && distance >= -distancePrecision;
      }

      private boolean isPositionOnLine(Position currentPos, Position posBefore, double distancePosBefore2CurrentPos) {
         double distancePosBefore2EndPos = posBefore.calcDistanceTo(this);
         double distanceCurrentPosEndPos = currentPos.calcDistanceTo(this);
         return round(distancePosBefore2CurrentPos, 10) <= round(distancePosBefore2EndPos + distanceCurrentPosEndPos, 10);
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         int result = super.hashCode();
         result = prime * result + (adHocVerification ? 1231 : 1237);
         long temp;
         temp = Double.doubleToLongBits(distancePrecision);
         result = prime * result + (int) (temp ^ (temp >>> 32));
         return result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (!super.equals(obj))
            return false;
         EndPositionImpl other = (EndPositionImpl) obj;
         if (adHocVerification != other.adHocVerification)
            return false;
         return (Double.doubleToLongBits(distancePrecision) == Double.doubleToLongBits(other.distancePrecision));
      }
   }
}
