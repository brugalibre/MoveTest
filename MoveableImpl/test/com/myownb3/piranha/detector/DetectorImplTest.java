package com.myownb3.piranha.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.detector.detectionaware.impl.DefaultDetectionAware;
import com.myownb3.piranha.detector.evasion.impl.DefaultEvasionAngleEvaluatorImpl.DefaultEvasionAngleEvaluatorBuilder;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;

class DetectorImplTest {
   @Test
   void testGetHasGridElementDetections() {

      // Given
      Position position = Positions.of(0, 0);
      DefaultDetectionAware detectionAware = spy(new DefaultDetectionAware());
      IDetector detector = DetectorBuilder.builder()
            .withDetectorReach(10)
            .withDetectorAngle(40)
            .withEvasionDistance(5)
            .withEvasionAngle(30)
            .withDetectionAware(detectionAware)
            .withDefaultEvasionAngleEvaluator(DefaultEvasionAngleEvaluatorBuilder.builder()
                  .withDetectorAngle(40)
                  .withAngleInc(4)
                  .build())
            .build();

      // When
      detector.hasGridElementDetected(position);

      // Then
      verify(detectionAware).getNearestDetectedGridElement(eq(position));
   }

   @Test
   void testGetEvasionDistance() {

      // Given
      double evasionAngle = 10;
      double detectorAngle = 10;
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withEvasionDistance(5)
            .withDetectorAngle(detectorAngle)
            .withEvasionAngle(evasionAngle)
            .withAngleInc(5)
            .build();

      // When
      double actualEvasionAngle = detector.getEvasionAngle();
      double actualDetectingAngle = detector.getDetectorAngle();

      // Then
      assertThat(actualEvasionAngle, is(evasionAngle));
      assertThat(actualDetectingAngle, is(detectorAngle));
   }

   @Test
   void testDetectObjectAlongPath_IsNotDetectingAndNotEvasion() {

      // Given
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 0);
      Position gridElementPos = Positions.of(0, 9);
      GridElement gridElement = mock(GridElement.class);

      // When
      detector.detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), detectorPosition);
      boolean actualIsEvasion = detector.isEvasion(gridElement);
      boolean hasObjectDetected = detector.hasObjectDetected(gridElement);

      // Then
      assertThat(actualIsEvasion, is(false));
      assertThat(hasObjectDetected, is(false));
   }

   @Test
   void testDetectObjectAlongPath_IsDetectingButNotEvasion() {

      // Given
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 0);
      Position gridElementPos = Positions.of(0, 1);
      GridElement gridElement = mock(GridElement.class);

      // When
      detector.detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), detectorPosition);
      boolean actualIsEvasion = detector.isEvasion(gridElement);
      boolean hasObjectDetected = detector.hasObjectDetected(gridElement);

      // Then
      assertThat(actualIsEvasion, is(false));
      assertThat(hasObjectDetected, is(true));
   }
}
