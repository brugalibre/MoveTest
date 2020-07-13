package com.myownb3.piranha.core.weapon.trajectory.impl;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class TargetPositionLeadEvaluatorImplTest {

   @Test
   void testComputeDeltaT_dtSmallerZero() {
      // Given
      TargetPositionLeadEvaluatorImpl positionLeadEvaluatorImpl = new TargetPositionLeadEvaluatorImpl(5);
      double dt1 = -1;
      double sqrt = 2.0;
      double B = 3.5;
      double A = 1.5;
      double expectedDt2 = -0.5;

      // When
      double actualDt2 = positionLeadEvaluatorImpl.computeDeltaT(A, B, sqrt, dt1);

      // Then
      MatcherAssert.assertThat(actualDt2, CoreMatchers.is(expectedDt2));

   }

}
