package com.myownb3.piranha.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;

class DetectorImplTest {

   @Test
   void testGetEvasionDistance() {

      // Given
      double evasionAngle = 10;
      double detectorAngle = 10;
      DetectorImpl detector = new DetectorImpl(5, 5, detectorAngle, evasionAngle, 5);

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
      DetectorImpl detector = new DetectorImpl();
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
      DetectorImpl detector = new DetectorImpl();
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

   @Test
   void testGetNearestEvasionGridElement() {

      // Given
      DetectorImpl detector = new DetectorImpl();
      Grid grid = GridBuilder.builder(100, 100)
            .build();
      Position position = Positions.of(0, 0);
      Position positionG1 = Positions.of(50, 50);
      Position positionG2 = Positions.of(49, 49);
      Position positionG3 = Positions.of(90, 90);

      Obstacle g1 = new ObstacleImpl(grid, positionG1);
      Obstacle g2 = new ObstacleImpl(grid, positionG2);
      Obstacle g3 = new ObstacleImpl(grid, positionG3);
      List<Avoidable> obstacles = Arrays.asList(g1, g2, g3);
      Collections.shuffle(obstacles);

      // When
      Optional<Avoidable> actualNearestGridElem = detector.getNearestEvasionAvoidable(position, obstacles);

      // Then
      Assert.assertThat(actualNearestGridElem.get(), is(g2));
   }
}
