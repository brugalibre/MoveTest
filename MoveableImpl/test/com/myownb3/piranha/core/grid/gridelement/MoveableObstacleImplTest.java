package com.myownb3.piranha.core.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class MoveableObstacleImplTest {

   @Test
   void testMoveableObstacleImpl() {

      // Given
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(Positions.of(0, 0))
            .build();

      // When
      boolean isObstacle = Obstacle.class.isAssignableFrom(moveable.getClass());

      // Then
      assertThat(isObstacle, is(true));
   }

   @Test
   void testMoveableObstacleImpl_WithShape() {

      // Given
      Position startPos = Positions.of(0, 0);
      PositionShape shape = PositionShapeBuilder.builder()
            .withPosition(startPos)
            .build();

      // When
      MoveableObstacleImpl moveable = MoveableObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withPosition(Positions.of(0, 0))
            .withShape(shape)
            .build();

      // Then
      assertThat(moveable.getShape(), is(shape));
   }
}
