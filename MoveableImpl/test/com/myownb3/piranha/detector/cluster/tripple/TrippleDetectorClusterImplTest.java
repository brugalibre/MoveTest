package com.myownb3.piranha.detector.cluster.tripple;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.IDetector;
import com.myownb3.piranha.detector.cluster.DetectingStrategyHandler;
import com.myownb3.piranha.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigBuilder;

public class TrippleDetectorClusterImplTest {

   @Test
   void testBuildTrippleClusterDetectorImpl() {

      // Given
      int detectorReach = 30;
      int evasionDistance = 25;
      int detectorAngle = 70;
      double evasionAngle = 55.0;
      EvasionStateMachineConfig centerDetectorConfig =
            buildEvasionStateMachineConfigBuilder(detectorReach, evasionDistance, detectorAngle, 100);
      EvasionStateMachineConfig sideDetectorConfig =
            buildEvasionStateMachineConfigBuilder(detectorReach / 3, evasionDistance / 3, detectorAngle, 100);

      // When
      TrippleDetectorCluster trippleClusterDetectorCluster =
            TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(centerDetectorConfig, sideDetectorConfig);
      IDetectorInfo leftSideDetector = trippleClusterDetectorCluster.getLeftSideDetector();
      IDetectorInfo rightSideDetector = trippleClusterDetectorCluster.getRightSideDetector();
      IDetectorInfo centerDetector = trippleClusterDetectorCluster.getCenterDetector();

      // Then
      assertThat(centerDetector.getOffsetAngle(), is(0.0));
      assertThat(leftSideDetector.getOffsetAngle(), is(-70.0));
      assertThat(rightSideDetector.getOffsetAngle(), is(70.0));
      assertThat(rightSideDetector.getDetector().getDetectorAngle(), is(evasionAngle));
      assertThat(leftSideDetector.getDetector().getDetectorAngle(), is(evasionAngle));
   }

   @Test
   void testDetectObject() {
      // Given
      Position position = Positions.of(5, 5);
      Position detPosition = Positions.of(0, 0);
      DetectingStrategyHandler detectingStrategyHandler = spy(new TestDetectingStrategyHandler());
      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withCenterDetector(mock(IDetector.class))
            .withLeftSideDetector(mock(IDetector.class), 5)
            .withRightSideDetector(mock(IDetector.class), -5)
            .withDetectionStrategyHandler(detectingStrategyHandler)
            .build();
      GridElement gridElement = mock(GridElement.class);

      // When
      trippleClusterDetectorCluster.detectObject(gridElement, position, detPosition);

      // Then
      verify(detectingStrategyHandler).detectObjectAlongPath(eq(gridElement), eq(Collections.singletonList(position)), eq(detPosition));
   }

   @Test
   void testDetectObjectAlongPath() {
      // Given
      Position position = Positions.of(5, 5);
      DetectingStrategyHandler detectingStrategyHandler = spy(new TestDetectingStrategyHandler());
      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withCenterDetector(mock(IDetector.class))
            .withLeftSideDetector(mock(IDetector.class), 5)
            .withRightSideDetector(mock(IDetector.class), -5)
            .withDetectionStrategyHandler(detectingStrategyHandler)
            .build();
      GridElement gridElement = mock(GridElement.class);

      // When
      trippleClusterDetectorCluster.detectObjectAlongPath(gridElement, Collections.emptyList(), position);

      // Then
      verify(detectingStrategyHandler).detectObjectAlongPath(eq(gridElement), eq(Collections.emptyList()), eq(position));
   }

   @Test
   void testGetEvasionAngleRelative2() {
      // Given
      Position position = Positions.of(5, 5);
      DetectingStrategyHandler detectingStrategyHandler = spy(new TestDetectingStrategyHandler());
      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withDetectionStrategyHandler(detectingStrategyHandler)
            .build();

      // When
      trippleClusterDetectorCluster.getEvasionAngleRelative2(position);

      // Then
      verify(detectingStrategyHandler).getEvasionAngleRelative2(eq(position));
   }

   @Test
   void testInit() {
      // Given
      DetectingStrategyHandler detectingStrategyHandler = spy(new TestDetectingStrategyHandler());
      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withDetectionStrategyHandler(detectingStrategyHandler)
            .build();

      // When
      trippleClusterDetectorCluster.init();

      // Then
      verify(detectingStrategyHandler).init();
   }

   @Test
   void testGetEvasionDelayDistance() {
      // Given
      DetectingStrategyHandler detectingStrategyHandler = spy(new TestDetectingStrategyHandler());
      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withDetectionStrategyHandler(detectingStrategyHandler)
            .build();

      // When
      trippleClusterDetectorCluster.getEvasionDelayDistance();

      // Then
      verify(detectingStrategyHandler).getEvasionDistance4DetectingDetector();
   }

   @Test
   void testIsEvasion() {
      // Given
      boolean isEvasion = true;
      boolean expectedIsEvasion = true;
      GridElement gridElement = mock(GridElement.class);

      IDetector centerDetectorInfo = mockIDetectorIsEvasion(isEvasion, gridElement);

      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withCenterDetector(centerDetectorInfo)
            .withLeftSideDetector(centerDetectorInfo, 5)
            .withRightSideDetector(centerDetectorInfo, -5)
            .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS)
            .withAutoDetectionStrategyHandler()
            .build();

      // When
      boolean actualIsEvasion = trippleClusterDetectorCluster.isEvasion(gridElement);

      // Then
      assertThat(actualIsEvasion, is(expectedIsEvasion));
   }

   @Test
   void testIsEvasion_NoEvasion() {
      // Given
      boolean expectedIsEvasion = false;
      GridElement gridElement = mock(GridElement.class);

      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withCenterDetector(mockIDetectorIsEvasion(false, gridElement))
            .withLeftSideDetector(mockIDetectorIsEvasion(false, gridElement), 5)
            .withRightSideDetector(mockIDetectorIsEvasion(false, gridElement), -5)
            .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS)
            .withAutoDetectionStrategyHandler()
            .build();

      // When
      boolean actualIsEvasion = trippleClusterDetectorCluster.isEvasion(gridElement);

      // Then
      assertThat(actualIsEvasion, is(expectedIsEvasion));
   }

   @Test
   void testHasObjectDetected() {
      // Given
      boolean expectedHasObjectDetected = true;
      boolean expectedIsEvasion = true;
      GridElement gridElement = mock(GridElement.class);

      TrippleDetectorCluster trippleClusterDetectorCluster = TrippleDetectorClusterBuilder.builder()
            .withCenterDetector(mockIDetectorHasObjectDetected(false, gridElement))
            .withLeftSideDetector(mockIDetectorHasObjectDetected(false, gridElement), 5)
            .withRightSideDetector(mockIDetectorHasObjectDetected(expectedHasObjectDetected, gridElement), -5)
            .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS)
            .withAutoDetectionStrategyHandler()
            .build();

      // When
      boolean actualIsEvasion = trippleClusterDetectorCluster.hasObjectDetected(gridElement);

      // Then
      assertThat(actualIsEvasion, is(expectedIsEvasion));
   }

   @Test
   void testGetEvasionRange() {
      // Given
      int detectorReach = 30;
      int evasionDistance = 25;
      EvasionStateMachineConfig centerDetectorConfig = buildEvasionStateMachineConfigBuilder(detectorReach, evasionDistance, 70, 50);
      EvasionStateMachineConfig sideDetectorConfig = buildEvasionStateMachineConfigBuilder(detectorReach / 3, evasionDistance / 3, 70, 50);

      // When
      TrippleDetectorCluster trippleClusterDetectorCluster =
            TrippleDetectorClusterBuilder.buildDefaultDetectorCluster(centerDetectorConfig, sideDetectorConfig);

      // Then
      assertThat(trippleClusterDetectorCluster.getDetectorRange(), is(detectorReach));
      assertThat(trippleClusterDetectorCluster.getEvasionRange(), is(evasionDistance));
   }

   private EvasionStateMachineConfig buildEvasionStateMachineConfigBuilder(int detectorReach, int evasionDistance, int detectorAngle,
         int evasionAngle) {
      return EvasionStateMachineConfigBuilder.builder()
            .withReturningAngleIncMultiplier(1)
            .withOrientationAngle(10)
            .withReturningMinDistance(0.06)
            .withReturningAngleMargin(0.7d)
            .withDetectorReach(detectorReach)
            .withEvasionDistance(evasionDistance)
            .withPassingDistance(25)
            .withDetectorAngle(detectorAngle)
            .withEvasionAngle(evasionAngle)
            .withEvasionAngleInc(2)
            .withPostEvasionReturnAngle(4)
            .build();
   }

   private static IDetector mockIDetectorIsEvasion(boolean isEvasion, GridElement gridElement) {
      IDetector iDetector = mock(IDetector.class);
      Mockito.when(iDetector.isEvasion(gridElement)).thenReturn(isEvasion);
      Mockito.when(iDetector.isEvasion(gridElement)).thenReturn(isEvasion);
      return iDetector;
   }

   private static IDetector mockIDetectorHasObjectDetected(boolean hasObjectDetected, GridElement gridElement) {
      IDetector iDetector = mock(IDetector.class);
      Mockito.when(iDetector.hasObjectDetected(gridElement)).thenReturn(hasObjectDetected);
      return iDetector;
   }

   private static class TestDetectingStrategyHandler implements DetectingStrategyHandler {

      @Override
      public void detectObjectAlongPath(GridElement gridElement, List<Position> gridElementPath, Position detectorPosition) {
         // ignore
      }

      @Override
      public double getEvasionAngleRelative2(Position position) {
         return 0;
      }

      @Override
      public Integer getEvasionDistance4DetectingDetector() {
         return 0;
      }

      @Override
      public void init() {
         // ignore
      }
   }
}
