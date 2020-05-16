package com.myownb3.piranha.util.vector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jscience.mathematics.vector.Float64Vector;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;

class VectorUtilTest {


   @Test
   void testRotateVector_90Deg() {

      // Given
      Float64Vector float64Vector = VectorUtil.getVector(Positions.of(5, 0));

      // When
      Float64Vector rotatedVector = VectorUtil.rotateVector(float64Vector, 90);

      // Then
      assertThat(rotatedVector.get(1).doubleValue(), is(5.0));
      assertThat(rotatedVector.get(0).doubleValue(), is(0.0));
      assertThat(rotatedVector.get(2).doubleValue(), is(0.0));
   }

   @Test
   void testRotateVector_270Deg() {

      // Given
      Float64Vector float64Vector = VectorUtil.getVector(Positions.of(5, 0));

      // When
      Float64Vector rotatedVector = VectorUtil.rotateVector(float64Vector, -90);

      // Then
      assertThat(rotatedVector.get(1).doubleValue(), is(-5.0));
      assertThat(rotatedVector.get(0).doubleValue(), is(0.0));
      assertThat(rotatedVector.get(2).doubleValue(), is(0.0));
   }
}
