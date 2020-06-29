/**
 * 
 */
package com.myownb3.piranha.util;

import static java.lang.Math.toDegrees;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.core.grid.position.Position;

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
      return Math.round(value * factor) / factor;
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

      Float64Vector posQVector = posQ.getVector();
      Float64Vector posPVector = posPOnVector.getVector();
      return a.cross(posQVector.minus(posPVector)).normValue() / a.normValue();
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

   /**
    * Returns the signum of the given value. If the value is greater 0 then 1 is returned.
    * If it is smaller than 0 then -1 is returned. If it is equal to 0, then 0 is returned
    * 
    * @param angleRelativeTo
    * @return the signum of the given value
    */
   public static int getSignum(double value) {
      if (value > 0) {
         return 1;
      } else if (value < 0) {
         return -1;
      }
      return 0;
   }
}
