package com.myownb3.piranha.core.collision.detection.shape.rectangle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionDetector;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.detection.shape.rectangle.RectangleCollisionDetectorImpl.RectangleCollisionDetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class RectangleCollisionDetectorImplTest {

   @Test
   void testCheckCollision_NoCollision() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position obstacle1Position = Positions.of(9, 7.1);
      Position obstacle2Position = Positions.of(7.1, 9.1);
      Obstacle obstacle = mockObstacle(obstacle1Position);
      Obstacle obstacle2 = mockObstacle(obstacle2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(10)
            .withWidth(10)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addObstacle(obstacle)
            .addObstacle(obstacle2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allObstacleGridElements);

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_CollisionInside() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position obstacle1Position = Positions.of(9, 7);
      Position obstacle2Position = Positions.of(3.1, 3.1);
      Obstacle obstacle = mockObstacle(obstacle1Position);
      Obstacle obstacle2 = mockObstacle(obstacle2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(8)
            .withWidth(12)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addObstacle(obstacle)
            .addObstacle(obstacle2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allObstacleGridElements);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_CollisionInside_2Quadrant() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position obstacle1Position = Positions.of(-5, -3.01);
      Position obstacle2Position = Positions.of(-4.99, 4.99);
      Obstacle obstacle = mockObstacle(obstacle1Position);
      Obstacle obstacle2 = mockObstacle(obstacle2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(8)
            .withWidth(12)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addObstacle(obstacle)
            .addObstacle(obstacle2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allObstacleGridElements);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_CollisionInside_3Quadrant() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position obstacle1Position = Positions.of(-5, -3);
      Position obstacle2Position = Positions.of(7, -3.1);
      Obstacle obstacle = mockObstacle(obstacle1Position);
      Obstacle obstacle2 = mockObstacle(obstacle2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(8)
            .withWidth(12)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addObstacle(obstacle)
            .addObstacle(obstacle2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allObstacleGridElements);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_Collision() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position obstacle1Position = Positions.of(9, 7);
      Position obstacle2Position = Positions.of(5, 5);
      Obstacle obstacle = mockObstacle(obstacle1Position);
      Obstacle obstacle2 = mockObstacle(obstacle2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(10)
            .withWidth(10)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addObstacle(obstacle)
            .addObstacle(obstacle2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allObstacleGridElements);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   private Obstacle mockObstacle(Position position) {
      return ObstacleBuilder.builder()
            .withGrid(mock(Grid.class))
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(position)
                  .build())
            .build();
   }

   private static class TestCaseBuilder {

      private List<GridElement> allObstacleGridElements;
      private Position newPosition;
      private Position oldPosition;
      private GridElement movedGridElement;
      private CollisionDetectionHandler collisionDetectionHandler;
      private CollisionDetector collisionDetector;
      private Position center;
      private int width;
      private int height;

      private TestCaseBuilder() {
         allObstacleGridElements = new ArrayList<>();
      }

      private TestCaseBuilder withCollisionDetectionHandler() {
         CollisionDetectionHandler cDhHandler = new CollisionDetectionHandler() {
            @Override
            public CollisionDetectionResult handleCollision(List<CollisionGridElement> otherGridElements, GridElement gridElement,
                  Position newPosition) {
               return null;
            }
         };

         this.collisionDetectionHandler = spy(cDhHandler);
         return this;
      }

      private TestCaseBuilder withCenter(Position center) {
         this.center = center;
         return this;
      }

      private TestCaseBuilder withWidth(int width) {
         this.width = width;
         return this;
      }

      private TestCaseBuilder withHeight(int height) {
         this.height = height;
         return this;
      }

      private TestCaseBuilder withCollisionDetector() {
         collisionDetector = RectangleCollisionDetectorBuilder.builder()
               .withRectangle(RectangleBuilder.builder()
                     .withCenter(center)
                     .withHeight(height)
                     .withWidth(width)
                     .build())
               .build();
         return this;
      }

      private TestCaseBuilder addObstacle(Obstacle obstacle) {
         this.allObstacleGridElements.add(obstacle);
         return this;
      }

      private TestCaseBuilder withNewPosition(Position newPos) {
         this.newPosition = newPos;
         return this;
      }

      private TestCaseBuilder withOldPosition(Position oldPos) {
         this.oldPosition = oldPos;
         return this;
      }

      private TestCaseBuilder withMovedGridElement() {
         this.movedGridElement = mock(GridElement.class);
         when(movedGridElement.getDimensionInfo()).thenReturn(DimensionInfoBuilder.getDefaultDimensionInfo(1));
         when(movedGridElement.getPosition()).thenReturn(Positions.of(0, 0));
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }

   }

}
