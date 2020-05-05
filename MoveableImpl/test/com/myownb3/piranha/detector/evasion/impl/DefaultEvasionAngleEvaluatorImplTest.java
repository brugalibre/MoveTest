package com.myownb3.piranha.detector.evasion.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.detector.evasion.impl.DefaultEvasionAngleEvaluatorImpl.DefaultEvasionAngleEvaluatorBuilder;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

class DefaultEvasionAngleEvaluatorImplTest {

   @Test
   void testGetEvasionAngleRelative2_NoAvoidablePresent() {

      // Given
      double angleInc = 0.0;
      double expectedEvasionAngle = angleInc;
      double detectorAngle = 80;
      Position position = Positions.of(5, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAngleInc(angleInc)
            .withDetectorAngle(detectorAngle)
            .withPosition(position)
            .withDetectionAware()
            .build();

      // When 
      double actualEvasionAngle = tcb.angleEvaluatorImpl.getEvasionAngleRelative2(position);

      // Then 
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_EvasionAngleMorePositiveThanNegative() {

      // Given
      double angleInc = 5;
      double expectedEvasionAngle = -angleInc;
      double detectorAngle = 80;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAngleInc(angleInc)
            .withDetectorAngle(detectorAngle)
            .withPosition(position)
            .withAvoidable()
            .withDetectedPosition(detectePosition1, false)
            .withDetectedPosition(detectePosition2, false)
            .withDetectedPosition(detectePosition3, false)
            .withDetectedPosition(detectePosition4, true)
            .withDetectionAware()
            .build();

      // When 
      double actualEvasionAngle = tcb.angleEvaluatorImpl.getEvasionAngleRelative2(position);

      // Then 
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_EvasionAngleMoreNegativeThanPositive() {

      // Given
      double angleInc = 5;
      double expectedEvasionAngle = angleInc;
      double detectorAngle = 80;
      Position position = Positions.of(5, 5);
      Position detectePosition1 = Positions.of(15, 5);
      Position detectePosition2 = Positions.of(25, 5);
      Position detectePosition3 = Positions.of(35, 5);
      Position detectePosition4 = Positions.of(45, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAngleInc(angleInc)
            .withDetectorAngle(detectorAngle)
            .withPosition(position)
            .withAvoidable()
            .withDetectedPosition(detectePosition1, true)
            .withDetectedPosition(detectePosition2, false)
            .withDetectedPosition(detectePosition3, true)
            .withDetectedPosition(detectePosition4, true)
            .withDetectionAware()
            .build();

      // When 
      double actualEvasionAngle = tcb.angleEvaluatorImpl.getEvasionAngleRelative2(position);

      // Then 
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   private static class TestCaseBuilder {

      private DetectionAware detectionAware;
      private double angleInc;
      private double detectorAngle;
      private DefaultEvasionAngleEvaluatorImpl angleEvaluatorImpl;
      private Avoidable avoidable;
      private Position position;
      private List<Position> detectePositions;

      private TestCaseBuilder() {
         detectePositions = new ArrayList<>();
      }

      private TestCaseBuilder withPosition(Position position) {
         this.position = position;
         return this;
      }

      private TestCaseBuilder withAngleInc(double angleInc) {
         this.angleInc = angleInc;
         return this;
      }

      private TestCaseBuilder withDetectedPosition(Position detectedPosition, boolean isWithinUpperBounds) {
         when(avoidable.getShape().isWithinUpperBounds(eq(detectedPosition), eq(detectorAngle))).thenReturn(isWithinUpperBounds);
         detectePositions.add(detectedPosition);
         return this;
      }

      private TestCaseBuilder withAvoidable() {
         Shape shape = mock(Shape.class);
         avoidable = mock(Avoidable.class);
         when(avoidable.getShape()).thenReturn(shape);
         return this;
      }

      private TestCaseBuilder withDetectorAngle(double detectorAngle) {
         this.detectorAngle = detectorAngle;
         return this;
      }

      private TestCaseBuilder withDetectionAware() {
         this.detectionAware = mock(DetectionAware.class);
         when(detectionAware.getDetectedPositions4GridElement(eq(avoidable))).thenReturn(detectePositions);
         when(detectionAware.getNearestEvasionAvoidable(eq(position))).thenReturn(Optional.ofNullable(avoidable));
         return this;
      }

      private TestCaseBuilder build() {
         angleEvaluatorImpl = DefaultEvasionAngleEvaluatorBuilder.builder()
               .withDetectorAngle(detectorAngle)
               .withAngleInc(angleInc)
               .build();
         angleEvaluatorImpl.setDetectionAware(detectionAware);
         return this;
      }

   }

}
