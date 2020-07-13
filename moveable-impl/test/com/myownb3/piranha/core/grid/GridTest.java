/**
 * 
 */
package com.myownb3.piranha.core.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.exception.GridElementOutOfBoundsException;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.test.Assert;

/**
 * @author Dominic
 *
 */
class GridTest {

   @Test
   public void test_BuildGridOnlyWithMaxXAndMaxY() {

      // Given
      int maxX = 15;
      int maxY = 15;
      int minX = -15;

      // When
      DefaultGrid defaultGrid = GridBuilder.builder()
            .withMaxX(maxX)
            .withMaxY(maxY)
            .withMinX(minX)
            .build();//

      // Then
      assertThat(defaultGrid.minX, is(0));
      assertThat(defaultGrid.minY, is(0));
   }

   @Test
   public void testBuildGridWithCollisionHandler_SamePos() {

      // Given
      CollisionTestCaseBuilder tcb = new CollisionTestCaseBuilder()
            .withMoveablePos(Positions.of(0, 0))
            .withObstaclePos(Positions.of(0, 0.1))
            .withCollisionDetectionHandler(spy(new TestCollisionDetectionHandler()))
            .withGrid()
            .withMoveable()
            .withObstacle()
            .build();

      // When
      tcb.moveable.moveForward();

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.moveable), any());
   }

   @Test
   public void testBuildGridWithCollisionHandler_DifferentPos() {

      // Given
      CollisionTestCaseBuilder tcb = new CollisionTestCaseBuilder()
            .withMoveablePos(Positions.of(0, 0))
            .withObstaclePos(Positions.of(0, 0.2))
            .withCollisionDetectionHandler(spy(new TestCollisionDetectionHandler()))
            .withGrid()
            .withMoveable()
            .withObstacle()
            .build();

      // When
      tcb.moveable.moveForward();

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.moveable), any());
   }

   @Test
   public void testGridDimensionGrid() {

      // Given
      int minX = -5;
      int minY = -5;
      int maxX = 5;
      int maxY = 5;

      int expectedX = -5;
      int expectedY = -5;
      int expectedHeight = 10;
      int expectedWidth = 10;

      // When
      Grid grid = GridBuilder.builder()
            .withMaxX(maxX)
            .withMaxY(maxY)
            .withMinX(minX)
            .withMinY(minY)
            .build();
      Dimension dimension = grid.getDimension();

      // Then
      assertThat(dimension.getX(), is(expectedX));
      assertThat(dimension.getY(), is(expectedY));
      assertThat(dimension.getHeight(), is(expectedHeight));
      assertThat(dimension.getWidth(), is(expectedWidth));
   }

   @Test
   public void testOutOfBoundsWhenCreatingNewGridElement() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();

      // When
      Executable ex = () -> {
         MoveableBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(Positions.of(20, 20))
                     .build())
               .build();
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testOutOfUpperBoundsXDefaultGrid() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(110)
            .build();

      // When
      moveable.turnRight();
      Executable ex = () -> {
         moveable.moveForward();
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testOutOfUpperBoundsYDefaultGrid() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(110)
            .build();

      // When
      Executable ex = () -> {
         moveable.moveForward();
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testOutOfLowerBoundsDefaultGrid() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(1)
            .build();

      // When

      Executable ex = () -> {
         moveable.moveBackward();
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testOutOfLowerBoundsXDefaultGrid() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(10)
            .withMaxY(10)
            .withMinX(0)
            .withMinY(0)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(3)
            .build();

      // When
      Executable ex = () -> {
         moveable.turnRight();
         moveable.moveBackward();
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testNotOutOfUpperBoundsYDefaultGrid() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withVelocity(10)
            .build();
      Position expectedEndPosition = Positions.of(0, -1);
      // When
      moveable.moveBackward();
      // Then
      Assert.assertThatPosition(moveable.getPosition(), is(expectedEndPosition), 3);
   }

   private static class CollisionTestCaseBuilder {

      private Position moveablePos;
      private Position obstaclePosition;
      private CollisionDetectionHandler collisionDetectionHandler = spy(CollisionDetectionHandler.class);
      private DefaultGrid grid;
      private Moveable moveable;

      public CollisionTestCaseBuilder withMoveablePos(Position moveablePosition) {
         this.moveablePos = moveablePosition;
         return this;
      }

      public CollisionTestCaseBuilder withObstaclePos(Position obstaclePosition) {
         this.obstaclePosition = obstaclePosition;
         return this;
      }

      public CollisionTestCaseBuilder withCollisionDetectionHandler(
            CollisionDetectionHandler collisionDetectionHandler) {
         this.collisionDetectionHandler = collisionDetectionHandler;
         return this;
      }

      public CollisionTestCaseBuilder withGrid() {
         this.grid = GridBuilder.builder()
               .withCollisionDetectionHandler(collisionDetectionHandler)
               .build();
         return this;
      }

      public CollisionTestCaseBuilder withMoveable() {
         moveable = MoveableBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(moveablePos)
                     .build())
               .build();
         return this;
      }

      public CollisionTestCaseBuilder withObstacle() {
         ObstacleBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(obstaclePosition)
                     .build())
               .build();
         return this;
      }

      public CollisionTestCaseBuilder build() {
         return this;
      }
   }

   private class TestCollisionDetectionHandler implements CollisionDetectionHandler {
      @Override
      public CollisionDetectionResult handleCollision(List<CollisionGridElement> otherGridElements, GridElement movedGridElement,
            Position newPosition) {
         return new CollisionDetectionResultImpl(newPosition);
      }
   }
}
