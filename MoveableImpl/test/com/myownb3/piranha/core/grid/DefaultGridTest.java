package com.myownb3.piranha.core.grid;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;

class DefaultGridTest {

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_ToFarAway() {

      // Given
      int velocity = 5;
      int radius = velocity;
      Position moveablePos = Positions.of(0, 0);
      Position obstaclePos1 = Positions.of(30, 30);
      Position obstaclePos2 = Positions.of(40, 40);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos, velocity));
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos1)
                  .build())
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos2)
                  .build())
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable, times(velocity)).check4Collision(any(), any(), eq(Collections.emptyList()));
   }

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_OneCloseEnoughAway() {

      // Given
      int velocity = 5;
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
      Moveable moveable = spy(buildMoveable(grid, moveablePos, velocity));
      Obstacle obstacle1 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos1)
                  .build())
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos2)
                  .build())
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable, times(velocity)).check4Collision(any(), any(), eq(Collections.singletonList(obstacle1)));
   }

   @Test
   void testGetAllGridElements2Check_CollisionWithinDistance_DontIgnoreSlowerMoveablesInFront_ToClose() {

      // Given
      int moveableVelocity = 5;
      int radius = 5;
      Position moveablePos = Positions.of(Directions.N, 0, 2.99, 0);
      Position moveableObstaclePos1 = Positions.of(0, 1, 0);
      Position moveableObstaclePos2 = Positions.of(0, 3.1, 0);
      Position moveableObstaclePos3 = Positions.of(0, 3, 0);
      Position moveableObstaclePos4 = Positions.of(0, 3, 0).rotate(180);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(-5)
            .withMinY(-5)
            .withCollisionDetectionHandler((a, b, c) -> new CollisionDetectionResultImpl(c))
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos, moveableVelocity));
      MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(moveableObstaclePos1, radius))
            .withVelocity(10)
            .build();
      MoveableObstacleImpl obstacle2 = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(moveableObstaclePos2, radius))
            .withVelocity(4)
            .build();
      MoveableObstacleImpl obstacle3 = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(moveableObstaclePos3, radius))
            .withVelocity(4)
            .build();
      MoveableObstacleImpl obstacle4 = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(moveableObstaclePos4, radius))
            .withVelocity(4)
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Arrays.asList(obstacle2, obstacle3, obstacle4)));
   }

   @Test
   void testGetAllGridElements2CheckCollisionWithinDistance_OneCloseEnoughAwayButBehindMovingGridElement1() {

      // Given
      int radius = 5;
      Position moveablePos = Positions.of(Directions.N, 0, 2.99, 0);
      Position obstaclePos1 = Positions.of(0, 1, 0);
      Position obstaclePos2 = Positions.of(0, -3, 0);
      Position obstaclePos3 = Positions.of(0, 3, 0);
      Grid grid = GridBuilder.builder()
            .withMaxX(100)
            .withMaxY(100)
            .withMinX(-5)
            .withMinY(-5)
            .withCollisionDetectionHandler((a, b, c) -> new CollisionDetectionResultImpl(c))
            .build();
      Moveable moveable = spy(buildMoveable(grid, moveablePos, 5));
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(obstaclePos1, radius))
            .build();
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(obstaclePos2, radius))
            .build();
      ObstacleImpl obstacle3 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(buildCircle(obstaclePos3, radius))
            .build();
      grid.prepare();

      // When
      moveable.moveForward();

      // Then
      verify(moveable).check4Collision(any(), any(), eq(Collections.singletonList(obstacle3)));
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
      Moveable moveable = buildMoveable(grid, moveablePos, 5);
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos1)
                  .build())
            .build();
      Obstacle obstacle2 = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos2)
                  .build())
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
      Moveable moveable = buildMoveable(grid, moveablePos, 5);
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(obstaclePos)
                  .build())
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
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withMovingIncrement(1)
            .build();
      ObstacleImpl obstacleImpl = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 0))
                  .build())
            .build();

      // When
      List<GridElement> allGridElementsWithinDistance = grid.getAllAvoidableGridElementsWithinDistance(moveable, 5);

      // Then
      assertThat(allGridElementsWithinDistance.size(), is(1));
      assertThat(allGridElementsWithinDistance.get(0), is(obstacleImpl));
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
            .withProjectile(ProjectileBuilder.builder()
                  .withHealth(2)
                  .withShape(PositionShapeBuilder.builder()
                        .withPosition(Positions.of(0, 0))
                        .build())
                  .withProjectileTypes(ProjectileTypes.BULLET)
                  .build())
            .withVelocity(10)
            .withDimensionInfo(getDefaultDimensionInfo(1))
            .build();

      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(10, 0))
                  .build())
            .build();

      // When
      List<GridElement> allGridElementsWithinDistance = grid.getAllGridElementsWithinDistance(Positions.of(0, 0), 5);

      // Then
      assertThat(allGridElementsWithinDistance.size(), is(1));
      assertThat(allGridElementsWithinDistance.get(0), is(bulletImpl));
   }

   private Moveable buildMoveable(Grid grid, Position gridElemPos, int velocity) {
      return MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(gridElemPos)
                  .build())
            .withVelocity(velocity)
            .build();
   }
}
