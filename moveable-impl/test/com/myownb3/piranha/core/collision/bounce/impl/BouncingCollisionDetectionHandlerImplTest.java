package com.myownb3.piranha.core.collision.bounce.impl;

import static java.util.Objects.isNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.CollisionGridElement;
import com.myownb3.piranha.core.collision.CollisionGridElementImpl;
import com.myownb3.piranha.core.collision.Intersection;
import com.myownb3.piranha.core.collision.IntersectionImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;

class BouncingCollisionDetectionHandlerImplTest {

   @Test
   void testHandleCollision_WithTestSimpleBouncePositionEvaluator() {

      // Given
      Position moveablePos = Positions.of(0, 0);
      Position newPosition = Positions.of(0, 1);
      Position bouncedMoveablePos = Positions.of(15, 6);
      Position expectedMoveablePos = bouncedMoveablePos.movePositionForward();
      Grid grid = mock(Grid.class);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withNewPosition(newPosition)
            .withSimpleBouncePositionEvaluator(spy(new TestBouncePositionEvaluator(bouncedMoveablePos)))
            .withMovedGridElement(MoveableBuilder.builder()
                  .withGrid(grid)
                  .withShape(CircleBuilder.builder()
                        .withCenter(moveablePos)
                        .withAmountOfPoints(4)
                        .withRadius(4)
                        .build())
                  .build())
            .withOtherGridElement(MoveableBuilder.builder()
                  .withGrid(grid)
                  .withShape(CircleBuilder.builder()
                        .withCenter(moveablePos)
                        .withAmountOfPoints(4)
                        .withRadius(4)
                        .build())
                  .build())
            .withIntersection(IntersectionImpl.of(new PathSegmentImpl(moveablePos, moveablePos), moveablePos))
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.detectionHandlerImpl.handleCollision(Collections.singletonList(tcb.collisionGridElement), tcb.movedGridElement, tcb.newPosition);

      // Then
      assertThat(collisionDetectionResult.getMovedPosition(), is(expectedMoveablePos));
   }

   @Test
   void testBuildBounceHandleCollision_CallRealEvaluator() {

      // Given
      Position moveablePos = Positions.of(0, 0);
      Grid grid = mock(Grid.class);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withNewPosition(Positions.of(0, 1))
            .withSimpleBouncePositionEvaluator(spy(new BouncedPositionEvaluatorImpl()))
            .withMovedGridElement(MoveableBuilder.builder()
                  .withGrid(grid)
                  .withShape(CircleBuilder.builder()
                        .withCenter(moveablePos)
                        .withAmountOfPoints(4)
                        .withRadius(4)
                        .build())
                  .build())
            .withOtherGridElement(SimpleGridElementBuilder.builder()
                  .withGrid(grid)
                  .withShape(PositionShapeBuilder.builder()
                        .withPosition(moveablePos)
                        .build())
                  .build())
            .withIntersection(IntersectionImpl.of(new PathSegmentImpl(moveablePos, moveablePos), moveablePos))
            .build();

      // When
      tcb.detectionHandlerImpl.handleCollision(Collections.singletonList(tcb.collisionGridElement), tcb.movedGridElement, tcb.newPosition);

      // Then
      verify(tcb.simpleBouncePositionEvaluator).calculateBouncedPosition(any(), eq(moveablePos));
   }

   private static class TestBouncePositionEvaluator extends BouncedPositionEvaluatorImpl {
      private Position bouncedPosition;

      public TestBouncePositionEvaluator(Position bouncedPosition) {
         this.bouncedPosition = bouncedPosition;
      }

      @Override
      public Position calculateBouncedPosition(PathSegment pathSegment, Position gridElemPosition) {
         return bouncedPosition;
      }
   }

   private static class TestCaseBuilder {
      private GridElement otherGridElement;
      private GridElement movedGridElement;
      private Position newPosition;
      private BouncingCollisionDetectionHandlerImpl detectionHandlerImpl;
      private BouncedPositionEvaluatorImpl simpleBouncePositionEvaluator;
      private CollisionGridElement collisionGridElement;
      private Intersection intersection;

      private TestCaseBuilder withNewPosition(Position newPosition) {
         this.newPosition = newPosition;
         return this;
      }

      private TestCaseBuilder withMovedGridElement(GridElement movedGridElement) {
         this.movedGridElement = movedGridElement;
         return this;
      }

      private TestCaseBuilder withOtherGridElement(GridElement otherGridElement) {
         this.otherGridElement = otherGridElement;
         return this;
      }

      private TestCaseBuilder withIntersection(Intersection intersection) {
         this.intersection = intersection;
         return this;
      }

      private TestCaseBuilder withSimpleBouncePositionEvaluator(BouncedPositionEvaluatorImpl simpleBouncePositionEvaluator) {
         this.simpleBouncePositionEvaluator = simpleBouncePositionEvaluator;
         return this;
      }

      private TestCaseBuilder build() {
         if (isNull(simpleBouncePositionEvaluator)) {
            simpleBouncePositionEvaluator = new BouncedPositionEvaluatorImpl();
         }
         detectionHandlerImpl = BouncingCollisionDetectionHandlerBuilder.builder()
               .withBouncedPositionEvaluator(simpleBouncePositionEvaluator)
               .build();
         this.collisionGridElement = CollisionGridElementImpl.of(intersection, otherGridElement);
         return this;
      }
   }
}
