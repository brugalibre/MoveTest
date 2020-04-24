/**
 * 
 */
package com.myownb3.piranha.util;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.jscience.mathematics.vector.Float64Vector;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.util.vector.VectorUtil;

/**
 * @author Dominic
 *
 */
class MathUtilTest {

   @Test
   void testIsPositionOnLevel() {

      // Given
      boolean expectedIsPositionOnLevel = true;
      Position pos2Test = Positions.of(1, 2);
      Position pos1 = Positions.of(5, 6);
      Position pos2 = Positions.of(1, 1);
      Position pos3 = Positions.of(10, 11);

      // When
      boolean actualIsPositionOnLevel = MathUtil.isPositionOnLevel(pos2Test, pos1, pos2, pos3);

      // Then
      assertThat(actualIsPositionOnLevel, is(expectedIsPositionOnLevel));
   }

   @Test
   void testIsPositionNotOnLevel() {

      // Given
      boolean expectedIsPositionOnLevel = false;
      Position pos2Test = Positions.of(-100, -200);
      Position pos1 = Positions.of(5, 6);
      Position pos2 = Positions.of(1, 1);
      Position pos3 = Positions.of(10, 11);

      // When
      boolean actualIsPositionOnLevel = MathUtil.isPositionOnLevel(pos2Test, pos1, pos2, pos3);

      // Then
      assertThat(actualIsPositionOnLevel, is(expectedIsPositionOnLevel));
   }

   @Test
   void testRotateVector_90Deg() {

      // Given
      Float64Vector float64Vector = VectorUtil.getVector(Positions.of(5, 0));

      // When
      Float64Vector rotatedVector = MathUtil.rotateVector(float64Vector, 90);

      // Then
      assertThat(rotatedVector.get(1).doubleValue(), is(5.0));
      assertThat(rotatedVector.get(0).doubleValue(), is(0.0));
   }

   @Test
   void testRotateVector_270Deg() {

      // Given
      Float64Vector float64Vector = VectorUtil.getVector(Positions.of(5, 0));

      // When
      Float64Vector rotatedVector = MathUtil.rotateVector(float64Vector, -90);

      // Then
      assertThat(rotatedVector.get(1).doubleValue(), is(-5.0));
      assertThat(rotatedVector.get(0).doubleValue(), is(0.0));
   }

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
   void testCalcAngle_SecondSector() {

      // Given
      double expectedCalcAngleBetweenVectors = 26.565;

      Position gridElementPos = Positions.of(-2, 7);
      Position moveablePosition = Positions.of(1, 1);

      // When
      double actualCalcAngleBetweenVectors = round(
            MathUtil.calcAngleBetweenPositions(moveablePosition, gridElementPos), 3);

      // Then
      assertThat(actualCalcAngleBetweenVectors, is(expectedCalcAngleBetweenVectors));
   }

   @Test
   void testCalcAngle_FirstAndForthSector() {

      // Given
      double expectedCalcAngleBetweenVectors = 54.462;

      Position gridElementPos = Positions.of(8, -4);
      Position moveablePosition = Positions.of(Directions.S, 1, 1);

      // When
      double actualCalcAngleBetweenVectors = round(
            MathUtil.calcAngleBetweenPositions(moveablePosition, gridElementPos), 3);

      // Then
      assertThat(actualCalcAngleBetweenVectors, is(expectedCalcAngleBetweenVectors));
   }

   @Test
   void testCalcAngle_ThirdAndForthSector() {

      // Given
      double expectedCalcAngleBetweenVectors = 52.12;

      Position gridElementPos = Positions.of(8, -4);
      Position moveablePosition = Positions.of(Directions.S, -2, -2);
      moveablePosition.rotate(26.57);// rotate to simulate the direction of a moveable which leads into the 4. sector

      // When
      double actualCalcAngleBetweenVectors = round(
            MathUtil.calcAngleBetweenPositions(moveablePosition, gridElementPos), 3);

      // Then
      assertThat(actualCalcAngleBetweenVectors, is(expectedCalcAngleBetweenVectors));
   }

   @Test
   void testRoundDown_ToDecimals() {

      // Given
      double pi = 3.1234;
      double expectedRoundedPi = 3.12;

      // When
      double roundetPi = MathUtil.round(pi, 2);

      // Then
      Assert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
   }

   @Test
   void testRoundDown_zeroDecimals() {

      // Given
      double pi = 3.1234;
      double expectedRoundedPi = 3;

      // When
      double roundetPi = MathUtil.round(pi, 0);

      // Then
      Assert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
   }

   @Test
   void testRoundUp2() {

      // Given
      double number = -3.53553;
      double expectedRoundedNumber = -3.54;

      // When
      double roundetNumber = MathUtil.round(number, 2);

      // Then
      Assert.assertThat(roundetNumber, CoreMatchers.is(expectedRoundedNumber));
   }

   @Test
   void testRoundUp() {

      // Given
      double pi = 3.1265;
      double expectedRoundedPi = 3.127;

      // When
      double roundetPi = MathUtil.roundThreePlaces(pi);

      // Then
      Assert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
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
