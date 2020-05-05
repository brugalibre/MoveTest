package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class PostEvasionUtilTest {

   @Test
   void testGetAngle2Turn_CalcedAngleGreaterAndNegative() {

      // Given
      double calcedAngle = -40;
      double minEvasionAngle = 5;
      double expectedAngle = -minEvasionAngle;

      // When
      double actualAngle2Turn = PostEvasionUtil.getAngle2Turn(calcedAngle, minEvasionAngle);

      // Then
      assertThat(actualAngle2Turn, is(expectedAngle));
   }

   @Test
   void testGetAngle2Turn_CalcedAngleGreaterAndPositive() {

      // Given
      double calcedAngle = 40;
      double minEvasionAngle = 5;
      double expectedAngle = minEvasionAngle;

      // When
      double actualAngle2Turn = PostEvasionUtil.getAngle2Turn(calcedAngle, minEvasionAngle);

      // Then
      assertThat(actualAngle2Turn, is(expectedAngle));
   }

   @Test
   void testGetAngle2Turn_CalcedAngleSmallerThanMin() {

      // Given
      double calcedAngle = -4;
      double expectedAngle = calcedAngle;
      double minEvasionAngle = 5;

      // When
      double actualAngle2Turn = PostEvasionUtil.getAngle2Turn(calcedAngle, minEvasionAngle);

      // Then
      assertThat(actualAngle2Turn, is(expectedAngle));
   }
}
