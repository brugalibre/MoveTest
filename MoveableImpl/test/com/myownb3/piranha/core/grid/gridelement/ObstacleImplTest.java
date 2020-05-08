package com.myownb3.piranha.core.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.position.Position;

class ObstacleImplTest {

   @Test
   void testConstructor() {

      // Given
      Grid grid = Mockito.mock(Grid.class);
      Position position = Mockito.mock(Position.class);
      CircleImpl shape = Mockito.mock(CircleImpl.class);

      // When
      Obstacle obstacle = new ObstacleImpl(grid, position, shape);

      // Then
      assertThat(obstacle.getShape(), is(shape));
   }

}
