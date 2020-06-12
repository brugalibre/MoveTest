package com.myownb3.piranha.application.random;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.random.RandomMoveableWithEndPositionRunner.RandomRunnerWithEndPositionsBuilder;
import com.myownb3.piranha.core.collision.detection.handler.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;

class RandomMoveableWithEndPositionRunnerTest {

   @Test
   void testBuildRandomMoveableWithEndPositionRunner_AndRun() {

      // Given
      int width = 4;
      int mainWindowWidth = 700;
      int padding = 30;
      Dimension dimension = new DimensionImpl(padding, padding, mainWindowWidth - padding, mainWindowWidth - padding);

      Position startPos = Positions.getRandomPosition(dimension, width, width);
      MoveableController moveableController = mockMoveableController();

      RandomMoveableWithEndPositionRunner endPositionRunner = RandomRunnerWithEndPositionsBuilder.builder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(dimension.getHeight())
                  .withMaxY(dimension.getWidth())
                  .withMinX(dimension.getX())
                  .withMinY(dimension.getY())
                  .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
                  .build())
            .withStartPos(startPos)
            .withRandomEndPositions(0)
            .withCircleRadius(width)
            .withRandomMoveableObstacles(20)
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .withReturningAngleIncMultiplier(1)
                  .withOrientationAngle(1)
                  .withReturningMinDistance(0.06)
                  .withReturningAngleMargin(0.7d)
                  .withPassingDistance(25)
                  .withPostEvasionReturnAngle(4)
                  .withDetectorConfig(DetectorConfigBuilder.builder()
                        .withDetectorReach(70)
                        .withEvasionDistance(50)
                        .withDetectorAngle(60)
                        .withEvasionAngle(45)
                        .withEvasionAngleInc(1)
                        .build())
                  .build())
            .withSideDetectorConfig(DetectorConfigBuilder.builder()
                  .withDetectorReach(45)
                  .withEvasionDistance(25)
                  .withDetectorAngle(60)
                  .withEvasionAngle(45)
                  .withEvasionAngleInc(1)
                  .build())
            .withDefaultDetectorCluster()
            .withMovingIncrement(2)
            .withMoveableController(moveableController)
            .build();

      // When
      endPositionRunner.run();

      // Then
      verify(moveableController).leadMoveable();
   }

   private MoveableController mockMoveableController() {
      MoveableController moveableController = mock(MoveableController.class);
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      when(moveable.getPositionHistory()).thenReturn(Collections.emptyList());
      when(moveableController.getMoveable()).thenReturn(moveable);
      return moveableController;
   }

   @Test
   void testBuildRandomMoveableWithEndPositionRunner() {

      // Given
      int expectedAmountOfGridElements = 2;// One for the End-Position and one for the single randome-obstacle
      int width = 4;
      int mainWindowWidth = 700;
      int padding = 30;
      Dimension dimension = new DimensionImpl(padding, padding, mainWindowWidth, mainWindowWidth);

      int detectorReach = 70;
      int evasionDistance = 50;

      // When
      RandomMoveableWithEndPositionRunner endPositionRunner = RandomRunnerWithEndPositionsBuilder.builder()
            .withGrid(MirrorGridBuilder.builder()
                  .withMaxX(dimension.getHeight())
                  .withMaxY(dimension.getWidth())
                  .withMinX(dimension.getX())
                  .withMinY(dimension.getY())
                  .withCollisionDetectionHandler(new DefaultCollisionDetectionHandlerImpl())
                  .build())
            .withStartPos(Positions.of(padding, padding))
            .withRandomEndPositions(1)
            .withCircleRadius(width)
            .withRandomMoveableObstacles(1)
            .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                  .withReturningAngleIncMultiplier(1)
                  .withOrientationAngle(1)
                  .withReturningMinDistance(0.06)
                  .withReturningAngleMargin(0.7d)
                  .withDetectorConfig(DetectorConfigBuilder.builder()
                        .withDetectorReach(detectorReach)
                        .withEvasionDistance(evasionDistance)
                        .withDetectorAngle(60)
                        .withEvasionAngle(45)
                        .withEvasionAngleInc(1)
                        .build())
                  .withPassingDistance(25)
                  .withPostEvasionReturnAngle(4)
                  .build())
            .withSideDetectorConfig(DetectorConfigBuilder.builder()
                  .withDetectorReach(45)
                  .withEvasionDistance(25)
                  .withDetectorAngle(60)
                  .withEvasionAngle(45)
                  .withEvasionAngleInc(1)
                  .build())
            .withDefaultDetectorCluster()
            .withMoveableController(res -> {
            })
            .build();

      // Then
      assertThat(endPositionRunner.getGrid(), is(notNullValue()));
      Grid grid = endPositionRunner.getGrid();
      assertThat(grid.getDimension().getHeight(), is(dimension.getHeight() - padding));
      assertThat(grid.getDimension().getWidth(), is(dimension.getWidth() - padding));

      assertThat(endPositionRunner.getDetectorCluster(), is(notNullValue()));
      assertThat(endPositionRunner.getDetectorCluster(), is(notNullValue()));
      assertThat(endPositionRunner.getMoveableController(), is(notNullValue()));

      assertThat(endPositionRunner.getAllGridElements().size(), is(expectedAmountOfGridElements));

      assertThat(endPositionRunner.getConfig(), is(notNullValue()));
      EvasionStateMachineConfig evasionStateMachineConfig = endPositionRunner.getConfig();
      assertThat(evasionStateMachineConfig.getDetectorReach(), is(detectorReach));
      assertThat(evasionStateMachineConfig.getEvasionDistance(), is(evasionDistance));
   }
}
