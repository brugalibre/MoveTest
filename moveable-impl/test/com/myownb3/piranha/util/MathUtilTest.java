/**
 * 
 */
package com.myownb3.piranha.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author Dominic
 *
 */
class MathUtilTest {

   @Test
   void testSignum() {

      // Given
      int positiv = 10;
      int zero = 0;
      int negativ = -10;

      // When
      int signumOfNegativ = MathUtil.getSignum(negativ);
      int signumOfZero = MathUtil.getSignum(zero);
      int signumOfPositiv = MathUtil.getSignum(positiv);

      // Then
      assertThat(signumOfNegativ, is(-1));
      assertThat(signumOfZero, is(0));
      assertThat(signumOfPositiv, is(1));
   }

   @Test
   void testRoundDown_ToDecimals() {

      // Given
      double pi = 3.1234;
      double expectedRoundedPi = 3.12;

      // When
      double roundetPi = MathUtil.round(pi, 2);

      // Then
      MatcherAssert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
   }

   @Test
   void testRoundDown_zeroDecimals() {

      // Given
      double pi = 3.1234;
      double expectedRoundedPi = 3;

      // When
      double roundetPi = MathUtil.round(pi, 0);

      // Then
      MatcherAssert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
   }

   @Test
   void testRoundUp2() {

      // Given
      double number = -3.53553;
      double expectedRoundedNumber = -3.54;

      // When
      double roundetNumber = MathUtil.round(number, 2);

      // Then
      MatcherAssert.assertThat(roundetNumber, CoreMatchers.is(expectedRoundedNumber));
   }

   @Test
   void testRoundUp() {

      // Given
      double pi = 3.1265;
      double expectedRoundedPi = 3.127;

      // When
      double roundetPi = MathUtil.roundThreePlaces(pi);

      // Then
      MatcherAssert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
   }

   @Test
   void testRoundIllegalDecPlaces() {

      // Given
      double pi = 3.1234;

      // When
      Executable ex = () -> {
         MathUtil.round(pi, -2);
      };
      // Then
      Assertions.assertThrows(IllegalArgumentException.class, ex);
   }

   @Test
   void testRoundToMutchDecPlaces() {

      // Given
      double pi = 3.12341324654153152534;

      // When
      Executable ex = () -> {
         MathUtil.round(pi, 31);
      };
      // Then
      Assertions.assertThrows(IllegalArgumentException.class, ex);
   }
}
