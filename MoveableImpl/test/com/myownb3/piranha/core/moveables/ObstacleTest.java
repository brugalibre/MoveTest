/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;

/**
 * @author Dominic
 *
 */
class ObstacleTest {

   @Test
   public void testMovebaleRecognizesObjectTrueRightSideOfNorht() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(2, 7))
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      MoveableBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(1, 1))
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .build();
      boolean isRecognized = true;

      // When
      boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

      // Then
      MatcherAssert.assertThat(hasObjectRecognized, is(isRecognized));
   }

   @Test
   public void testMovebaleRecognizesObjectTrueLeftSideOfNorht() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(-1, 7))
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      MoveableBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(1, 1))
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .build();
      boolean isRecognized = true;

      // When
      boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

      // Then
      MatcherAssert.assertThat(hasObjectRecognized, is(isRecognized));
   }

   @Test
   public void testMovebaleRecognizesObjectFalseWrongAngle() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(4, 7))
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();

      MoveableBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(1, 1))
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .build();

      boolean notRecognized = false;

      // When
      boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

      // Then
      MatcherAssert.assertThat(hasObjectRecognized, is(notRecognized));
   }

   @Test
   public void testMovebaleRecognizesObjectFalseOutOfReach() {

      // Given
      Grid grid = GridBuilder.builder(100, 100)
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(20, 70))
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      MoveableBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(1, 1))
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .build();
      boolean notRecognized = false;

      // When
      boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

      // Then
      MatcherAssert.assertThat(hasObjectRecognized, is(notRecognized));
   }
}
