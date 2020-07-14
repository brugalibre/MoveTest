/**
 * 
 */
package com.myownb3.piranha.core.grid.direction;

import java.io.Serializable;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link Direction} indicates the orientation on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Direction extends Serializable {

   /**
    * @return value on the y-axis by which {@link Position} can be moved backward
    */
   double getBackwardY();

   /**
    * @return value on the x-axis by which {@link Position} can be moved backward
    */
   double getBackwardX();

   /**
    * @return value on the y-axis by which {@link Position} can be moved forward
    */
   double getForwardY();

   /**
    * @return value on the x-axis by which {@link Position} can be moved forward
    */
   double getForwardX();

   /**
    * @return a 2-dimensional direction {@link Float64Vector} of this {@link Direction}.
    */
   Float64Vector getVector();

   /**
    * Rotate this Direction for the given amount of degrees
    * 
    * @param degree
    * @return a new and turned instance of a {@link Direction}
    */
   Direction rotate(double degree);

   /**
    * @return the angle of this Direction
    */
   double getAngle();

   /**
    * @returns the cardinal direction of this {@link Direction}
    */
   String getCardinalDirection();
}
