package com.myownb3.piranha.core.grid.direction;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class DirectionImplTest {

   @Test
   void testRotateNAN() {

      // Given
      double angle2Turn = Double.NaN;

      // When 
      Executable exe = () -> {
         Directions.of(1, 1).rotate(angle2Turn);
      };
      // Then
      assertThrows(IllegalArgumentException.class, exe);
   }
}
