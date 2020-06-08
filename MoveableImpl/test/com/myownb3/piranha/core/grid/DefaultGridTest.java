package com.myownb3.piranha.core.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.MoveableBuilder;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;

class DefaultGridTest {

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_ToFarAway() {

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
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos1)
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos2)
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.emptyList()));
   }

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_OneCloseEnoughAway() {

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
      Obstacle obstacle1 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos1)
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos2)
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.singletonList(obstacle1)));
   }

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_OneCloseEnoughAwayButBehindMovingGridElement1() {

      // Given
      int radius = 5;
      Position moveablePos = Positions.of(Directions.N, 0, 2.99);
      Position obstaclePos1 = Positions.of(0, 1);
      Position obstaclePos2 = Positions.of(0, -3);
      Position obstaclePos3 = Positions.of(0, 3);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(-5)
            .withMinY(-5)
            .withCollisionDetectionHandler((a, b, c) -> new CollisionDetectionResultImpl(c))
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos));
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos1)
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos2)
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      ObstacleImpl obstacle3 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos3)
            .withShape(buildCircle(obstaclePos3, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.singletonList(obstacle3)));
   }

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_OneCloseEnoughAwayButBehindMovingGridElement2() {

      // Given
      int radius = 5;
      Position moveablePos = Positions.of(Directions.S, 0, 0);
      Position obstaclePos1 = Positions.of(0, 1);
      Position obstaclePos2 = Positions.of(0, -3);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(-5)
            .withMinY(-5)
            .withCollisionDetectionHandler((a, b, c) -> new CollisionDetectionResultImpl(c))
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos));
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos1)
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleImpl obstacle2 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos2)
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.singletonList(obstacle2)));
   }

   private CircleImpl buildCircle(Position obstaclePos2, int radius) {
      return CircleBuilder.builder()
            .withRadius(radius)
            .withAmountOfPoints(4)
            .withCenter(obstaclePos2)
            .build();
   }

   @Test
   void testGetAllGridElementsWithinDistance_WithinDistance() {

      // Given
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos1 = Positions.of(0, 6.1);
      Position obstaclePos2 = Positions.of(0, 5.5);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = buildMoveable(grid, moveablePos);
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos1)
            .build();
      Obstacle obstacle2 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos2)
            .build();

      // When
      List<GridElement> allGridElementsWithinDistance = grid.getAllAvoidableGridElementsWithinDistance(moveable, 5);

      // Then
      assertThat(allGridElementsWithinDistance.size(), is(1));
      assertThat(allGridElementsWithinDistance.get(0), is(obstacle2));
   }

   @Test
   void testGetAllGridElementsWithinDistance_OutOfDistance() {

      // Given
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos = Positions.of(0, 6.1);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = buildMoveable(grid, moveablePos);
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(obstaclePos)
            .build();

      // When
      List<GridElement> allGridElementsWithinDistance = grid.getAllAvoidableGridElementsWithinDistance(moveable, 5);

      // Then
      assertThat(allGridElementsWithinDistance.size(), is(0));
   }

   @Test
   void testGetAllGridElementsWithinDistance_EndPointMoveable() {

      // Given

      DefaultGrid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = EndPointMoveableBuilder.builder()
            .withStartPosition(Positions.of(0, 0))
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withMoveablePostActionHandler((a, b) -> {
            })
            .build();
      ObstacleImpl obstacleImpl = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(5, 0))
            .build();

      // When
      List<GridElement> allGridElementsWithinDistance = grid.getAllAvoidableGridElementsWithinDistance(obstacleImpl, 5);

      // Then
      assertThat(allGridElementsWithinDistance.size(), is(1));
      assertThat(allGridElementsWithinDistance.get(0), is(moveable));
   }

   @Test
   void testGetAllGridElementsWithinDistance_Projectile() {

      // Given
      DefaultGrid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      ProjectileGridElement bulletImpl = ProjectileGridElementBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(0, 0))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

      ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(10, 0))
            .build();

      // When
      List<GridElement> allGridElementsWithinDistance = grid.getAllGridElementsWithinDistance(Positions.of(0, 0), 5);

      // Then
      assertThat(allGridElementsWithinDistance.size(), is(1));
      assertThat(allGridElementsWithinDistance.get(0), is(bulletImpl));
   }

   private Moveable buildMoveable(Grid grid, Position gridElemPos) {
      return MoveableBuilder.builder()
            .withGrid(grid)
            .withPosition(gridElemPos)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElemPos)
                  .build())
            .build();
   }
}
