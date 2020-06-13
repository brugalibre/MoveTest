package com.myownb3.piranha.core.statemachine.impl;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

public class EvasionStateMachineIntegTest {
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
         obstacle = ObstacleBuilder.builder()
               .withGrid(grid)
               .withPosition(obstaclePos)
               .build();
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
         Obstacle obstacle = ObstacleBuilder.builder()
               .withGrid(grid)
               .withPosition(obstaclePos)
               .build();
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
               .withGrid(grid)
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
               .withMoveablePostActionHandler(stateMachine)
               .withMovingIncrement(movingIncrement)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(startPos)
                     .build())
               .build();
         return this;
      }
   }
}
