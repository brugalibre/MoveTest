package com.myownb3.piranha.core.collision.detection;

import static java.util.Objects.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.Intersection;
import com.myownb3.piranha.core.collision.detection.DefaultCollisionDetectorImpl.DefaultCollisionDetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class DefaultCollisionDetectorImplTest {

   @Test
   void testCheckCollision_NotOnOrInsideCircle() {

      // Given
      int radius = 4;
      Position posOfShapesPath = Positions.of(0, 5);
      List<PathSegment> pathOfShape = createPathSegment(posOfShapesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 0))
            .build();

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(Collections.singletonList(tcb.collisionGridElement)), eq(tcb.gridElement),
            eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_OnCircle_AlreadyBehindSegment() {

      // Given
      int radius = 4;
      List<PathSegment> pathOfShape = createPathSegment(Positions.of(5.01, 5.01), Positions.of(4.99, 5.01));
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0.9998770722734, 1.1404662753155))
            .build();

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(Collections.singletonList(tcb.collisionGridElement)), eq(tcb.gridElement),
            eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_OnCircle() {

      // Given
      int radius = 5;
      List<PathSegment> pathOfShape = createPathSegment(Positions.of(5.01, 5.01, 1), Positions.of(4.99, 5.01, 1));
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(5, 0, 1))
            .build();

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(any(), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_OnCircle_ButToFarRightOfTheSegment() {

      // Given
      int radius = 5;
      List<PathSegment> pathOfShape = createPathSegment(Positions.of(4.95, 5), Positions.of(5.05, 5));
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(5.08, 5));// 5.08 is .03 to far to the right

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(Collections.singletonList(tcb.collisionGridElement)), eq(tcb.gridElement),
            eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_OnCircle_ButToFarLeftOfTheSegment() {

      // Given
      int radius = 5;
      List<PathSegment> pathOfShape = createPathSegment(Positions.of(4.95, 5), Positions.of(5.05, 5));
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(4.94, 5))// 4.94 is .01 to far to the left
            .build();

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(Collections.singletonList(tcb.collisionGridElement)), eq(tcb.gridElement),
            eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_NewPosOutsideCircle() {

      // Given
      int radius = 5;
      Position posOfGridElementsPath = Positions.of(0, 5);
      List<PathSegment> pathOfShape = createPathSegment(posOfGridElementsPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(0, 11))
            .build();

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(Collections.singletonList(tcb.collisionGridElement)), eq(tcb.gridElement),
            eq(tcb.newPosition));
   }

   @Ignore
   void testCheckCollision_InsideCircle() {

      // Given
      int radius = 5;
      List<PathSegment> pathOfGridElementsShape = createPathSegment(Positions.of(5.01, 5.01), Positions.of(4.99, 5.01));
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfShape(pathOfGridElementsShape)
            .withObstacle()
            .withGridElement()
            .withNewPosition(Positions.of(5, 5.4))
            .build();

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.obstacle));

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(Collections.singletonList(tcb.collisionGridElement)), eq(tcb.gridElement),
            eq(tcb.newPosition));
   }

   private static List<PathSegment> createPathSegment(Position posOfGridElementsPath) {
      return createPathSegment(posOfGridElementsPath, posOfGridElementsPath);
   }

   private static List<PathSegment> createPathSegment(Position beginPos, Position endPos) {
      PathSegment pathSegment = new PathSegmentImpl(beginPos, endPos);
      return Collections.singletonList(pathSegment);
   }

   private static class TestCaseBuilder {

      private Shape shape;
      public DefaultCollisionDetectorImpl detectorImpl;
      public CollisionDetectionHandler collisionDetectionHandler;
      public GridElement gridElement;
      public Position oldPos;
      public Position newPosition;
      private Circle circle;
      private Obstacle obstacle;
      private List<PathSegment> pathOfGridElementsShape;
      private CollisionGridElementImpl collisionGridElement;

      public TestCaseBuilder withCircle(int radius) {
         this.circle = CircleBuilder.builder()
               .withRadius(radius)
               .withAmountOfPoints(4)
               .withCenter(Positions.of(Directions.N, 0, 0, 0))
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
         circle.transform(Positions.of(0, 0));
         this.gridElement = SimpleGridElementBuilder.builder()
               .withGrid(mock(Grid.class))
               .withShape(circle)
               .build();
         return this;
      }

      public TestCaseBuilder withCircleDetectorImpl() {
         this.detectorImpl = DefaultCollisionDetectorBuilder.builder()
               .withShape(circle)
               .build();
         return this;
      }

      public TestCaseBuilder build() {
         collisionGridElement = CollisionGridElementImpl.of(mock(Intersection.class), obstacle);
         return this;
      }

      private Obstacle mockObstacle(List<PathSegment> pathOfShape) {

         if (isNull(shape)) {
            shape = mock(Shape.class);
            when(shape.getPath()).thenReturn(pathOfShape);
         }

         Obstacle obstacle = mock(Obstacle.class);
         when(obstacle.getShape()).thenReturn(shape);
         when(obstacle.getPath(any())).thenReturn(pathOfShape);
         return obstacle;
      }
   }

}
