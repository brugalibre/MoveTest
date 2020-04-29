package com.myownb3.piranha.detector.detectionaware.impl;

import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.DetectionResult;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;

public class DefaultDetectionAwareTest {

   @Test
   void testGetNearestEvasionGridElement() {

      // Given
      DefaultDetectionAware defaultDetectionAware = new DefaultDetectionAware();
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
      Optional<Avoidable> actualNearestGridElem = defaultDetectionAware.getNearestGridElement(position, obstacles);

      // Then
      MatcherAssert.assertThat(actualNearestGridElem.get(), is(g2));
   }

   @Test
   void testGetNearestDetectedGridElement() {

      // Given
      DefaultDetectionAware defaultDetectionAware = new DefaultDetectionAware();
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

      // Simulate the detecting of those GridElements
      List<DetectionResult> detectionResultsG1 = buildDetectionResults(positionG1);
      List<DetectionResult> detectionResultsG2 = buildDetectionResults(positionG2);
      List<DetectionResult> detectionResultsG3 = buildDetectionResults(positionG3);
      defaultDetectionAware.checkGridElement4Detection(g1, detectionResultsG1);
      defaultDetectionAware.checkGridElement4Detection(g2, detectionResultsG2);
      defaultDetectionAware.checkGridElement4Detection(g3, detectionResultsG3);

      // When
      Optional<GridElement> nearestDetectedGridElement = defaultDetectionAware.getNearestDetectedGridElement(position);

      // Then
      MatcherAssert.assertThat(nearestDetectedGridElement.get(), is(g2));
   }

   private List<DetectionResult> buildDetectionResults(Position positionG1) {
      DetectionResult detectionResult = new DetectionResult(false, true, positionG1);
      List<DetectionResult> detectionResults = Collections.singletonList(detectionResult);
      return detectionResults;
   }

}
