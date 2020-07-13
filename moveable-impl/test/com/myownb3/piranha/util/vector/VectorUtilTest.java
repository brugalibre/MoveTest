package com.myownb3.piranha.util.vector;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.jscience.mathematics.vector.Float64Vector;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.util.MathUtil;

class VectorUtilTest {

   @Test
   void testGetNormalVector() {

      // Given
      double y1 = 6.0;
      double x1 = 5.0;
      double y2 = round(MathUtil.getRandom(5), 10);
      double x2 = round(MathUtil.getRandom(5), 10);
      Float64Vector vector1 = Float64Vector.valueOf(x1, y1, 0);
      Float64Vector vector2 = Float64Vector.valueOf(x2, y2, 0);

      // When
      Float64Vector rotatedVector1 = VectorUtil.getNormal(vector1);
      Float64Vector rotatedVector2 = VectorUtil.getNormal(vector2);

      // Then
      assertThat(rotatedVector1.get(0).doubleValue(), is(y1));
      assertThat(rotatedVector1.get(1).doubleValue(), is(-x1));
      assertThat(rotatedVector2.get(0).doubleValue(), is(y2));
      assertThat(rotatedVector2.get(1).doubleValue(), is(-x2));
   }

   @Test
   void testRotateVector_90Deg() {

      // Given
      Float64Vector float64Vector = Positions.of(5, 0).getVector();

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
      Float64Vector float64Vector = Positions.of(5, 0).getVector();

      // When
      Float64Vector rotatedVector = VectorUtil.rotateVector(float64Vector, -90);

      // Then
      assertThat(rotatedVector.get(1).doubleValue(), is(-5.0));
      assertThat(rotatedVector.get(0).doubleValue(), is(0.0));
      assertThat(rotatedVector.get(2).doubleValue(), is(0.0));
   }
}
