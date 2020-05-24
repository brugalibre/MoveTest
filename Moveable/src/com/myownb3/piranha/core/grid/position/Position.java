/**
 * 
 */
package com.myownb3.piranha.core.grid.position;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;

/**
 * Defines a specific position on a {@link Grid}
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
    * @return the y-axis value
    */
   double getY();

   /**
    * @return the x-axis value
    */
   double getX();

   /**
    * Calculates the distance between this and the given {@link Position}
    * 
    * @param position
    * @return the calculated distance
    */
   double calcDistanceTo(Position position);

   /**
    * @return the angle of this Position (not its direction) relatively to the
    *         given coordinate system or {@link Grid}
    */
   double calcAbsolutAngle();

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
    * @return a {@link Float64Vector} of the x- and y coordinates of this {@link Position}. The z-coordinate is always 0
    */
   Float64Vector getVector();

}
