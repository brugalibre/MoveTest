package com.myownb3.piranha.util.attribute;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.function.Supplier;

import org.jscience.mathematics.vector.Float64Vector;
import org.junit.jupiter.api.Test;

class LazyAttributeTest {

   @Test
   void testGet() {

      // Given
      Supplier<Float64Vector> supplier = spy(new TestSupplier());
      LazyAttribute<Float64Vector> float64VectorAttribute = new LazyAttribute<>(supplier);

      // When
      Float64Vector float64Vector1 = float64VectorAttribute.get();
      Float64Vector float64Vector2 = float64VectorAttribute.get();

      // Then
      assertThat(float64Vector1, is(float64Vector2));
      verify(supplier).get();
   }

   private static class TestSupplier implements Supplier<Float64Vector> {

      private Float64Vector value;

      public TestSupplier() {
         value = Float64Vector.valueOf(4, 4, 0);
      }

      @Override
      public Float64Vector get() {
         return value;
      }
   }
}
