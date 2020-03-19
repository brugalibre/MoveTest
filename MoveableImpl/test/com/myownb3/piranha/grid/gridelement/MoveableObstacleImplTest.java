package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.shape.PointShape;

class MoveableObstacleImplTest {

   @Test
   void testMoveableObstacleImpl() {

      // Given
      MoveableObstacleImpl moveable = new MoveableObstacleImpl(Mockito.mock(Grid.class),
            Mockito.mock(Position.class));

      // When
      boolean isObstacle = Obstacle.class.isAssignableFrom(moveable.getClass());

      // Then
      assertThat(isObstacle, is(true));
   }

   @Test
   void testMoveableObstacleImpl_WithShape() {

      // Given
      Position startPos = Positions.of(0, 0);
      PointShape shape = new PointShape(startPos);

      // When
      MoveableObstacleImpl moveable = new MoveableObstacleImpl(Mockito.mock(Grid.class), Mockito.mock(Position.class), shape);

      // Then
      assertThat(moveable.getShape(), is(shape));
   }
}
