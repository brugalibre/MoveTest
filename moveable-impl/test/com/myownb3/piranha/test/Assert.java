/**
 * 
 */
package com.myownb3.piranha.test;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class Assert {

   private Assert() {
      // privatos
   }

   /**
    * @param actualPos
    * @param matcher
    * @param decimalPlaces
    */
   public static void assertThatPosition(Position actualPos, Matcher<? super Position> matcher, int decimalPlaces) {
      double roundedX = MathUtil.round(actualPos.getX(), decimalPlaces);
      double roundedY = MathUtil.round(actualPos.getY(), decimalPlaces);
      Position value2BeTested = Positions.of(actualPos.getDirection(), roundedX, roundedY, actualPos.getZ());
      MatcherAssert.assertThat(value2BeTested, matcher);
   }
}
