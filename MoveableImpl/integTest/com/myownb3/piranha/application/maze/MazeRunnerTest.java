package com.myownb3.piranha.application.maze;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.detector.collision.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.grid.maze.Maze;
import com.myownb3.piranha.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.moveables.MoveableController;


class MazeRunnerTest {

   @Test
   void testBuildMazeRunWithCustomMaze() {

      // Given
      Position startPos = Positions.of(130, 330);
      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withEvasionStateMachineConfig(70, 45)
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
                        .withEvasionAngle(25)
                        .withEvasionAngleInc(2)
                        .build())
            .withStartPos(startPos)
            .withMaze(MazeBuilder.builder()
                  .withGrid(MirrorGridBuilder.builder()
                        .withMaxX(1000)
                        .withMaxY(1000)
                        .withMinX(30)
                        .withMinY(30)
                        .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
                        .build())
                  .withStartPos(startPos)
                  .withCorridor(wallThickness)
                  .withCorridorWidth(coridorWidth)
                  .withSegmentLenth(segmentLength)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withObstacle(buildDefaultCircle(), 0, 0)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withEndPosition(buildDefaultCircle())
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withEndPosition(buildDefaultCircle())
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .withEndPosition(buildDefaultCircle())
                  .build()
                  .build())
            .withMovingIncrement(4)
            .withMoveableController(res -> {
            })
            .build();
      // When
      mazeRunner.run();
      Position actualEndPos = mazeRunner.getMoveableController().getCurrentEndPos();

      Maze maze = mazeRunner.getMaze();
      EndPosition endPosition = maze.getEndPositions().get(maze.getEndPositions().size() - 1);

      // Then
      assertThat(round(actualEndPos.getY(), 0), is(round(endPosition.getY(), 0)));
      assertThat(round(actualEndPos.getX(), 0), is(round(endPosition.getX(), 0)));
   }

   @Test
   void testBuildMazeRunWithCustomMaze_WithoutEndPos() {

      // Given
      Position startPos = Positions.of(130, 330);
      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;
      Position expectedMoveablePos = Positions.of(130, 330.2);
      List<Position> actualMoveablePos = new ArrayList<>();

      List<MoveableController> moveableControllers = new ArrayList<>();
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withEvasionStateMachineConfig(70, 45)
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
                        .withEvasionAngle(25)
                        .withEvasionAngleInc(2)
                        .build())
            .withStartPos(startPos)
            .withMaze(MazeBuilder.builder()
                  .withGrid(MirrorGridBuilder.builder()
                        .withMaxX(1000)
                        .withMaxY(1000)
                        .withMinX(30)
                        .withMinY(30)
                        .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
                        .build())
                  .withStartPos(startPos)
                  .withCorridor(wallThickness)
                  .withCorridorWidth(coridorWidth)
                  .withSegmentLenth(segmentLength)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .build()
                  .build())
            .withMoveableController(res -> {
               moveableControllers.get(0).stop();
               actualMoveablePos.add(res.getMoveablePosition());
            })
            .build();
      moveableControllers.add(mazeRunner.getMoveableController());

      // When
      mazeRunner.run();
      Position actualEndPos = mazeRunner.getMoveableController().getCurrentEndPos();

      // Then
      assertThat(actualEndPos, is(nullValue()));
      assertThat(actualMoveablePos.size(), is(1));
      assertThat(actualMoveablePos.get(0), is(expectedMoveablePos));
   }

   private static CircleImpl buildDefaultCircle() {
      return CircleBuilder.builder()
            .withRadius(4)
            .withAmountOfPoints(4)
            .withCenter(Positions.of(0, 0))
            .build();
   }
}
