/**
 * 
 */
package com.myownb3.piranha.core.grid.position;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;

/**
 * Defines a two dimensional point on a {@link Grid}. Although a {@link Position} defines a z-axis, this value is only used to determine
 * the distance from the ground (e.g. Grid)
 * 
 * @author Dominic
 *
 */
public interface Position {

   /**
    * Rotate this {@link Position} for the given amount of degrees
    * 
    * @return a rotated instance of this {@link Position}
    * @param degree
    */
   Position rotate(double degree);

   /**
    * @return the Direction in which this Positions shows
    */
   Direction getDirection();

   /**
    * Calculates the distance between this and the given {@link Position}
    * 
    * @param position
    * @return the calculated distance
    */
   double calcDistanceTo(Position position);

   /**
    * Calculates the angle between this point relatively to their coordinates on
    * the {@link Grid}
    * 
    * @param position
    *        the position
    * @return the relatively angle between this and the other position
    */
   double calcAngleRelativeTo(Position position);

   /**
    * The this {@link Position} is moved forward one time
    * 
    * @return a new {@link Position}
    */
   public Position movePositionForward();

   /**
    * This {@link Position} is moved forward for the given amount
    * 
    * @param amount
    *        the amount of times it is moved forward
    * @return a new {@link Position}
    */
   public Position movePositionForward(int amount);

   /**
    * Creates a new {@link Position} by moving this position for the given distance
    * 
    * @param distance
    *        the distance
    * 
    * @return a new {@link Position} by moving the given position for the given distance
    */
   public Position movePositionForward4Distance(double distance);

   /**
    * Creates a new {@link Position} by moving this position backward for the given distance
    * 
    * @param distance
    *        the distance
    * 
    * @return a new {@link Position} by moving the given position for the given distance
    */
   public Position movePositionBackward4Distance(double distance);

   /**
    * @return the z-axis value
    */
   double getZ();

   /**
    * @return the y-axis value
    */
   double getY();

   /**
    * @return the x-axis value
    */
   double getX();

   /**
    * @return a {@link Float64Vector} of the x- and y coordinates of this {@link Position}. The z-coordinate is always 0
    */
   Float64Vector getVector();

}
