/**
 * 
 */
package com.myownb3.piranha.core.moveables.controller;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.collision.CollisionDetectedException;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachine;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.exception.NotImplementedException;

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
            .withEndPointMoveable()
            .withGrid(mock(DefaultGrid.class))
            .withShape(shape)
            .buildAndReturnParentBuilder()
            .build();//

      // Then
      assertThat(moveableController.getMoveable().getPosition(), is(startPos));
      assertThat(moveableController.getCurrentEndPos(), is(nullValue()));
   }

   @Test
   void testMoveForwardIncrementalEndPos() {

      // Given
      Position startPos = Positions.of(0, 0);
      EndPosition endPos1 = EndPositions.of(0, 0.1);
      EndPosition endPos2 = EndPositions.of(0, 0.2);
      List<EndPosition> endPosList = Arrays.asList(endPos1, endPos2);

      EndPointMoveable movable = EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(startPos)
                  .build())
            .build();

      MoveableController moveableController = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
            .withEndPositions(endPosList)
            .withMoveable(movable)
            .build();//

      // When
      moveableController.leadMoveable();
      Position endPosAfterFirstMove = moveableController.getCurrentEndPos();
      moveableController.leadMoveable();
      Position endPosAfterSecondMove = moveableController.getCurrentEndPos();

      // Then
      assertThat(endPosAfterFirstMove, is(endPos2));
      assertThat(endPosAfterSecondMove, is(endPos1));
   }

   @Test
   void testMoveForwardIncrementalEndPos_ButDoesNotReachEndPos() {

      // Given
      Position startPos = Positions.of(0, 0);
      EndPosition endPos1 = EndPositions.of(0, 10);
      List<EndPosition> endPosList = Arrays.asList(endPos1);

      EndPointMoveable movable = spy(EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(10)
                  .withMaxY(10)
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(startPos)
                  .build())
            .build());

      MoveableController moveableController = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
            .withEndPositions(endPosList)
            .withMoveable(movable)
            .build();//

      // When
      moveableController.leadMoveable();

      // Then
      assertThat(moveableController.getCurrentEndPos(), is(endPos1));
   }

   @Test
   void testMoveForwardWithoutEndPos() {

      // Given
      Position startPos = Positions.of(0, 12);
      Position expectedEndPos = Positions.of(0, 12.1);
      List<MoveableController> moveableControllers = new ArrayList<MoveableController>();

      MoveableController moveableController = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD_WITHOUT_END_POS)
            .withEndPointMoveable()
            .withGrid(GridBuilder.builder(30, 30)
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(4)
                  .withCenter(startPos)
                  .build())
            .addMoveablePostActionHandler(moveable -> {
               moveableControllers.get(0).stop();
               return false;
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
      DefaultGrid grid = GridBuilder.builder(30, 30)
            .build();
      EvasionStateMachine handler = spy(EvasionStateMachineBuilder.builder()
            .withGrid(grid)
            .withDetector(mock(Detector.class))
            .withEvasionStateMachineConfig(mock(EvasionStateMachineConfig.class))
            .build());
      MoveResult result = new MoveResultImpl(endPosDistance, 0, true);
      EndPointMoveable moveable = spy(EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(endPos1)
                  .build())
            .withEvasionStateMachine(handler)
            .withMovingIncrement(10)
            .build());
      doReturn(result).when(moveable).moveForward2EndPos();

      MoveableController controller = MoveableControllerBuilder.builder()
            .withStrategie(MovingStrategy.FORWARD)
            .withEndPositions(Arrays.asList(endPos1, endPos2))
            .withMoveable(moveable)
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
   void test_MoveForward_NorthUnknownStrategie() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      EndPosition expectedEndPos = EndPositions.of(0, 12);
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();


      // When
      Executable ex = () -> {
         new MoveableController(() -> moveable, MovingStrategy.BACKWARD, Collections.singletonList(expectedEndPos));
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

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
      ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 10))
                  .build())
            .build();
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .build();

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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(startPos)
                  .build())
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
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(obstaclePos)
                     .build())
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

      public TestCaseBuilder withMoveable(Position startPos) {
         moveable = EndPointMoveableBuilder.builder()
               .withGrid(grid)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(startPos)
                     .build())
               .withEvasionStateMachine(stateMachine)
               .withMovingIncrement(movingIncrement)
               .build();
         return this;
      }
   }
}
