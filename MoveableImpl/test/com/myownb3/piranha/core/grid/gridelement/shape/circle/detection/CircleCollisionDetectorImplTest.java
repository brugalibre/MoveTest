package com.myownb3.piranha.core.grid.gridelement.shape.circle.detection;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.detector.collision.CollisionDetectedException;
import com.myownb3.piranha.core.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.detector.collision.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

class CircleCollisionDetectorImplTest {

   @Test
   void testCheckCollision_NotOnOrInsideCircle() {

      // Given
      int radius = 4;
      Position posOfShapesPath = Positions.of(0, 5);
      List<PathSegment> pathOfShape = mockPathSegment(posOfShapesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 0));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(tcb.obstacle), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_OnCircle() {

      // Given
      int radius = 5;
      Position posOfShapesPath = Positions.of(0, 5);
      List<PathSegment> pathOfShape = mockPathSegment(posOfShapesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 0));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(tcb.obstacle), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_NewPosOutsideCircle() {

      // Given
      int radius = 5;
      Position posOfGridElementsPath = Positions.of(0, 5);
      List<PathSegment> pathOfShape = mockPathSegment(posOfGridElementsPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 11));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(tcb.obstacle), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_InsideCircle() {

      // Given
      int radius = 10;
      Position posOfGridElementsPath = Positions.of(0, 5);
      List<PathSegment> pathOfGridElementsShape = mockPathSegment(posOfGridElementsPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfGridElementsShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 6));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(tcb.obstacle), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheck4Collision_Collision() {

      // Given
      Position posOfGridElementsPath = Positions.of(0, 5);
      List<PathSegment> pathOfGridElementsShape = mockPathSegment(posOfGridElementsPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
            .withCircle(5)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfGridElementsShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 1));

      // When
      Executable ex = () -> {
         tcb.circle.check4Collision(tcb.collisionDetectionHandler, tcb.newPosition, singletonList(tcb.obstacle));
      };

      // Then
      assertThrows(CollisionDetectedException.class, ex);
   }

   private List<PathSegment> mockPathSegment(Position posOfGridElementsPath) {
      PathSegment pathSegment = mock(PathSegment.class);
      when(pathSegment.getBegin()).thenReturn(posOfGridElementsPath);
      return Collections.singletonList(pathSegment);
   }

   private static class TestCaseBuilder {

      public CircleCollisionDetectorImpl detectorImpl;
      public CollisionDetectionHandler collisionDetectionHandler;
      public GridElement gridElement;
      public Position oldPos;
      public Position newPosition;
      private Circle circle;
      private Obstacle obstacle;
      private List<PathSegment> pathOfGridElementsShape;

      public TestCaseBuilder withCircle(int radius) {
         this.circle = CircleBuilder.builder()
               .withRadius(radius)
               .withAmountOfPoints(4)
               .withCenter(Positions.of(Directions.N, 0, 0))
               .build();
         return this;
      }

      public TestCaseBuilder withPathOfShape(List<PathSegment> pathOfGridElementsShape) {
         this.pathOfGridElementsShape = pathOfGridElementsShape;
         return this;
      }

      public TestCaseBuilder withObstacle() {
         this.obstacle = mockObstacle(pathOfGridElementsShape);
         return this;
      }

      public TestCaseBuilder withNewPosition(Position newPosition) {
         this.newPosition = newPosition;
         return this;
      }

      public TestCaseBuilder withCollisionDetectionHandler(CollisionDetectionHandler collisionDetectionHandler) {
         this.collisionDetectionHandler = collisionDetectionHandler;
         return this;
      }

      public TestCaseBuilder withGridElement() {
         this.gridElement = new SimpleGridElement(mock(Grid.class), Positions.of(0, 0), circle);
         return this;
      }

      public TestCaseBuilder withCircleDetectorImpl() {
         this.detectorImpl = new CircleCollisionDetectorImpl(circle);
         return this;
      }
   }

   private static Obstacle mockObstacle(List<PathSegment> pathOfShape) {
      Shape shape = mock(Shape.class);
      when(shape.getPath()).thenReturn(pathOfShape);

      Obstacle obstacle = mock(Obstacle.class);
      when(obstacle.getShape()).thenReturn(shape);
      return obstacle;
   }
}
