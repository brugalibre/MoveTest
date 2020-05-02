package com.myownb3.piranha.grid.gridelement.shape.rectangle.detection;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.collision.CollisionDetectionHandler;
import com.myownb3.piranha.detector.collision.CollisionDetector;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.detection.RectangleCollisionDetectorImpl.RectangleCollisionDetectorBuilder;

class RectangleCollisionDetectorImplTest {

   @Test
   void testCheckCollision_NoCollision() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position avoidable1Position = Positions.of(9, 7.1);
      Position avoidable2Position = Positions.of(7.1, 9.1);
      Avoidable avoidable = mockAvoidable(avoidable1Position);
      Avoidable avoidable2 = mockAvoidable(avoidable2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(10)
            .withWidth(10)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addAvoidable(avoidable)
            .addAvoidable(avoidable2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition, tcb.allAvoidables);

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_CollisionInside() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position avoidable1Position = Positions.of(9, 7);
      Position avoidable2Position = Positions.of(3.1, 3.1);
      Avoidable avoidable = mockAvoidable(avoidable1Position);
      Avoidable avoidable2 = mockAvoidable(avoidable2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(8)
            .withWidth(12)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addAvoidable(avoidable)
            .addAvoidable(avoidable2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allAvoidables);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(avoidable2), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_CollisionInside_2Quadrant() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position avoidable1Position = Positions.of(-5, -3.01);
      Position avoidable2Position = Positions.of(-4.99, 4.99);
      Avoidable avoidable = mockAvoidable(avoidable1Position);
      Avoidable avoidable2 = mockAvoidable(avoidable2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(8)
            .withWidth(12)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addAvoidable(avoidable)
            .addAvoidable(avoidable2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allAvoidables);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(avoidable2), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_CollisionInside_3Quadrant() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position avoidable1Position = Positions.of(-5, -3);
      Position avoidable2Position = Positions.of(7, -3.1);
      Avoidable avoidable = mockAvoidable(avoidable1Position);
      Avoidable avoidable2 = mockAvoidable(avoidable2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(8)
            .withWidth(12)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addAvoidable(avoidable)
            .addAvoidable(avoidable2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition,
            tcb.allAvoidables);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(avoidable), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   @Test
   void testCheckCollision_Collision() {

      // Given
      Position center = Positions.of(0, 0);
      Position oldPos = center;
      Position newPos = Positions.of(1, 1);
      Position avoidable1Position = Positions.of(9, 7);
      Position avoidable2Position = Positions.of(5, 5);
      Avoidable avoidable = mockAvoidable(avoidable1Position);
      Avoidable avoidable2 = mockAvoidable(avoidable2Position);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenter(center)
            .withHeight(10)
            .withWidth(10)
            .withCollisionDetector()
            .withMovedGridElement()
            .withOldPosition(oldPos)
            .withNewPosition(newPos)
            .addAvoidable(avoidable)
            .addAvoidable(avoidable2)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.collisionDetector.checkCollision(tcb.collisionDetectionHandler, tcb.movedGridElement, tcb.oldPosition, tcb.newPosition, tcb.allAvoidables);

      // Then
      verify(tcb.collisionDetectionHandler).handleCollision(eq(avoidable2), eq(tcb.movedGridElement), eq(tcb.newPosition));
   }

   private Avoidable mockAvoidable(Position position) {
      Avoidable avoidable = mock(Avoidable.class);
      Shape shape = mock(Shape.class);
      when(avoidable.getShape()).thenReturn(shape);
      when(shape.getPath()).thenReturn(Collections.singletonList(position));
      return avoidable;
   }

   private static class TestCaseBuilder {

      private List<Avoidable> allAvoidables;
      private Position newPosition;
      private Position oldPosition;
      private GridElement movedGridElement;
      private CollisionDetectionHandler collisionDetectionHandler;
      private CollisionDetector collisionDetector;
      private Position center;
      private int width;
      private int height;

      private TestCaseBuilder() {
         allAvoidables = new ArrayList<>();
      }

      private TestCaseBuilder withCollisionDetectionHandler() {
         CollisionDetectionHandler cDhHandler = new CollisionDetectionHandler() {
            @Override
            public void handleCollision(Avoidable avoidable, GridElement gridElement, Position newPosition) {
               // ok
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

      private TestCaseBuilder addAvoidable(Avoidable avoidable) {
         this.allAvoidables.add(avoidable);
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
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }

   }

}
