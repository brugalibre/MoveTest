package com.myownb3.piranha.statemachine.impl.handler.returningstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ReturningStateHandlerAngleHelperTest {


   @Test
   void testCalcAngle2Turn4CorrectionPhase2_ActualDiffIsZero() {

      // Given
      ReturningStateHandlerAngleHelper helper = new ReturningStateHandlerAngleHelper();
      double initAngle2Turn = 5;
      double currentAngle = 0;
      double expectedAngle2Turn = -0.0;

      // When
      double actualAngle2Turn = helper.calcAngle2Turn4CorrectionPhase2(currentAngle, initAngle2Turn);

      // Then
      assertThat(actualAngle2Turn, is(expectedAngle2Turn));
   }

   @Test
   void testCalcAngle2Turn4CorrectionPhase2_ActualDiffIsNegativ() {

      // Given
      ReturningStateHandlerAngleHelper helper = new ReturningStateHandlerAngleHelper();
      double initAngle2Turn = 5;
      double currentAngle = -10;
      double expectedAngle2Turn = 5.0;

      // When
      double actualAngle2Turn = helper.calcAngle2Turn4CorrectionPhase2(currentAngle, initAngle2Turn);

      // Then
      assertThat(actualAngle2Turn, is(expectedAngle2Turn));
   }

}
