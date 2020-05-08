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
import com.myownb3.piranha.core.grid.gridelement.Avoidable;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;

class CircleCollisionDetectorImplTest {

   @Test
   void testCheckCollision_NotOnOrInsideCircle() {

      // Given
      int radius = 4;
      Position posOfAvoidablesPath = Positions.of(0, 5);
      List<Position> pathOfAvoidablesShape = Collections.singletonList(posOfAvoidablesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfAvoidablesShape(pathOfAvoidablesShape)
            .withAvoidable()
            .withGridElement()
            .withNewPosition(Positions.of(0, 0));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.avoidable));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(tcb.avoidable), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_OnCircle() {

      // Given
      int radius = 5;
      Position posOfAvoidablesPath = Positions.of(0, 5);
      List<Position> pathOfAvoidablesShape = Collections.singletonList(posOfAvoidablesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfAvoidablesShape(pathOfAvoidablesShape)
            .withAvoidable()
            .withGridElement()
            .withNewPosition(Positions.of(0, 0));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.avoidable));

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(tcb.avoidable), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_NewPosOutsideCircle() {

      // Given
      int radius = 5;
      Position posOfAvoidablesPath = Positions.of(0, 5);
      List<Position> pathOfAvoidablesShape = Collections.singletonList(posOfAvoidablesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfAvoidablesShape(pathOfAvoidablesShape)
            .withAvoidable()
            .withGridElement()
            .withNewPosition(Positions.of(0, 11));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.avoidable));

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(eq(tcb.avoidable), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_InsideCircle() {

      // Given
      int radius = 10;
      Position posOfAvoidablesPath = Positions.of(0, 5);
      List<Position> pathOfAvoidablesShape = Collections.singletonList(posOfAvoidablesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(spy(CollisionDetectionHandler.class))
            .withCircle(radius)
            .withCircleDetectorImpl()
            .withPathOfAvoidablesShape(pathOfAvoidablesShape)
            .withAvoidable()
            .withGridElement()
            .withNewPosition(Positions.of(0, 6));

      // When
      tcb.detectorImpl.checkCollision(tcb.collisionDetectionHandler, tcb.gridElement, tcb.oldPos, tcb.newPosition,
            Collections.singletonList(tcb.avoidable));

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(tcb.avoidable), eq(tcb.gridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheck4Collision_Collision() {

      // Given
      Position posOfAvoidablesPath = Positions.of(0, 5);
      List<Position> pathOfAvoidablesShape = Collections.singletonList(posOfAvoidablesPath);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
            .withCircle(5)
            .withCircleDetectorImpl()
            .withPathOfAvoidablesShape(pathOfAvoidablesShape)
            .withAvoidable()
            .withGridElement()
            .withNewPosition(Positions.of(0, 1));

      // When
      Executable ex = () -> {
         tcb.circle.check4Collision(tcb.collisionDetectionHandler, tcb.newPosition, singletonList(tcb.avoidable));
      };

      // Then
      assertThrows(CollisionDetectedException.class, ex);
   }

   private static class TestCaseBuilder {

      public CircleCollisionDetectorImpl detectorImpl;
      public CollisionDetectionHandler collisionDetectionHandler;
      public GridElement gridElement;
      public Position oldPos;
      public Position newPosition;
      private Circle circle;
      private Avoidable avoidable;
      private List<Position> pathOfAvoidablesShape;

      public TestCaseBuilder withCircle(int radius) {
         this.circle = CircleBuilder.builder()
               .withRadius(radius)
               .withAmountOfPoints(4)
               .withCenter(Positions.of(Directions.N, 0, 0))
               .build();
         return this;
      }

      public TestCaseBuilder withPathOfAvoidablesShape(List<Position> pathOfAvoidablesShape) {
         this.pathOfAvoidablesShape = pathOfAvoidablesShape;
         return this;
      }

      public TestCaseBuilder withAvoidable() {
         this.avoidable = mockAvoidable(pathOfAvoidablesShape);
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

   private static Avoidable mockAvoidable(List<Position> pathOfAvoidablesShape) {
      Shape shape = mock(Shape.class);
      when(shape.getPath()).thenReturn(pathOfAvoidablesShape);

      Avoidable avoidable = mock(Avoidable.class);
      when(avoidable.getShape()).thenReturn(shape);
      return avoidable;
   }
}
