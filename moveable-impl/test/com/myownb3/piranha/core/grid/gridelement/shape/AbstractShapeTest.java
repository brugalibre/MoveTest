package com.myownb3.piranha.core.grid.gridelement.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegmentImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class AbstractShapeTest {

   @Test
   void testBuildPath4Collision() {

      // Given
      List<PathSegment> path = Collections.singletonList(new PathSegmentImpl(Positions.of(5, 5), Positions.of(5, 5)));
      TestShape testShape = new TestShape(path, Positions.of(5, 5));

      // When
      List<Position> actualPath4Detection = testShape.buildPath4Detection();

      // Then
      assertThat(actualPath4Detection, is(Collections.singletonList(testShape.center)));
   }

   @Test
   void testGetEvasionAngleRelative2_EvasionAngleMorePositiveThanNegative() {

      // Given
      boolean expectedIsWithinUpperBounds = false;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .withDetectedPosition(detectePosition1)
            .withDetectedPosition(detectePosition2)
            .withDetectedPosition(detectePosition3)
            .withDetectedPosition(detectePosition4)
            .build();

      // When 
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then 
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   @Test
   void testCheckIfDetectedPosAreLeftOrRight_DetectionRightTurnLeft() {

      // Given
      boolean expectedIsWithinUpperBounds = false;
      Position position = Positions.of(5, 5).rotate(-10);
      Position detectePosition1 = Positions.of(15, 5).rotate(Math.random() * 360);
      Position detectePosition2 = Positions.of(15, 10).rotate(Math.random() * 360);
      Position detectePosition3 = Positions.of(15, 15).rotate(Math.random() * 360);
      Position detectePosition4 = Positions.of(15, 20).rotate(Math.random() * 360);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withShape(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withDetectedPosition(detectePosition1)
            .withDetectedPosition(detectePosition2)
            .withDetectedPosition(detectePosition3)
            .withDetectedPosition(detectePosition4)
            .build();

      // Then
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   @Test
   void testCheckIfDetectedPosAreLeftOrRight_NoDetectionAtAll() {

      // Given
      boolean expectedIsWithinUpperBounds = false;
      Position position = Positions.of(5, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withShape(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .build();

      // Then
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   @Test
   void testCheckIfDetectedPosAreLeftOrRight_DetectionLeftTurnRight() {

      // Given
      boolean expectedIsWithinUpperBounds = true;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(20, 2).rotate(Math.random() * 360);
      Position detectePosition2 = Positions.of(9, 2).rotate(Math.random() * 360);
      Position detectePosition3 = Positions.of(5, 2).rotate(Math.random() * 360);
      Position detectePosition4 = Positions.of(1, 2).rotate(Math.random() * 360);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withShape(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withDetectedPosition(detectePosition1)
            .withDetectedPosition(detectePosition2)
            .withDetectedPosition(detectePosition3)
            .withDetectedPosition(detectePosition4)
            .build();

      // When
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   private static class TestShape extends AbstractShape {

      private static final long serialVersionUID = 1L;

      protected TestShape(List<PathSegment> path, Position center) {
         super(path, center);
      }

      @Override
      public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
            List<GridElement> gridElements2Check) {
         return null;
      }

      @Override
      public Position getForemostPosition() {
         return null;
      }

      @Override
      public Position getRearmostPosition() {
         return null;
      }

      @Override
      public double getDimensionRadius() {
         return 0;
      }

   }

   private static class TestCaseBuilder {

      private List<Position> detectedPositions;
      private Shape shape;

      private TestCaseBuilder() {
         detectedPositions = new ArrayList<>();
      }

      private TestCaseBuilder withDetectedPosition(Position detectedPositionIn) {
         detectedPositions.add(detectedPositionIn);
         return this;
      }

      private TestCaseBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      private TestCaseBuilder build() {
         return this;
      }
   }
}
