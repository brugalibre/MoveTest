package com.myownb3.piranha.application.maze;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.maze.MazeRunner.MazeRunnerBuilder;
import com.myownb3.piranha.core.collision.detection.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrier;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierImpl.LightBarrierBuilder;
import com.myownb3.piranha.core.detector.lightbarrier.LightBarrierPassedCallbackHandler;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.maze.Maze;
import com.myownb3.piranha.core.grid.maze.MazeImpl.MazeBuilder;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSegment;
import com.myownb3.piranha.core.grid.maze.corridor.CorridorSide;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.PostMoveForwardHandler;
import com.myownb3.piranha.core.moveables.controller.MoveableController;


class MazeRunnerTest {

   @Test
   void testBuildMazeRunWithCustomMaze() {

      // Given
      Position startPos = Positions.of(130, 330);
      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withEvasionStateMachineConfig(90, 50)
            .withTrippleDetectorCluster(DetectorConfigBuilder.builder()
                  .withDetectorReach(90)
                  .withEvasionDistance(55)
                  .withDetectorAngle(60)
                  .withEvasionAngle(60)
                  .withEvasionAngleInc(1)
                  .build(),
                  DetectorConfigBuilder.builder()
                        .withDetectorReach(60)
                        .withEvasionDistance(25)
                        .withDetectorAngle(60)
                        .withEvasionAngle(60)
                        .withEvasionAngleInc(1)
                        .build())
            .withStartPos(startPos)
            .withMaze(MazeBuilder.builder()
                  .withEndPositionPrecision(1)
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
                  .withObstacle(CircleBuilder.builder()
                        .withRadius(4)
                        .withAmountOfPoints(4)
                        .withCenter(Positions.of(0, 0))
                        .build(), 0, 0)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .withEndPosition(CircleBuilder.builder()
                        .withRadius(4)
                        .withAmountOfPoints(4)
                        .withCenter(Positions.of(0, 0))
                        .build())
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
      double expectedXAxisValue = 463;
      double expectedYAxisValue = 485.4113448726;
      Position startPos = Positions.of(130, 330);
      int wallThickness = 10;
      int coridorWidth = 80;
      int segmentLength = 80;
      MazeRunnerTestPostMoveActionHandler postMoveFowardHandler = new MazeRunnerTestPostMoveActionHandler();
      MazeRunner mazeRunner = MazeRunnerBuilder.builder()
            .withMovingIncrement(4)
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
                  .withObstacle(CircleBuilder.builder()
                        .withRadius(4)
                        .withAmountOfPoints(4)
                        .withCenter(Positions.of(0, 0))
                        .build(), 0, 0)
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .appendCorridorSegment()
                  .appendCorridorLeftAngleBend()
                  .appendCorridorSegment()
                  .withDetector(DetectorConfigBuilder.builder()
                        .withDetectorAngle(1)
                        .withDetectorReach(coridorWidth)
                        .build(), CorridorSide.LEFT)
                  .build()
                  .build())

            // With this detector settings, everything runs without End-Positions
            .withEvasionStateMachineConfig(90, 50)
            .withTrippleDetectorCluster(DetectorConfigBuilder.builder()
                  .withDetectorReach(90)
                  .withEvasionDistance(55)
                  .withDetectorAngle(60)
                  .withEvasionAngle(60)
                  .withEvasionAngleInc(1)
                  .build(),
                  DetectorConfigBuilder.builder()
                        .withDetectorReach(60)
                        .withEvasionDistance(25)
                        .withDetectorAngle(60)
                        .withEvasionAngle(60)
                        .withEvasionAngleInc(1)
                        .build())
            .withStartPos(startPos)
            .withMovingIncrement(4)
            .withMoveableController(postMoveFowardHandler)
            .build();

      postMoveFowardHandler.moveableController = mazeRunner.getMoveableController();
      postMoveFowardHandler.lightBarrier = LightBarrierBuilder.builder()
            .withLightBarrierCallbackHandler(postMoveFowardHandler)
            .withPlacedDetector(mazeRunner.getMaze()
                  .getMazeCorridorSegments().stream()
                  .map(CorridorSegment::getDetector)
                  .filter(Optional::isPresent)
                  .map(Optional::get)
                  .findFirst()
                  .get())
            .build();

      // When
      mazeRunner.run();
      Position actualEndPos = mazeRunner.getMoveableController().getMoveable().getPosition();

      // Then
      assertThat(round(actualEndPos.getX(), 0), is(round(expectedXAxisValue, 0)));
      assertThat(round(actualEndPos.getY(), 0), is(round(expectedYAxisValue, 0)));
   }

   @Test
   void testBuildMazeRunWithCustomMaze_WithoutEndPos_MoveOneIncrement() {

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

   private static class MazeRunnerTestPostMoveActionHandler implements PostMoveForwardHandler, LightBarrierPassedCallbackHandler {

      private MoveableController moveableController;
      private LightBarrier lightBarrier;

      @Override
      public void handlePostMoveForward(MoveResult moveResult) {
         Moveable moveable = moveableController.getMoveable();
         lightBarrier.checkGridElement(moveable);
      }

      @Override
      public void handleLightBarrierTriggered(GridElement gridElement) {
         moveableController.stop();
      }
   }
}
