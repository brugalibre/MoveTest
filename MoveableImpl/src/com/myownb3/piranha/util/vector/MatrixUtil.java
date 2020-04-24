package com.myownb3.piranha.util.vector;

import org.jscience.mathematics.vector.DenseMatrix;
import org.jscience.mathematics.vector.Float64Matrix;
import org.jscience.mathematics.vector.Matrix;

import com.myownb3.piranha.grid.gridelement.position.Position;

/**
 * Provides methods for handling or creating a {@link Matrix}
 * 
 * @author Dominic
 *
 */
public class MatrixUtil {

   private MatrixUtil() {
      // private
   }

   /**
    * Creates a new {@link Float64Matrix} with the given two {@link Position}s
    * 
    * @param posOne
    *        the first {@link Position}
    * @param posTwo
    *        the second {@link Position}
    * @return a new {@link DenseMatrix}
    */
   public static Float64Matrix createMatrix4Positions(Position posOne, Position posTwo) {
      return Float64Matrix.valueOf(new double[][] {
            {posOne.getX(), posTwo.getX()},
            {posOne.getY(), posTwo.getY()}
      });
   }
}
