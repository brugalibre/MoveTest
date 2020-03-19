/**
 * 
 */
package com.myownb3.piranha.test;

import org.hamcrest.Matcher;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class Assert {

   /**
    * @param actualPos
    * @param matcher
    * @param decimalPlaces
    */
   public static void assertThatPosition(Position actualPos, Matcher<? super Position> matcher, int decimalPlaces) {
      double roundedX = MathUtil.round(actualPos.getX(), decimalPlaces);
      double roundedY = MathUtil.round(actualPos.getY(), decimalPlaces);
      Position value2BeTested = Positions.of(actualPos.getDirection(), roundedX, roundedY);
      org.junit.Assert.assertThat(value2BeTested, matcher);
   }
}
