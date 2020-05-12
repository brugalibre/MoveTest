/**
 * 
 */
package com.myownb3.piranha.util.vector;

import static com.myownb3.piranha.util.MathUtil.round;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.position.Position;

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
    * Rotates the given {@link Float64Vector} for the given angle
    * 
    * @param vector
    *        the vector to rotate
    * @param angle
    *        the angle
    * @return a new instance of the rotated {@link Float64Vector}
    */
   public static Float64Vector rotateVector(Float64Vector vector, double angleAsDeg) {
      double angle = toRadians(angleAsDeg);
      double x1 = vector.getValue(0);
      double y1 = vector.getValue(1);
      double x2 = (cos(angle) * x1) - (sin(angle) * y1);
      double y2 = (sin(angle) * x1) + (cos(angle) * y1);
      return Float64Vector.valueOf(round(x2, 10), round(y2, 10), 0.0);
   }

   /**
    * 
    * @param vector
    *        the given {@link Float64Vector} whose normal is returned
    * @return the normal vector of the given one
    */
   public static Float64Vector getNormal(Float64Vector vector) {
      return rotateVector(vector, -90.0);
   }
}
