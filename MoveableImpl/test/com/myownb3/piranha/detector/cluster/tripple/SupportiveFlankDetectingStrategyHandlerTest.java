package com.myownb3.piranha.detector.cluster.tripple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;

class SupportiveFlankDetectingStrategyHandlerTest {

   @Test
   void testInit() {

      // Given
      int expectedEvasionDelayDistance = 50;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(true, true, expectedEvasionDelayDistance)
            .withLeftSideDetector(-70, false, false, 0)
            .withRightSideDetector(70, false, false, 0)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      assertThat(tcb.handler.getEvasionDistance4DetectingDetector(), is(expectedEvasionDelayDistance));
   }

   @Test
   void testCurrentDetectingDetector_CenterDetectorDetects() {

      // Given
      int expectedEvasionDelayDistance = 50;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(true, true, expectedEvasionDelayDistance)
            .withLeftSideDetector(-70, false, false, 0)
            .withRightSideDetector(70, false, false, 0)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      assertThat(tcb.handler.getEvasionDistance4DetectingDetector(), is(expectedEvasionDelayDistance));
   }

   @Test
   void testCurrentDetectingDetector_RighSideDetectorDetects() {

      // Given
      int expectedEvasionDelayDistance = 50;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(false, false, 10)
            .withLeftSideDetector(-70, false, false, 0)
            .withRightSideDetector(70, true, true, expectedEvasionDelayDistance)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      assertThat(tcb.handler.getEvasionDistance4DetectingDetector(), is(expectedEvasionDelayDistance));
   }

   @Test
   void testCurrentDetectingDetector_NoDetectorDetects() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(false, false, 2)
            .withLeftSideDetector(-70, false, false, 4)
            .withRightSideDetector(70, false, false, 6)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      assertThat(tcb.handler.getEvasionDistance4DetectingDetector(), is(nullValue()));
   }

   @Test
   void testCurrentDetectingDetector_LeftSideDetectorDetects() {

      // Given
      int expectedEvasionDelayDistance = 50;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(false, false, 10)
            .withLeftSideDetector(-70, true, true, expectedEvasionDelayDistance)
            .withRightSideDetector(70, false, false, 0)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      assertThat(tcb.handler.getEvasionDistance4DetectingDetector(), is(expectedEvasionDelayDistance));
   }

   @Test
   void testDetectObjectAlongPath_CenterDetectorDetects() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(true, false, 0)
            .withLeftSideDetector(-70, false, false, 0)
            .withRightSideDetector(70, false, false, 0)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      verify(tcb.leftSideDetector.getDetector()).detectObjectAlongPath(any(), any(), any());
      verify(tcb.rightSideDetector.getDetector()).detectObjectAlongPath(any(), any(), any());
      verify(tcb.centerDetectorInfo.getDetector()).detectObjectAlongPath(any(), any(), any());
   }

   @Test
   void testDetectObjectAlongPath_LeftSideDetectorDetects() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(false, false, 0)
            .withLeftSideDetector(-70, true, true, 0)
            .withRightSideDetector(70, false, false, 0)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      verify(tcb.leftSideDetector.getDetector()).detectObjectAlongPath(any(), any(), any());
      verify(tcb.rightSideDetector.getDetector(), never()).detectObjectAlongPath(any(), any(), any());
      verify(tcb.centerDetectorInfo.getDetector(), never()).detectObjectAlongPath(any(), any(), any());

   }

   @Test
   void testDetectObjectAlongPath_RightDetectorDetects() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(false, false, 0)
            .withLeftSideDetector(-70, false, false, 0)
            .withRightSideDetector(70, true, true, 0)
            .build();

      // When
      tcb.handler.detectObjectAlongPath(tcb.gridElement, tcb.gridElementPath, tcb.zeroDetectorPosition);

      // Then
      verify(tcb.leftSideDetector.getDetector()).detectObjectAlongPath(any(), any(), any());
      verify(tcb.rightSideDetector.getDetector()).detectObjectAlongPath(any(), any(), any());
      verify(tcb.centerDetectorInfo.getDetector(), never()).detectObjectAlongPath(any(), any(), any());
   }

   @Test
   void testGetEvasionAngleRelative2_LeftSideDetector() {

      // Given
      double expectedEvasionAngleRelative2 = 5.0;
      double evaluatedEvasionAngleRelative2 = -5.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withDetectingCenterDetector(30)
            .withDetectingLeftSideDetector(evaluatedEvasionAngleRelative2)
            .withDetectingRightSideDetector(50)
            .build();

      // When
      double actualEvasionAngleRelative2 = tcb.handler.getEvasionAngleRelative2(Positions.of(0, 0));

      // Then
      assertThat(actualEvasionAngleRelative2, is(expectedEvasionAngleRelative2));
   }

   @Test
   void testGetEvasionAngleRelative2_CenterSideDetector() {

      // Given
      double expectedEvasionAngleRelative2 = 5.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withDetectingCenterDetector(expectedEvasionAngleRelative2)
            .withDetectingLeftSideDetector(0)
            .withDetectingRightSideDetector(0)
            .build();

      // When
      double actualEvasionAngleRelative2 = tcb.handler.getEvasionAngleRelative2(Positions.of(0, 0));

      // Then
      assertThat(actualEvasionAngleRelative2, is(expectedEvasionAngleRelative2));
   }

   @Test
   void testGetEvasionAngleRelative2_RighSideDetector() {

      // Given
      double expectedEvasionAngleRelative2 = -5.0;
      double evaluatedEvasionAngleRelative2 = 5.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withDetectingCenterDetector(50)
            .withDetectingLeftSideDetector(0)
            .withDetectingRightSideDetector(evaluatedEvasionAngleRelative2)
            .build();

      // When
      double actualEvasionAngleRelative2 = tcb.handler.getEvasionAngleRelative2(Positions.of(0, 0));

      // Then
      assertThat(actualEvasionAngleRelative2, is(expectedEvasionAngleRelative2));
   }

   @Test
   void testGetEvasionAngleRelative2_NoDetector() {

      // Given
      double expectedEvasionAngleRelative2 = 0.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGridElement()
            .withGridElementPath()
            .withZeroDetectorPosition(Positions.of(0, 0))
            .withCenterDetector(false, false, 0)
            .withLeftSideDetector(-70, false, false, 0)
            .withRightSideDetector(70, false, false, 0)
            .build();

      // When
      double actualEvasionAngleRelative2 = tcb.handler.getEvasionAngleRelative2(Positions.of(0, 0));

      // Then
      assertThat(actualEvasionAngleRelative2, is(expectedEvasionAngleRelative2));
   }

   private static class TestCaseBuilder {
      private GridElement gridElement;
      private List<Position> gridElementPath;
      private Position zeroDetectorPosition;
      private SupportiveFlanksDetectingStrategyHandler handler;
      private IDetectorInfo rightSideDetector;
      private IDetectorInfo leftSideDetector;
      private IDetectorInfo centerDetectorInfo;

      public TestCaseBuilder() {
         handler = new SupportiveFlanksDetectingStrategyHandler();
      }

      private TestCaseBuilder withGridElement() {
         this.gridElement = mock(GridElement.class);
         return this;
      }

      public TestCaseBuilder withCenterDetector(boolean hasObjectDetected, boolean isEvasion, int evasionDelayDistance) {
         this.centerDetectorInfo = createIDetectorInfoImpl(gridElement, 0, hasObjectDetected, isEvasion, evasionDelayDistance, 0);
         return this;
      }

      public TestCaseBuilder withLeftSideDetector(double offsetAngle, boolean hasObjectDetected, boolean isEvasion, int evasionDelayDistance) {
         this.leftSideDetector = createIDetectorInfoImpl(gridElement, offsetAngle, hasObjectDetected, isEvasion, evasionDelayDistance, 0);
         return this;
      }

      public TestCaseBuilder withRightSideDetector(double offsetAngle, boolean hasObjectDetected, boolean isEvasion, int evasionDelayDistance) {
         this.rightSideDetector = createIDetectorInfoImpl(gridElement, offsetAngle, hasObjectDetected, isEvasion, evasionDelayDistance, 0);
         return this;
      }

      public TestCaseBuilder withDetectingCenterDetector(double evasionAngleRelative) {
         this.centerDetectorInfo =
               createIDetectorInfoImpl(gridElement, 0, evasionAngleRelative != 0.0, evasionAngleRelative != 0.0, 0, evasionAngleRelative);
         return this;
      }

      public TestCaseBuilder withDetectingLeftSideDetector(double evasionAngleRelative) {

         this.leftSideDetector =
               createIDetectorInfoImpl(gridElement, 70, evasionAngleRelative != 0.0, evasionAngleRelative != 0.0, 0, evasionAngleRelative);
         return this;
      }

      public TestCaseBuilder withDetectingRightSideDetector(double evasionAngleRelative) {
         this.rightSideDetector =
               createIDetectorInfoImpl(gridElement, -70, evasionAngleRelative != 0.0, evasionAngleRelative != 0.0, 0, evasionAngleRelative);
         return this;
      }

      public TestCaseBuilder withZeroDetectorPosition(Position zeroDetectorPosition) {
         this.zeroDetectorPosition = zeroDetectorPosition;
         return this;
      }

      public TestCaseBuilder withGridElementPath() {
         this.gridElementPath = Collections.emptyList();
         return this;
      }

      public TestCaseBuilder build() {
         handler.setCenterDetector(centerDetectorInfo);
         handler.setLeftSideDetector(leftSideDetector);
         handler.setRightSideDetector(rightSideDetector);

         handler.setCurrentEvasionDetectingDetector(gridElement);
         return this;
      }
   }

   private static IDetectorInfo createIDetectorInfoImpl(GridElement gridElement, double offsetAngle, boolean hasObjectDetected, boolean isEvasion,
         int evasionDelayDistance, double evasionAngleRelative) {
      IDetector detector = mock(IDetector.class);
      doNothing().when(detector).detectObjectAlongPath(any(), any(), any());
      when(detector.hasObjectDetected(eq(gridElement))).thenReturn(hasObjectDetected);
      when(detector.isEvasion(eq(gridElement))).thenReturn(isEvasion);
      when(detector.getEvasionDelayDistance()).thenReturn(evasionDelayDistance);
      when(detector.getEvasionAngleRelative2(any())).thenReturn(evasionAngleRelative);
      return IDetectorInfoImpl.of(detector, offsetAngle);
   }
}
