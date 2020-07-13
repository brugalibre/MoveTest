package com.myownb3.piranha.core.grid.gridelement.position;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.position.Position;

class PositionsTest {

   @Test
   void testMovePositionBackward4Distance() {

      // Given
      Position position2Move = Positions.of(5, 5);
      Position expectedPosition = Positions.of(5, 0);
      // When
      Position actualPosition = position2Move.movePositionBackward4Distance(5);

      // Then
      assertThat(actualPosition, is(expectedPosition));
   }

   @Test
   void testMovePositionForward() {

      // Given
      Position position2Move = Positions.of(5, 5);
      Position expectedPosition = Positions.of(5, 5.1);

      // When
      Position actualPosition = position2Move.movePositionForward();

      // Then
      assertThat(actualPosition, is(expectedPosition));
   }
}
