package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import static java.lang.Math.abs;

public class PostEvasionUtil {

   private PostEvasionUtil() {
      // private
   }

   public static double getAngle2Turn(double calcEvasionAngle, double minEvasionAngle) {
      if (abs(calcEvasionAngle) > minEvasionAngle) {
         return calcEvasionAngle > 0 ? minEvasionAngle : -minEvasionAngle;
      }
      return calcEvasionAngle;
   }
}
