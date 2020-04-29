package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl;

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
