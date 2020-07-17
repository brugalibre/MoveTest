package com.myownb3.piranha.core.grid.filter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class FilterGridElementsMovingAwayTest {

   @Test
   void testIsFalse_MovingGridElementMovesAway() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAvoidableGridElementElement(Positions.of(5, 0), 1)
            .withMovedGridElement(Positions.of(1, 0), 0)
            .build();

      // When
      boolean actualIsTrue = tcb.gridElementsMovingAwayFilter.test(tcb.avoidableGridElement);

      // Then
      assertThat(actualIsTrue, is(false));
   }

   @Test
   void testIsTrue_MovingGridElementMovesCloser() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAvoidableGridElementElement(Positions.of(Directions.S, 0, 5, 0), 1)
            .withMovedGridElement(Positions.of(0, 1), 0)
            .build();

      // When
      boolean actualIsTrue = tcb.gridElementsMovingAwayFilter.test(tcb.avoidableGridElement);

      // Then
      assertThat(actualIsTrue, is(true));
   }

   @Test
   void testIsTrue_NotMovingGridElement() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAvoidableGridElementElement(Positions.of(5, 0), 0)
            .withMovedGridElement(Positions.of(1, 0), 5)
            .build();

      // When
      boolean actualIsTrue = tcb.gridElementsMovingAwayFilter.test(tcb.avoidableGridElement);

      // Then
      assertThat(actualIsTrue, is(true));
   }

   private static class TestCaseBuilder {

      private FilterGridElementsMovingAway gridElementsMovingAwayFilter;
      private GridElement movedGridElement;
      private GridElement avoidableGridElement;

      private TestCaseBuilder withAvoidableGridElementElement(Position position, int velocity) {
         this.avoidableGridElement = mockGridElement(position, velocity);
         return this;
      }

      private TestCaseBuilder withMovedGridElement(Position position, int velocity) {
         this.movedGridElement = mockGridElement(position, velocity);
         return this;
      }

      private TestCaseBuilder build() {
         gridElementsMovingAwayFilter = FilterGridElementsMovingAway.of(() -> movedGridElement);
         return this;
      }

      private GridElement mockGridElement(Position position, int velocity) {
         GridElement gridElement = mock(GridElement.class);
         when(gridElement.getPosition()).thenReturn(position);
         when(gridElement.getVelocity()).thenReturn(velocity);
         return gridElement;
      }
   }
}
