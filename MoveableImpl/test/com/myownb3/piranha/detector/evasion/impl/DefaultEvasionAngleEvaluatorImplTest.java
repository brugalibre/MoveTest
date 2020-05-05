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

class DefaultEvasionAngleEvaluatorImplTest {

   @Test
   void testGetEvasionAngleRelative2_NoAvoidablePresent() {

      // Given
      double angleInc = 0.0;
      double expectedEvasionAngle = angleInc;
      Position position = Positions.of(5, 5);

      TestCaseBuilder tcb = new TestCaseBuilder()
            .withAngleInc(angleInc)
            .withPosition(position)
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

      private TestCaseBuilder withDetectionAware() {
         this.detectionAware = mock(DetectionAware.class);
         when(detectionAware.getDetectedPositions4GridElement(eq(avoidable))).thenReturn(detectePositions);
         when(detectionAware.getNearestEvasionAvoidable(eq(position))).thenReturn(Optional.ofNullable(avoidable));
         return this;
      }

      private TestCaseBuilder build() {
         angleEvaluatorImpl = DefaultEvasionAngleEvaluatorBuilder.builder()
               .withAngleInc(angleInc)
               .build();
         angleEvaluatorImpl.setDetectionAware(detectionAware);
         return this;
      }
   }
}
