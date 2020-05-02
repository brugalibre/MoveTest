package com.myownb3.piranha.grid.gridelement;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;

class MoveableObstacleImplTest {

   @Test
   void testMoveableObstacleImpl() {

      // Given
      MoveableObstacleImpl moveable = new MoveableObstacleImpl(Mockito.mock(Grid.class),
            Positions.of(0, 0));

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
      MoveableObstacleImpl moveable = new MoveableObstacleImpl(Mockito.mock(Grid.class), Positions.of(0, 0), shape);

      // Then
      assertThat(moveable.getShape(), is(shape));
   }
}
