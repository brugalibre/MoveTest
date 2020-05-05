package com.myownb3.piranha.grid.gridelement.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;

class AbstractShapeTest {

   @Test
   void testGetEvasionAngleRelative2_EvasionAngleMorePositiveThanNegative() {

      // Given
      boolean expectedIsWithinUpperBounds = true;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPosition(position)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .withDetectedPosition(detectePosition1, false)
            .withDetectedPosition(detectePosition2, false)
            .withDetectedPosition(detectePosition3, false)
            .withDetectedPosition(detectePosition4, true)
            .build();

      // When 
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then 
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   @Test
   void testGetEvasionAngleRelative2_EvasionAngleMoreNegativeThanPositive() {

      // Given
      boolean expectedIsWithinUpperBounds = false;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPosition(position)
            .withShape(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withDetectedPosition(detectePosition1, true)
            .withDetectedPosition(detectePosition2, false)
            .withDetectedPosition(detectePosition3, true)
            .withDetectedPosition(detectePosition4, true)
            .build();

      // When 
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then 
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   @Test
   void testCheckIfDetectedPosAreLeftOrRight_DetectionRightTurnLeft() {

      // Given
      boolean expectedIsWithinUpperBounds = true;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPosition(position)
            .withShape(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withDetectedPosition(detectePosition1, false)
            .withDetectedPosition(detectePosition2, false)
            .withDetectedPosition(detectePosition3, false)
            .withDetectedPosition(detectePosition4, false)
            .build();

      // Then
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   @Test
   void testCheckIfDetectedPosAreLeftOrRight_NoDetectionAtAll() {

      // Given
      boolean expectedIsWithinUpperBounds = true;
      Position position = Positions.of(5, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPosition(position)
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
      boolean expectedIsWithinUpperBounds = false;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPosition(position)
            .withShape(RectangleBuilder.builder()
                  .withCenter(Positions.of(5, 5))
                  .withHeight(10)
                  .withWidth(10)
                  .build())
            .withDetectedPosition(detectePosition1, true)
            .withDetectedPosition(detectePosition2, true)
            .withDetectedPosition(detectePosition3, true)
            .withDetectedPosition(detectePosition4, true)
            .build();

      // Then
      boolean actualIsWithinUpperBounds = tcb.shape.isWithinUpperBounds(tcb.detectedPositions, position);

      // Then
      assertThat(actualIsWithinUpperBounds, is(expectedIsWithinUpperBounds));
   }

   private static class TestCaseBuilder {

      private Position position;
      private List<Position> detectedPositions;
      private Shape shape;

      private TestCaseBuilder() {
         detectedPositions = new ArrayList<>();
      }

      private TestCaseBuilder withPosition(Position position) {
         this.position = position;
         return this;
      }

      private TestCaseBuilder withDetectedPosition(Position detectedPositionIn, boolean isWithinUpperBounds) {
         Position detectedPosition = spy(detectedPositionIn);
         detectedPositions.add(detectedPosition);
         doReturn(isWithinUpperBounds ? 10.0 : -10.0).when(detectedPosition).calcAngleRelativeTo(eq(position));
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
