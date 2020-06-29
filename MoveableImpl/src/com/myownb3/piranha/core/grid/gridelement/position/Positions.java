/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.position;

import static com.myownb3.piranha.util.MathUtil.round;

import java.util.List;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.util.MathUtil;
import com.myownb3.piranha.util.attribute.LazyAttribute;

/**
 * @author Dominic
 *
 */
public class Positions {

   private static final PositionHelper POSITION_HELPER = new PositionHelper();

   private Positions() {
      // Do not instantiate
   }

   /**
    * Creates a new {@link Position} with the given coordinates
    * 
    * @param x
    *        the x-axis coordinate
    * @param y
    *        the y-axis coordinate
    * @return a new created Position
    */
   public static Position of(double x, double y) {
      return new PositionImpl(x, y, 0.0);
   }

   /**
    * Creates a new {@link Position} with the given coordinates
    * 
    * @param x
    *        the x-axis coordinate
    * @param y
    *        the y-axis coordinate
    * @param z
    *        the x-axis coordinate
    * @return a new created Position
    */
   public static Position of(double x, double y, double z) {
      return new PositionImpl(x, y, z);
   }

   /**
    * Creates a new {@link Position} with the given coordinates
    * 
    * @param direction
    *        the {@link Direction} of th new {@link Position} to create
    * @param x
    *        the x-axis coordinate
    * @param y
    *        the y-axis coordinate
    * @param z
    *        the x-axis coordinate
    * @return a new created Position
    */
   public static Position of(Direction direction, double x, double y, double z) {
      return new PositionImpl(direction, x, y, z);
   }

   /**
    * Creates a new {@link Position} with the coordinates from the given {@link Position}
    * 
    * @param direction
    *        the {@link Direction} of th new {@link Position} to create
    * @param position
    *        the {@link Position} which coordinates are used for the new Position
    *        the x-axis coordinate
    * @return a new created Position
    */
   public static Position of(Direction direction, Position position) {
      return of(direction, position.getX(), position.getY(), position.getZ());
   }

   /**
    * @param vector
    *        the vector
    * @param decimals
    *        the amount of decimal places for x- and y axis values
    * @return a new {@link Position} for the given {@link Float64Vector}
    * 
    */
   public static Position of(Float64Vector vector, int decimals) {
      return new PositionImpl(round(vector.getValue(0), decimals), round(vector.getValue(1), decimals), 0.0);
   }

   /**
    * Creates a copy of the given {@link Position}
    * 
    * @param position
    * @return a copy of the given {@link Position}
    */
   public static Position of(Position position) {
      Direction direction = Directions.of(position.getDirection());
      return Positions.of(direction, position.getX(), position.getY(), position.getZ());
   }

   /**
    * Builds a {@link List} with {@link Position} which are placed between the two given {@link Position}s
    * 
    * @param pos1
    *        the start {@link Position}
    * @param pos2
    *        the end {@link Position}
    * @param distanceBetweenPosOnColDetectionPath
    *        the minimal distance between two Positions
    * @return
    */
   public static List<Position> buildPositionsBetweenTwoPositions(Position pos1, Position pos2, double distanceBetweenPosOnColDetectionPath) {
      return POSITION_HELPER.buildPositionsBetweenTwoPositions(pos1, pos2, distanceBetweenPosOnColDetectionPath);
   }

   /**
    * Returns a new {@link PositionImpl} within the borders of the given
    * {@link Dimension} considering the given height and width of the Position
    * 
    * @param dimension
    * @param height
    * @param width
    * @return a new created Position
    */
   public static Position getRandomPosition(Dimension dimension, int height, int width) {

      int minX = dimension.getX();
      int maxX = minX + dimension.getWidth();
      int minY = dimension.getY();
      int maxY = minX + dimension.getHeight();

      int x = (int) Math.min(MathUtil.getRandom(maxX) + minX, maxX - width / 2);
      int y = (int) Math.min(MathUtil.getRandom(maxY) + minY, maxY - height / 2);

      return Positions.of(x, y);
   }

   public static class PositionImpl implements Position {

      private double y;
      private double x;
      private double z;
      private Direction direction;
      private LazyAttribute<Float64Vector> vector;

      /**
       * Creates a new {@link PositionImpl} with the given coordinates The
       * {@link Direction} is set to {@link Directions#N} by default
       * 
       * @param x
       *        the x axis value
       * @param y
       *        the y axis value
       * @param z
       */
      protected PositionImpl(double x, double y, double z) {
         this(Directions.N, x, y, z);
      }

      /**
       * Creates a new {@link PositionImpl} with the given coordinates and
       * {@link Direction}
       * 
       * @param direction
       *        the desired direction
       * @param x
       *        the x axis value
       * @param y
       *        the y axis value
       */
      private PositionImpl(Direction direction, double x, double y, double z) {
         this.direction = direction;
         this.vector = new LazyAttribute<>(() -> Float64Vector.valueOf(x, y, 0));
         this.x = MathUtil.round(x, 10);
         this.y = MathUtil.round(y, 10);
         this.z = MathUtil.round(z, 10);
      }

      /**
       * @param degree
       */
      @Override
      public Position rotate(double degree) {
         Direction newDirection = direction.rotate(degree);
         return Positions.of(newDirection, x, y, z);
      }

      @Override
      public double calcDistanceTo(Position position) {
         Position distanceVector = Positions.of(position.getX() - x, position.getY() - y);
         return Math.sqrt(
               distanceVector.getX() * distanceVector.getX() + distanceVector.getY() * distanceVector.getY());
      }

      @Override
      public double calcAngleRelativeTo(Position position) {

         double absDeltaX = position.getX() - x;
         double absDeltaY = position.getY() - y;
         PositionImpl distanceVector = (PositionImpl) Positions.of(absDeltaX, absDeltaY);

         return distanceVector.calcAbsoluteAngle() - direction.getAngle();
      }

      /**
       * Returns the angle of the {@link GridElement}
       */
      @Visible4Testing
      double calcAbsoluteAngle() {
         double angleAsRadiant = getAngleAsRadiant();
         double angleAsDegree = Math.toDegrees(angleAsRadiant);

         // x-axis is negative -> absolute value of angle + 90 (since we are looking from
         // the absolute zero point)
         return getAbsolutAngle(angleAsDegree);
      }

      /**
       * @return the current angle of this position as radiant. Special case to
       *         handle: If this position is currently at 0.0, then we return PI/2 or
       *         -PI/2 since a calculation of {@link Math#atan(0/0)} returns a NAN
       * 
       */
      private double getAngleAsRadiant() {
         if (getY() == 0 && getX() == 0) {
            return Math.toRadians(direction.getAngle());
         }
         return Math.atan(getY() / getX());
      }

      @Override
      public Position movePositionForward() {
         return movePositionForward(1);
      }

      @Override
      public Position movePositionForward(int amount) {
         return POSITION_HELPER.movePositionForward(this, amount);
      }

      @Override
      public Position movePositionForward4Distance(double distance) {
         return POSITION_HELPER.movePositionForward4Distance(this, distance);
      }

      @Override
      public Position movePositionBackward4Distance(double distance) {
         return POSITION_HELPER.movePositionBackward4Distance(this, distance);
      }

      /**
       * @return the direction
       */
      @Override
      public Direction getDirection() {
         return this.direction;
      }

      @Override
      public double getZ() {
         return z;
      }

      @Override
      public final double getY() {
         return this.y;
      }

      @Override
      public final double getX() {
         return this.x;
      }

      @Override
      public Float64Vector getVector() {
         return vector.get();
      }

      /*
	 * Calculates the absolute value depending on the quadrant this position is located
	 *
	 * @formatter:off
	 *  ___________
	 * |     |     |
	 * |  2. |  1. |
	 * |____ |_____|
	 * |     |     |
	 * |  3. |  4. |
	 * |_____|_____|
	 * 
	 * @formatter:on
	 */
      private double getAbsolutAngle(double angleAsDegree) {
         return getAbsolutAngle(angleAsDegree, x, y);
      }

      /**
       * Calculates the absolute value depending on the quadrant this position is located
       * 
       * @param angleAsDegree
       *        the angle e.g. -45 degree
       * @return the absolute angle considering the 4 different quadrants, so -45 will be 325
       */
      public static double getAbsolutAngle(double angleAsDegree, double x, double y) {

         if (y < 0 && x < 0) {
            // 3. Quadrant
            angleAsDegree = angleAsDegree + 180;
         } else if (y >= 0 && x < 0) {
            // 2. Quadrant
            angleAsDegree = 180 + angleAsDegree;
         } else if (y < 0) {
            // 4. Quadrant
            angleAsDegree = angleAsDegree + 360;
         }
         return angleAsDegree;
      }

      @Override
      public int hashCode() {
         final int prime = 31;
         double result = 1;
         result = prime * result + this.x;
         result = prime * result + this.y;
         result = prime * result + this.z;
         return (int) result;
      }

      @Override
      public boolean equals(Object obj) {
         if (this == obj)
            return true;
         if (obj == null)
            return false;
         if (getClass() != obj.getClass())
            return false;
         PositionImpl other = (PositionImpl) obj;
         if (this.x != other.x)
            return false;
         if (this.y != other.y)
            return false;
         if (this.z != other.z)
            return false;
         return true;
      }

      @Override
      public String toString() {
         return "Direction: '" + direction + "', X-Axis: '" + x + "', Y-Axis: '" + y + "', Z-Axis: '" + z + "'";
      }
   }
}
