package com.myownb3.piranha.core.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.detectionaware.impl.DefaultDetectionAware;
import com.myownb3.piranha.core.detector.evasion.impl.DefaultEvasionAngleEvaluatorImpl.DefaultEvasionAngleEvaluatorBuilder;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;

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
                  .withAngleInc(4)
                  .build())
            .build();

      // When
      detector.hasGridElementDetectedAtPosition(position);

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

   @Ignore
   void testDetectObjectAlongPath_CircleIsInsideDetectionAngle_1Quadrant() {

      // Given
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 1);
      Position gridElementPos = Positions.of(Directions.S, -1.49, 4);
      GridElement gridElement = mock(GridElement.class);

      // When
      detector.detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), detectorPosition);
      boolean hasObjectDetected = detector.hasObjectDetected(gridElement);

      // Then
      assertThat(hasObjectDetected, is(true));
   }

   @Test
   void testDetectObjectAlongPath_CircleIsOutsideDetectionAngle_1Quadrant() {

      // Given
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 1);
      Position gridElementPos = Positions.of(Directions.S, 1.51, 4);
      GridElement gridElement = mock(GridElement.class);

      // When
      detector.detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), detectorPosition);
      boolean hasObjectDetected = detector.hasObjectDetected(gridElement);

      // Then
      assertThat(hasObjectDetected, is(not(true)));
   }

   @Test
   void testDetectObjectAlongPath_CircleIsOutsideDetectionAngle_2Quadrant() {

      // Given
      boolean expectedIsDetected = false;
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 0);
      Position gridElementPos = Positions.of(-5, 5);
      GridElement gridElement = mock(GridElement.class);

      // When
      detector.detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), detectorPosition);
      boolean hasObjectDetected = detector.hasObjectDetected(gridElement);

      // Then
      assertThat(hasObjectDetected, is(expectedIsDetected));
   }

   //   @Test
   void testDetectObjectAlongPath_CircleIsInsideDetectionAngle_2Quadrant() {

      // Given
      boolean expectedIsDetected = true;
      DetectorImpl detector = DetectorBuilder.builder()
            .withDetectorReach(10)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Position detectorPosition = Positions.of(0, 0);
      Position gridElementPos = Positions.of(-4, 5);
      GridElement gridElement = mock(GridElement.class);

      // When
      detector.detectObjectAlongPath(gridElement, Collections.singletonList(gridElementPos), detectorPosition);
      boolean hasObjectDetected = detector.hasObjectDetected(gridElement);

      // Then
      assertThat(hasObjectDetected, is(expectedIsDetected));
   }
}
