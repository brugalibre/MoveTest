package com.myownb3.piranha.core.detector.cluster.tripple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

public class StaticSupportiveFlanksDetectingStrategyHandlerTest {

   @Test
   void testGetEvasionAngleRelative2_NoDetectingDetector() {

      // Given
      StaticSupportiveFlanksDetectingStrategyHandler handler = new StaticSupportiveFlanksDetectingStrategyHandler();
      Position position = Positions.of(0, 0);
      double expectedAngle = 0.0;

      // When
      double actualEvasionAngleRelative2 = handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngleRelative2, is(expectedAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterDetectorHasEvasion_RightTurnRightSideDetectorIsDetecting_NotClean() {

      // Given
      double evaluatedEvasionAngle = 15.0;
      double expectedEvasionAngle = -evaluatedEvasionAngle;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, true, false, evaluatedEvasionAngle)
            .withLeftDetector(-70, false, false)
            .withRightDetector(-70, false, true)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      double actualEvasionAngle = tcb.handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterDetectorHasEvasion_RightTurnLeftDetectorIsDetecting_Clean() {

      // Given
      double expectedEvasionAngle = 15.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, true, false, expectedEvasionAngle)
            .withLeftDetector(-70, false, true)
            .withRightDetector(-70, false, false)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      double actualEvasionAngle = tcb.handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterDetectorHasEvasion_LeftTurnRightSideDetectorIsDetecting_NotClean() {

      // Given
      double evaluatedEvasionAngle = -15.0;
      double expectedEvasionAngle = -evaluatedEvasionAngle;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, true, false, expectedEvasionAngle)
            .withLeftDetector(-70, false, true)
            .withRightDetector(-70, false, false)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      double actualEvasionAngle = tcb.handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterDetectorHasEvasion_LeftTurnLeftSideDetectorIsDetecting_Clean() {

      // Given
      double expectedEvasionAngle = -15.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, true, false, expectedEvasionAngle)
            .withLeftDetector(-70, false, false)
            .withRightDetector(-70, false, true)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      double actualEvasionAngle = tcb.handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterDetectorHasEvasion_LeftAndRightSideDetectorIsDetecting() {

      // Given
      double expectedEvasionAngle = -15.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, true, false, expectedEvasionAngle)
            .withLeftDetector(-70, false, true)
            .withRightDetector(-70, false, true)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      double actualEvasionAngle = tcb.handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterDetectorHasEvasion_TurnLeft_LeftSideDetectorIsDetecting_ButRightSideIsNot() {

      // Given
      double evaluatedEvasionAngle = -15.0;
      double expectedEvasionAngle = -evaluatedEvasionAngle;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, true, false, evaluatedEvasionAngle)
            .withLeftDetector(-70, false, true)
            .withRightDetector(-70, false, false)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      double actualEvasionAngle = tcb.handler.getEvasionAngleRelative2(position);

      // Then
      assertThat(actualEvasionAngle, is(expectedEvasionAngle));
   }

   @Test
   void testGetEvasionAngleRelative2_LeftSideDetectorHasEvasion() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, false)
            .withLeftDetector(-70, true, false)
            .withRightDetector(-70, false, false)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      tcb.handler.getEvasionAngleRelative2(position);

      // Then
      verify(tcb.leftDetectorInfo.getDetector()).getEvasionAngleRelative2(eq(position));
   }

   @Test
   void testGetEvasionAngleRelative2_RightSideDetectorHasEvasion() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withCenterDetector(0, false)
            .withLeftDetector(-70, false, false)
            .withRightDetector(-70, true, false)
            .build();
      Position position = Positions.of(0, 0);

      // When
      tcb.handler.setCurrentEvasionDetectingDetector(mock(GridElement.class));
      tcb.handler.getEvasionAngleRelative2(position);

      // Then
      verify(tcb.rightDetectorInfo.getDetector()).getEvasionAngleRelative2(eq(position));
   }

   private static class TestCaseBuilder {
      private IDetectorInfo centerDetectorInfo;
      private IDetectorInfo leftDetectorInfo;
      private IDetectorInfo rightDetectorInfo;
      private StaticSupportiveFlanksDetectingStrategyHandler handler;

      private TestCaseBuilder withCenterDetector(double offsetValue, boolean hasEvasion) {
         centerDetectorInfo = mockIDetectorInfo(offsetValue, false, hasEvasion, 0);
         return this;
      }

      private TestCaseBuilder withLeftDetector(double offsetValue, boolean hasEvasion, boolean hasDetection) {
         leftDetectorInfo = mockIDetectorInfo(offsetValue, hasDetection, hasEvasion, 0);
         return this;
      }

      private TestCaseBuilder withRightDetector(double offsetValue, boolean hasEvasion, boolean hasDetection) {
         rightDetectorInfo = mockIDetectorInfo(offsetValue, hasDetection, hasEvasion, 0);
         return this;
      }

      private TestCaseBuilder withCenterDetector(double offsetValue, boolean hasEvasion, boolean hasDetection, double evaluatedEvasionAngle) {
         centerDetectorInfo = mockIDetectorInfo(offsetValue, hasDetection, hasEvasion, evaluatedEvasionAngle);
         return this;
      }

      private TestCaseBuilder build() {
         this.handler = new StaticSupportiveFlanksDetectingStrategyHandler();
         handler.setCenterDetector(centerDetectorInfo);
         handler.setLeftSideDetector(leftDetectorInfo);
         handler.setRightSideDetector(rightDetectorInfo);
         return this;
      }
   }

   private static IDetectorInfo mockIDetectorInfo(double offsetValue, boolean hasDetection, boolean hasEvasion, double evaluatedEvasionAngle) {
      IDetectorInfo iDetectorInfo = mock(IDetectorInfo.class);
      IDetector detector = mock(IDetector.class);
      when(iDetectorInfo.getOffsetAngle()).thenReturn(offsetValue);
      when(iDetectorInfo.getDetector()).thenReturn(detector);
      when(iDetectorInfo.getDetector().hasGridElementDetectedAtPosition(any())).thenReturn(hasDetection);
      when(iDetectorInfo.getDetector().isEvasion(any())).thenReturn(hasEvasion);
      when(iDetectorInfo.getDetector().getEvasionAngleRelative2(any())).thenReturn(evaluatedEvasionAngle);
      return iDetectorInfo;
   }
}
