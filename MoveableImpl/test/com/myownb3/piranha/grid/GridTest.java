/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.exception.GridElementOutOfBoundsException;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
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
      CollisionTestCaseBuilder tcb = new CollisionTestCaseBuilder().withMoveablePos(Positions.of(0, 0))
            .withObstaclePos(Positions.of(0, 0.1))
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withGrid()
            .withMoveable()
            .withObstacle();

      // When
      tcb.moveable.moveForward();

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(tcb.obstacle), any());
   }

   @Test
   public void testBuildGridWithCollisionHandler_DifferentPos() {

      // Given
      CollisionTestCaseBuilder tcb = new CollisionTestCaseBuilder().withMoveablePos(Positions.of(0, 0))
            .withObstaclePos(Positions.of(0, 0.2))
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withGrid()
            .withMoveable()
            .withObstacle();

      // When
      tcb.moveable.moveForward();

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(tcb.obstacle), any());
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
   public void testAddElementOnGrid() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      boolean isElementOnGrid = true;

      // When
      Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7.1));

      boolean isElementEffectivelyOnGrid = grid.containsElement(obstacle);

      // Then
      assertThat(isElementEffectivelyOnGrid, is(isElementOnGrid));
   }

   @Test
   public void testAddElementOnGridAndMove() {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(20)
            .withMaxY(20)
            .build();
      boolean isElementOnGridBeforeMove = true;
      boolean isElementOnGridAfterMove = true;

      // When

      Moveable moveable = MoveableBuilder.builder(grid, Positions.of(1, 7.1))
            .build();
      boolean isElementEffectivelyOnGridAfterMove = grid.containsElement(moveable);

      moveable.moveForward(5);
      boolean isElementEffectivelyOnGridBeforeMove = grid.containsElement(moveable);

      // Then
      assertThat(isElementEffectivelyOnGridBeforeMove, is(isElementOnGridBeforeMove));
      assertThat(isElementEffectivelyOnGridAfterMove, is(isElementOnGridAfterMove));
   }

   @Test
   public void testOutOfBoundsWhenCreatingNewGridElement() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();

      // When
      Executable ex = () -> {
         MoveableBuilder.builder(grid, Positions.of(20, 20))
               .build();
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testOutOfUpperBoundsXDefaultGrid() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .build();

      // When
      moveable.turnRight();
      Executable ex = () -> {
         moveable.moveForward(110);
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testOutOfUpperBoundsYDefaultGrid() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .build();

      // When
      Executable ex = () -> {
         moveable.moveForward(110);
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
      Moveable moveable = MoveableBuilder.builder(grid)
            .build();

      // When

      Executable ex = () -> {
         moveable.moveBackward(1);
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
      Moveable moveable = MoveableBuilder.builder(grid)
            .build();

      // When
      Executable ex = () -> {
         moveable.turnRight();
         moveable.moveBackward(3);
      };
      // Then
      assertThrows(GridElementOutOfBoundsException.class, ex);
   }

   @Test
   public void testNotOutOfUpperBoundsYDefaultGrid() {

      // Given
      Moveable moveable = MoveableBuilder.builder()
            .build();
      Position expectedEndPosition = Positions.of(0, -1);
      // When
      moveable.moveBackward(10);
      // Then
      Assert.assertThatPosition(moveable.getPosition(), is(expectedEndPosition), 3);
   }

   @Test
   public void testAddElementNotOnGrid() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      boolean isElementOnGrid = true;

      // When
      new ObstacleImpl(grid, Positions.of(1, 7.1));
      Obstacle antoherObstacle = new ObstacleImpl(new DefaultGrid(), Positions.of(1, 7.1));

      boolean isElementEffectivelyOnGrid = grid.containsElement(antoherObstacle);

      // Then
      assertThat(isElementEffectivelyOnGrid, is(not(isElementOnGrid)));
   }

   private static class CollisionTestCaseBuilder {

      private Position moveablePos;
      private Position obstaclePosition;
      private CollisionDetectionHandler collisionDetectionHandler = spy(CollisionDetectionHandler.class);
      private DefaultGrid grid;
      private Moveable moveable;
      private ObstacleImpl obstacle;

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
         this.grid = GridBuilder.builder().withCollisionDetectionHandler(collisionDetectionHandler)
               .build();
         return this;
      }

      public CollisionTestCaseBuilder withMoveable() {
         moveable = MoveableBuilder.builder(grid, moveablePos)
               .build();
         return this;
      }

      public CollisionTestCaseBuilder withObstacle() {
         this.obstacle = new ObstacleImpl(grid, obstaclePosition);
         return this;
      }
   }
}
