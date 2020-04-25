package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;

class DefaultGridTest {

   @Test
   void testGetAllAvoidables2CheckCollisionWithinDistance_ToFarAway() {

      // Given
      int radius = 5;
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos1 = Positions.of(30, 30);
      Position obstaclePos2 = Positions.of(40, 40);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos));
      new ObstacleImpl(grid, obstaclePos1, buildCircle(obstaclePos1, radius));
      new ObstacleImpl(grid, obstaclePos2, buildCircle(obstaclePos2, radius));
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.emptyList()));
   }

   @Test
   void testGetAllAvoidables2CheckCollisionWithinDistance_OneCloseEnoughAway() {

      // Given
      int radius = 5;
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos1 = Positions.of(3, 0);
      Position obstaclePos2 = Positions.of(40, 40);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos));
      Obstacle obstacle1 = new ObstacleImpl(grid, obstaclePos1, buildCircle(obstaclePos1, radius));
      new ObstacleImpl(grid, obstaclePos2, buildCircle(obstaclePos2, radius));
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.singletonList(obstacle1)));
   }

   private CircleImpl buildCircle(Position obstaclePos2, int radius) {
      return new CircleBuilder(radius)
            .withAmountOfPoints(4)
            .withCenter(obstaclePos2)
            .build();
   }

   @Test
   void testGetAllAvoidablesWithinDistance_WithinDistance() {

      // Given
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos1 = Positions.of(0, 5.1);
      Position obstaclePos2 = Positions.of(0, 5.0);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = buildMoveable(grid, moveablePos);
      new ObstacleImpl(grid, obstaclePos1);
      Obstacle obstacle2 = new ObstacleImpl(grid, obstaclePos2);

      // When
      List<Avoidable> allAvoidablesWithinDistance = grid.getAllAvoidablesWithinDistance(moveable, 5);

      // Then
      assertThat(allAvoidablesWithinDistance.size(), is(1));
      assertThat(allAvoidablesWithinDistance.get(0), is(obstacle2));
   }

   @Test
   void testGetAllAvoidablesWithinDistance_OutOfDistance() {

      // Given
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos = Positions.of(0, 5.1);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = buildMoveable(grid, moveablePos);
      new ObstacleImpl(grid, obstaclePos);

      // When
      List<Avoidable> allAvoidablesWithinDistance = grid.getAllAvoidablesWithinDistance(moveable, 5);

      // Then
      assertThat(allAvoidablesWithinDistance.size(), is(0));
   }

   private Moveable buildMoveable(Grid grid, Position gridElemPos) {
      return MoveableBuilder.builder(grid, gridElemPos)
            .withShape(new PositionShape(gridElemPos))
            .build();
   }
}
