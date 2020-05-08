package com.myownb3.piranha.application.maze;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.core.detector.collision.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;

class MazeRunnerBuilderTest {

   @Test
   void testBuildMazeRunner() {

      // Given
      int width = 1000;
      int minX = 30;
      int expectedHeight = width - minX;
      int evasionDistance = 45;

      // When
      Position startPos = Positions.of(75, 75);
      EndPosition endPosition = EndPositions.of(500, 450);
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withEvasionStateMachineConfig(60, evasionDistance)
            .withTrippleDetectorCluster(DetectorConfigBuilder.builder()
                  .withDetectorReach(60)
                  .withEvasionDistance(evasionDistance)
                  .withDetectorAngle(60)
                  .withEvasionAngle(45)
                  .withEvasionAngleInc(2)
                  .build(),
                  DetectorConfigBuilder.builder()
                        .withDetectorReach(25)
                        .withEvasionDistance(5)
                        .withDetectorAngle(60)
                        .withEvasionAngle(45)
                        .withEvasionAngleInc(2)
                        .build())
            .withStartPos(startPos)
            .withMaze(MazeBuilder.builder()
                  .withGrid(MirrorGridBuilder.builder()
                        .withMaxX(width)
                        .withMaxY(width)
                        .withMinX(minX)
                        .withMinY(minX)
                        .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
                        .build())
                  .withStartPos(Positions.of(50, 50))
                  .withEndPosition(endPosition)
                  .withCorridor(2)
                  .withCorridorWidth(2)
                  .withSegmentLenth(2)
                  .appendCorridorSegment()
                  .build()
                  .build())
            .withMoveableController(res -> {
            })
            .build();

      // Then
      assertThat(mazeRunner.getGrid().getDimension().getHeight(), is(expectedHeight));
      assertThat(mazeRunner.getConfig().getEvasionDistance(), is(evasionDistance));
      assertThat(mazeRunner.getDetectorCluster(), is(notNullValue()));
      assertThat(mazeRunner.getMoveableController(), is(notNullValue()));
   }

   @Test
   void testBuildMazeRunWithCustomMaze() {

      // Given
      Grid grid = mock(Grid.class);
      Position startPos = Positions.of(75, 75);
      EndPosition endPosition = EndPositions.of(500, 450);

      // When
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withEvasionStateMachineConfig(60, 45)
            .withTrippleDetectorCluster(DetectorConfigBuilder.builder()
                  .withDetectorReach(60)
                  .withEvasionDistance(45)
                  .withDetectorAngle(60)
                  .withEvasionAngle(45)
                  .withEvasionAngleInc(2)
                  .build(),
                  DetectorConfigBuilder.builder()
                        .withDetectorReach(25)
                        .withEvasionDistance(5)
                        .withDetectorAngle(60)
                        .withEvasionAngle(45)
                        .withEvasionAngleInc(2)
                        .build())
            .withStartPos(startPos)
            .withMaze(MazeBuilder.builder()
                  .withGrid(grid)
                  .withStartPos(Positions.of(0, 0))
                  .withEndPosition(endPosition)
                  .withCorridor(2)
                  .withCorridorWidth(2)
                  .withSegmentLenth(2)
                  .appendCorridorSegment()
                  .build()
                  .build())
            .withMoveableController(res -> {
            })
            .build();

      // Then
      assertThat(mazeRunner.getAllGridElements().size(), is(3));// three elements: 2 CollidorSegments and 1 EndPositionGridElement
   }

   @Test
   void testBuildMazeRun() {

      // Given
      Grid grid = mock(Grid.class);
      List<Position> expectedPositionList = Collections.emptyList();
      MoveableController moveableController = mock(MoveableController.class);
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPositionHistory()).thenReturn(expectedPositionList);
      when(moveableController.getMoveable()).thenReturn(moveable);

      Position startPos = Positions.of(75, 75);
      EndPosition endPosition = EndPositions.of(500, 450);
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withEvasionStateMachineConfig(60, 45)
            .withTrippleDetectorCluster(DetectorConfigBuilder.builder()
                  .withDetectorReach(60)
                  .withEvasionDistance(45)
                  .withDetectorAngle(60)
                  .withEvasionAngle(45)
                  .withEvasionAngleInc(2)
                  .build(),
                  DetectorConfigBuilder.builder()
                        .withDetectorReach(25)
                        .withEvasionDistance(5)
                        .withDetectorAngle(60)
                        .withEvasionAngle(45)
                        .withEvasionAngleInc(2)
                        .build())
            .withStartPos(startPos)
            .withMovingIncrement(2)
            .withMoveableController(moveableController)
            .withMaze(MazeBuilder.builder()
                  .withGrid(grid)
                  .withStartPos(Positions.of(0, 0))
                  .withEndPosition(endPosition)
                  .withEndPositionPrecision(1)
                  .withCorridor(2)
                  .withCorridorWidth(2)
                  .withSegmentLenth(2)
                  .build()
                  .build())
            .build();

      // When
      List<Position> actualPositionList = mazeRunner.run();

      // Then
      verify(moveableController).leadMoveable();
      assertThat(actualPositionList, is(expectedPositionList));
   }

}
