package com.myownb3.piranha.core.detector.evasion.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.detectionaware.DetectionAware;
import com.myownb3.piranha.core.detector.evasion.impl.DefaultEvasionAngleEvaluatorImpl.DefaultEvasionAngleEvaluatorBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class DefaultEvasionAngleEvaluatorImplTest {

   @Test
   void testGetEvasionAngleRelative2_NoGridElementPresent() {

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
      private Obstacle obstacle;
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
         when(detectionAware.getDetectedPositions4GridElement(eq(obstacle))).thenReturn(detectePositions);
         when(detectionAware.getNearestEvasionGridElement(eq(position))).thenReturn(Optional.ofNullable(obstacle));
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
