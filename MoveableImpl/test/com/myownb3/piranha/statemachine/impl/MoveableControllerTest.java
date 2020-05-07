/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.detector.collision.CollisionDetectedException;
import com.myownb3.piranha.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.grid.gridelement.shape.Shape;
import com.myownb3.piranha.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.EndPointMoveableImpl;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.moveables.MoveResultImpl;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder.EndPointMoveableBuilder;
import com.myownb3.piranha.moveables.MovingStrategy;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class MoveableControllerTest {

   @Test
   void testBuilder() {

      // Given
      Position startPos = Positions.of(0, 12);
      EndPosition endPos = EndPositions.of(0, 25);
      Shape shape = CircleBuilder.builder()
            .withRadius(5)
            .withAmountOfPoints(4)
            .withCenter(startPos)
            .build();

      // When
      MoveableController moveableController = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD)
            .withEndPositions(Collections.singletonList(endPos))
            .withPostMoveForwardHandler(res -> {
            })
            .withEndPointMoveable()
            .withGrid(mock(DefaultGrid.class))
            .withStartPosition(startPos)
            .withShape(shape)
            .withHandler((a, b) -> {
            })
            .buildAndReturnParentBuilder()
            .build();//

      // Then
      assertThat(moveableController.getMoveable().getPosition(), is(startPos));
      assertThat(moveableController.getCurrentEndPos(), is(nullValue()));
   }

   @Test
   void testMoveForwardWithoutEndPos() {

      // Given
      Position startPos = Positions.of(0, 12);
      Position expectedEndPos = Positions.of(0, 12.1);
      List<MoveableController> moveableControllers = new ArrayList<MoveableController>();

      MoveableController moveableController = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD_WITHOUT_END_POS)
            .withPostMoveForwardHandler(res -> {
               moveableControllers.get(0).stop();
            })
            .withEndPointMoveable()
            .withGrid(GridBuilder.builder(30, 30)
                  .build())
            .withStartPosition(startPos)
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(4)
                  .withCenter(startPos)
                  .build())
            .withHandler((a, b) -> {
            })
            .buildAndReturnParentBuilder()
            .build();//

      moveableControllers.add(moveableController);

      // When
      moveableController.leadMoveable();

      // Then
      assertThat(moveableController.getMoveable().getPosition(), is(expectedEndPos));
      assertThat(moveableController.getCurrentEndPos(), is(nullValue()));
   }

   @Test
   void test_leadForwardWith2Points() {

      // given
      double endPosDistance = 10;
      EndPosition endPos1 = EndPositions.of(0, 12);
      EndPosition endPos2 = EndPositions.of(0, 13);
      EvasionStateMachine handler = spy(EvasionStateMachineBuilder.builder()
            .withDetector(mock(Detector.class))
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build());
      DefaultGrid grid = GridBuilder.builder(30, 30)
            .build();
      MoveResult result = new MoveResultImpl(endPosDistance, 0, true);
      EndPointMoveable moveable = spy(new EndPointMoveableImpl(grid, endPos1, handler, 10));
      doReturn(result).when(moveable).moveForward2EndPos();

      MoveableController controller = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD)
            .withEndPositions(Arrays.asList(endPos1, endPos2))
            .withMoveable(moveable)
            .withPostMoveForwardHandler(res -> {
            })
            .build();//

      // When
      controller.leadMoveable();

      // Then
      assertThat(result.getEndPosDistance(), is(endPosDistance));
      verify(moveable).setEndPosition(eq(endPos1));
      verify(moveable).setEndPosition(eq(endPos2));
      verify(handler).setEndPosition(eq(endPos1));
      verify(handler).setEndPosition(eq(endPos2));
      verify(moveable, times(2)).moveForward2EndPos();
   }

   @Test
   void test_leadForwardWith2PointsAbortAfterTheFirstOne() {

      // given
      EndPosition endPos1 = EndPositions.of(0, 12);
      EndPosition endPos2 = EndPositions.of(12, 24);
      EndPointMoveable moveable = Mockito.mock(EndPointMoveable.class);
      MoveResult result = new MoveResultImpl(0, 0, true);
      when(moveable.moveForward2EndPos()).thenReturn(result);

      List<MoveableController> moveContrList = new ArrayList<>();
      MoveableController controller = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD)
            .withEndPositions(Arrays.asList(endPos1, endPos2))
            .withMoveable(moveable)
            .withPostMoveForwardHandler(res -> {
               moveContrList.get(0).stop();
            }).build();//
      moveContrList.add(controller);

      // When
      controller.leadMoveable();

      // Then
      verify(moveable).setEndPosition(eq(endPos1));
      verify(moveable).setEndPosition(eq(endPos2));
      verify(moveable).moveForward2EndPos();
   }

   @Test
   void test_MoveForward_NorthUnknownStrategie() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      EndPosition expectedEndPos = EndPositions.of(0, 12);
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(Positions.of(0, 0))
            .withHandler((g, m) -> {
            })
            .build();

      MoveableController controller = new MoveableController(moveable, MovingStrategy.BACKWARD, Collections.singletonList(expectedEndPos));

      // When
      Executable ex = () -> {
         controller.leadMoveable();
      };
      // Then
      Assertions.assertThrows(NotImplementedException.class, ex);
   }

   @Test
   void test_MoveForward_North() {

      // Given
      Grid grid = GridBuilder.builder(20, 20)
            .build();
      EndPosition expectedEndPos = EndPositions.of(0, 12);
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(Positions.of(0, 0))
            .withHandler((g, m) -> {
            }).build();

      MoveableController controller = new MoveableController(moveable, Collections.singletonList(expectedEndPos));

      // When
      controller.leadMoveable();

      // Then
      Position effectEndPos = moveable.getPosition();
      assertThat(effectEndPos, is(Positions.of(expectedEndPos)));
   }

   @Test
   void test_MoveForward_WithObstacleAndCollision() throws InterruptedException {

      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(20)
            .withMaxY(20)
            .withDefaultCollisionDetectionHandler()
            .build();
      EndPosition expectedEndPos = EndPositions.of(0, 12);
      new ObstacleImpl(grid, Positions.of(0, 10));
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(Positions.of(0, 0))
            .withHandler((g, m) -> {
            }).build();

      MoveableController controller = new MoveableController(moveable, Collections.singletonList(expectedEndPos));

      // When
      Executable ex = () -> {
         controller.leadMoveable();
      };

      // Then
      Assertions.assertThrows(CollisionDetectedException.class, ex);
   }

   @Test
   void test_MoveForward_South() {

      // Given
      Grid grid = GridBuilder.builder(20, 20)
            .build();
      EndPosition expectedEndPos = EndPositions.of(0, -10);
      Position startPos = Positions.of(0, 0).rotate(180);
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(startPos)
            .withHandler((g, m) -> {
            })
            .build();

      MoveableController controller = new MoveableController(moveable, Collections.singletonList(expectedEndPos));

      // When
      controller.leadMoveable();

      // Then
      Position effectEndPos = Positions.of(moveable.getPosition());
      assertThat(effectEndPos, is(Positions.of(expectedEndPos)));
   }

   @Test
   void test_MoveForward_North_WithObstacle() throws InterruptedException {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDefaultGrid(200, 200)
            .withEndPos(EndPositions.of(0, 11))
            .withObstacle(Positions.of(0, 8))
            .withStateMachineConfig(1, 0.035, 0.7, 5, 70, 60, 10)
            .withDetector()
            .withStateMachine()
            .withMoveable(Positions.of(0, 0))
            .withMoveableController();

      // When
      tcb.controller.leadMoveable();

      // Then
      Position effectEndPos = tcb.moveable.getPosition();
      assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
      assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
      assertThat(tcb.moveable.getPositionHistory().contains(tcb.obstacle.getPosition()), is(not(true)));
      assertThat(tcb.moveable.getPositionHistory().isEmpty(), is(not(true)));
   }

   @Test
   void test_MoveForward_NorthEast_WithObstacle() throws InterruptedException {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDefaultGrid(200, 200)
            .withEndPos(EndPositions.of(28, 28, true))
            .withObstacle(Positions.of(10, 10))
            .withStateMachineConfig(1, 0.035, 0.7, 5, 70, 60, 10)
            .withDetector()
            .withStateMachine()
            .withMoveable(Positions.of(0, 0))
            .withMoveableController();

      // When
      tcb.controller.leadMoveable();

      // Then
      Position effectEndPos = tcb.moveable.getPosition();
      List<Position> trackingList = tcb.moveable.getPositionHistory();

      assertThat(trackingList.isEmpty(), is(not(true)));
      assertThat(trackingList.contains(tcb.obstacle.getPosition()), is(not(true)));
      assertThat(tcb.stateMachine.evasionState, is(EvasionStates.DEFAULT));
      assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
      assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
   }

   @Test
   void test_MoveForward_NorthEast_WithTwoObstacles() throws InterruptedException {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDefaultGrid(200, 200)
            .withEndPos(EndPositions.of(25, 25, true))
            .addObstacle(Positions.of(10, 10))
            .addObstacle(Positions.of(20, 19.5))
            .withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 2)
            .withDetector()
            .withStateMachine()
            .withMoveable(Positions.of(0, 0))
            .withMoveableController();

      // When
      tcb.controller.leadMoveable();

      // Then
      List<Position> trackingList = tcb.moveable.getPositionHistory();

      assertThat(trackingList.isEmpty(), is(not(true)));
      for (Obstacle obstacle : tcb.obstacles) {
         assertThat(trackingList.contains(obstacle.getPosition()), is(not(true)));
      }
      Position effectEndPos = tcb.moveable.getPosition();
      assertThat(tcb.stateMachine.evasionState, is(EvasionStates.DEFAULT));
      assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
      assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
   }

   @Test
   void test_MoveForward_NorthEast_WithMultipleObstacles() throws InterruptedException {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDefaultGrid(200, 200)
            .withEndPos(EndPositions.of(33, 33, true))
            .addObstacle(Positions.of(10, 10))
            .addObstacle(Positions.of(20, 19.5))
            .addObstacle(Positions.of(23, 23))
            .withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 3)
            .withDetector()
            .withStateMachine()
            .withMoveable(Positions.of(0, 0))
            .withMoveableController();

      // When
      tcb.controller.leadMoveable();

      // Then
      Position effectEndPos = tcb.moveable.getPosition();
      List<Position> trackingList = tcb.moveable.getPositionHistory();

      assertThat(trackingList.isEmpty(), is(not(true)));
      for (Obstacle obstacle : tcb.obstacles) {
         assertThat(trackingList.contains(obstacle.getPosition()), is(not(true)));
      }
      assertThat(tcb.stateMachine.evasionState, is(EvasionStates.DEFAULT));
      assertThat(round(effectEndPos.getY(), 0), is(tcb.endPos.getY()));
      assertThat(round(effectEndPos.getX(), 0), is(tcb.endPos.getX()));
   }

   @Test
   void test_MoveForward_NorthEast_WithMultipleObstaclesAndOffsetStartPos() throws InterruptedException {

      // Given
      Position startPos = Positions.of(Directions.S, 4, 2);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDefaultGrid(200, 200)
            .withEndPos(EndPositions.of(30, 30, true))
            .addObstacle(Positions.of(10, 10))
            .addObstacle(Positions.of(20, 19.5))
            .addObstacle(Positions.of(23, 23))
            .withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 2)
            .withDetector()
            .withStateMachine()
            .withMoveable(startPos)
            .withMoveableController();

      // When
      tcb.controller.leadMoveable();

      // Then
      Position effectEndPos = tcb.moveable.getPosition();
      List<Position> trackingList = tcb.moveable.getPositionHistory();

      assertThat(trackingList.isEmpty(), is(not(true)));
      for (Obstacle obstacle : tcb.obstacles) {
         assertThat(trackingList.contains(obstacle.getPosition()), is(not(true)));
      }
      assertThat(tcb.stateMachine.evasionState, is(EvasionStates.DEFAULT));
      assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
      assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));

   }

   @Test
   void test_MoveForward_NorthEast_WithMultipleObstacles_DoNotAvoid2One() throws InterruptedException {

      Position endPosAsNormalPosition = Positions.of(34, 34);
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withDefaultGrid(200, 200)
            .withEndPos(EndPositions.of(endPosAsNormalPosition))
            .addObstacle(Positions.of(10, 10))
            .addObstacle(Positions.of(20, 17))
            .addObstacle(Positions.of(25, 25))
            .withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 2)
            .withDetector()
            .withStateMachine()
            .withMovingIncrement(1)
            .withMoveable(Positions.of(0, 0))
            .withMoveableController();

      // When
      tcb.controller.leadMoveable();

      // Then
      Position effectEndPos = tcb.moveable.getPosition();
      List<Position> trackingList = tcb.moveable.getPositionHistory();

      assertThat(trackingList.isEmpty(), is(not(true)));
      for (Obstacle obstacle : tcb.obstacles) {
         assertThat(trackingList.contains(obstacle.getPosition()), is(not(true)));
      }
      assertThat(tcb.stateMachine.evasionState, is(EvasionStates.DEFAULT));
      assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
      assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
   }

   private static final class TestCaseBuilder {
      private Grid grid;
      private EndPosition endPos;
      private Obstacle obstacle;
      private List<Obstacle> obstacles;
      private Detector detector;
      private EndPointMoveable moveable;
      private EvasionStateMachine stateMachine;
      private MoveableController controller;
      private EvasionStateMachineConfig config;
      private int movingIncrement;

      private TestCaseBuilder() {
         obstacles = new ArrayList<>();
         movingIncrement = 2;
      }

      public TestCaseBuilder withMoveableController() {
         this.controller = new MoveableController(moveable, Collections.singletonList(endPos));
         return this;
      }

      public TestCaseBuilder withDefaultGrid(int maxY, int maxX) {
         this.grid = GridBuilder.builder(maxY, maxX)
               .build();
         return this;
      }

      public TestCaseBuilder withEndPos(EndPosition expectedEndPos) {
         this.endPos = expectedEndPos;
         return this;
      }

      public TestCaseBuilder withObstacle(Position obstaclePos) {
         Objects.requireNonNull(grid, "We need a Grid to add any GridElement!");
         obstacle = new ObstacleImpl(grid, obstaclePos);
         obstacles.add(obstacle);
         return this;
      }

      public TestCaseBuilder withDetector() {
         Objects.requireNonNull(config, "We need first a Config befor we can create a Detector!");
         this.detector = DetectorBuilder.builder()
               .withDetectorReach(config.getDetectorReach())
               .withEvasionDistance(config.getEvasionDistance())
               .withDetectorAngle(config.getDetectorAngle())
               .withEvasionAngle(config.getEvasionAngle())
               .withAngleInc(config.getEvasionAngleInc())
               .build();
         return this;
      }

      public TestCaseBuilder addObstacle(Position obstaclePos) {
         Obstacle obstacle = new ObstacleImpl(grid, obstaclePos);
         obstacles.add(obstacle);
         return this;
      }

      public TestCaseBuilder withStateMachineConfig(int angleIncMultiplier, double minDistance, double angleMargin,
            int detectorReach, int detectorAngle, int evasionAngle, double evasionAngleInc) {
         config = EvasionStateMachineConfigBuilder.builder()
               .withReturningAngleIncMultiplier(angleIncMultiplier)
               .withReturningMinDistance(minDistance)
               .withReturningAngleMargin(angleMargin)
               .withOrientationAngle(10)
               .withPassingDistance(2 * detectorReach / 3)
               .withPostEvasionReturnAngle(4)
               .withDetectorConfig(DetectorConfigBuilder.builder()
                     .withDetectorReach(detectorReach)
                     .withEvasionDistance(2 * detectorReach / 3)
                     .withDetectorAngle(detectorAngle)
                     .withEvasionAngle(evasionAngle)
                     .withEvasionAngleInc(evasionAngleInc)
                     .build())
               .build();
         return this;
      }

      public TestCaseBuilder withStateMachine() {
         stateMachine = EvasionStateMachineBuilder.builder()
               .withDetector(detector)
               .withEndPosition(endPos)
               .withEvasionStateMachineConfig(config)
               .build();
         return this;
      }

      public TestCaseBuilder withMovingIncrement(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public TestCaseBuilder withMoveable(Position startPos) {
         moveable = EndPointMoveableBuilder.builder()
               .withGrid(grid)
               .withStartPosition(startPos)
               .withHandler(stateMachine)
               .withMovingIncrement(movingIncrement)
               .build();
         return this;
      }
   }
}
