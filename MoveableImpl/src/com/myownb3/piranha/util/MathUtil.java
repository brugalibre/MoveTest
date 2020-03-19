/**
 * 
 */
package com.myownb3.piranha.util;

import static com.myownb3.piranha.util.vector.VectorUtil.getVector;
import static java.lang.Math.toDegrees;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
public class MathUtil {

   private MathUtil() {
      // private
   }

   public static double round(double value, int places) {

      if (places < 0 || places > 30) {
         throw new IllegalArgumentException("The amount of decimal places must be between 0 and 30!");
      }
      double factor = calcFactor(places);
      return (double) Math.round(value * factor) / factor;
   }

   private static double calcFactor(int places) {
      double factor = 1;

      for (int i = 1; i <= places; i++) {
         factor = factor * 10;
      }
      return factor;
   }

   public static double roundThreePlaces(double value) {
      return round(value, 3);
   }

   /**
    * Returns a random number considering the given offset
    * 
    * @param offset
    *        the offset
    * @return a random number
    */
   public static double getRandom(int offset) {
      return Math.random() * offset;
   }

   /**
    * Calculates the (ortogonal) distance from {@link Position} Q to the line which
    * is created by the {@link Position} P and the vector a
    * 
    * @param posQ
    *        the {@link Position} Q
    * @param posPOnVector
    *        the {@link Position} P placed on the vector
    * @param a
    *        the vector itself
    * @return the distance between the point Q and the line 'PosQ' and vector
    */
   public static double calcDistanceFromPositionToLine(Position posQ, Position posPOnVector, Float64Vector a) {

      Float64Vector posQVector = getVector(posQ);
      Float64Vector posPVector = getVector(posPOnVector);
      return a.cross(posQVector.minus(posPVector)).normValue() / a.normValue();
   }

   /**
    * Calculates the angle between the two Vectors which can be created between the
    * given {@link Position}s. The direection of the {@link Moveable}s position is
    * considered.
    * 
    * @param moveablePosition
    *        the position from a {@link Moveable}
    * @param gridElementPos
    *        the position of a {@link GridElement} on a
    *        {@link Grid}
    * @return the calculated angle with a precision of three decimal places
    */
   public static double calcAngleBetweenPositions(Position moveablePosition, Position gridElementPos) {
      Float64Vector moveable2GridElemVector = getVectorFromMoveable2GridElement(moveablePosition, gridElementPos);
      Float64Vector moveableDirectionVector = getVector(moveablePosition.getDirection());
      return calcAngleBetweenVectors(moveable2GridElemVector, moveableDirectionVector);
   }

   /**
    * Calculates the angle between the two vectors
    * 
    * @param vector1
    *        the first vector
    * @param vector2
    *        the second vector
    * @return the angle in degree between thos two vectors
    */
   public static double calcAngleBetweenVectors(Float64Vector vector1, Float64Vector vector2) {
      double v1TimesV2 = vector2.times(vector1).doubleValue();
      return calcAngleBetweenVectors(v1TimesV2, vector1.normValue(), vector2.normValue());
   }

   private static double calcAngleBetweenVectors(double moveableVectorTimesGridElemVector,
         double moveable2GridElemVectorLenght, double moveableVectorLenght) {
      double vectorAngle = moveableVectorTimesGridElemVector / (moveableVectorLenght * moveable2GridElemVectorLenght);
      double radValue = Math.acos(Math.min(vectorAngle, 1));
      return toDegrees(radValue);
   }

   private static Float64Vector getVectorFromMoveable2GridElement(Position moveablePosition, Position gridElemPos) {
      Float64Vector moveableVector = getVector(moveablePosition);
      Float64Vector gridElemVector = getVector(gridElemPos);
      return gridElemVector.minus(moveableVector);
   }
}
