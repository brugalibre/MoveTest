/**
 * 
 */
package com.myownb3.piranha.util;

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
   public static double getRandom(double offset) {
      return Math.random() * offset;
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
