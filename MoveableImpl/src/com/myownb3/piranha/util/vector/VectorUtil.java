/**
 * 
 */
package com.myownb3.piranha.util.vector;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * Contains some useful methods for creating {@link Float64Vector} from
 * {@link Position} or a {@link Direction}
 * 
 * @author Dominic
 *
 */
public class VectorUtil {

   private VectorUtil() {
      // private
   }

   /**
    * Creates a unit vector from the given {@link Float64Vector}
    * 
    * @param vector
    *        the given vector
    * @return a unit vector from the given {@link Float64Vector}
    */
   public static Float64Vector getUnitVector(Float64Vector vector) {
      double normValue = vector.normValue();
      double xAxisValue = vector.get(0).doubleValue();
      double yAxisValue = vector.get(1).doubleValue();
      return Float64Vector.valueOf(xAxisValue / normValue, yAxisValue / normValue, 0);
   }

   /**
    * Creates a 3-dimensional {@link Float64Vector} for the given {@link Position} and its x,-
    * and y-coordinates
    * 
    * @param pos
    *        the {@link Position}
    * @return a {@link Float64Vector}
    */
   public static Float64Vector getVector(Position pos) {
      return Float64Vector.valueOf(pos.getX(), pos.getY(), 0);
   }

   /**
    * Creates a 3-dimensional {@link Float64Vector} for the given {@link Position} and its x,-
    * and y-forwarding coordinates
    * 
    * @param direction
    *        the {@link Direction}
    * @return a {@link Float64Vector}
    * 
    * @see Direction#getForwardX()
    * @see Direction#getForwardY()
    */
   public static Float64Vector getVector(Direction direction) {
      return Float64Vector.valueOf(direction.getForwardX(), direction.getForwardY(), 0);
   }

   /**
    * Creates a new 2-dimensional {@link Float64Vector} for the given input values
    * 
    * @param x
    *        the x-value
    * @param y
    *        the y-value
    * @return a new 2-dimensional {@link Float64Vector}
    */
   public static Float64Vector createVector(double x, double y) {
      return Float64Vector.valueOf(x, y);
   }
}
