package com.myownb3.piranha.core.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;

class DummyGridWallTest {

   @Test
   void testDummyGridWall() {

      // Given
      DummyGridWall dummyGridWall = new DummyGridWall(mock(Grid.class), Positions.of(5, 5));

      // When
      boolean actualIsAvoidable = dummyGridWall.isAvoidable();

      // Then
      assertThat(actualIsAvoidable, is(false));
   }

}
