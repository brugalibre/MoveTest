/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;

/**
 * @author Dominic
 *
 */
class ScannerTest {

   @Test
   void testEvasion_DistanceCloseEnough() {

      // Given
      // We do not care about the detection handler, since this is not part of this
      // test
      Grid grid = GridBuilder.builder()
            .withCollisionDetectionHandler((a, b, c) -> new CollisionDetectionResultImpl(c))
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 7))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(5.625)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 1))
                  .build())
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .withVelocity(50)
            .build();
      boolean isEvasion = true;

      // When
      moveable.moveForward();
      boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(isEffectivelyEvasion, is(isEvasion));
   }

   @Test
   void testEvasion_DistanceCloseEnough_ButAlreadyOvertaken() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(7, 7))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(5.625)
            .build();
      MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(8, 8))
                  .build())
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .build();
      // Since this moveable is placed 'in front' of the obstacle, it must not be
      // detected
      boolean evasion = true;

      // When
      boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(isEffectivelyEvasion, is(not(evasion)));
   }

   @Test
   void testEvasion_DistanceCloseEnoughButButNotEvasion() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 7))
                  .build())
            .build();
      int detectorReach = 45;
      int evasionDistance = 2 * detectorReach / 3;
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withEvasionDistance(evasionDistance)
            .withDetectorAngle(45)
            .withEvasionAngle(15)
            .withAngleInc(5.625)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(3, 2))
                  .build())
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .withVelocity(4)
            .build();
      // GridElement angle is 81.87°

      // When
      moveable.moveForward();
      boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(isEffectivelyEvasion, is(not(true)));
   }

   @Test
   void testEvasion_DistanceNotCloseEnough() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 7.1))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(5.625)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 1))
                  .build())
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .withVelocity(3)
            .build();
      boolean isEvasion = true;

      // When
      moveable.moveForward();// Only moving forward 3 - so the obstacle stays out of 'avoiding' range
      boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(isEffectivelyEvasion, is(not(isEvasion)));
      // The distance is effectively about 3.1 - just about 0.1 to far away
   }

   @Test
   void testEvasion_OpositDirectionBackward() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, -1))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 2))
                  .build())
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .withVelocity(3)
            .build();
      boolean isEvasion = true;

      // When
      moveable.moveBackward();
      boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(isEffectivelyEvasion, is(not(isEvasion)));
   }

   @Test
   void testEvasion_CorrectDirectionBackward() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, -7))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();

      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 2))
                  .build())
            .withHandler(new DetectableMoveableHelper(grid, detector))
            .withVelocity(60)
            .build();
      boolean isEvasionAfterTurn = true;
      boolean isEvasionBeforeTurn = false;

      // When
      moveable.moveBackward();
      boolean isEffectivelyEvasionBeforeTurn = detector.isEvasion(obstacle);
      moveable.makeTurn(180);
      boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(isEffectivelyEvasionBeforeTurn, is(isEvasionBeforeTurn));
      assertThat(isEffectivelyEvasion, is(isEvasionAfterTurn));
   }

   @Test
   void testEvasion_UnknownGridElement() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, -7))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      boolean isEvasion = true;
      boolean hasDetected = true;

      // When
      boolean isEffectivEvasion = detector.isEvasion(obstacle);
      boolean hasEffectivDetected = detector.hasObjectDetected(obstacle);

      // Then
      assertThat(isEvasion, is(not(isEffectivEvasion)));
      assertThat(hasDetected, is(not(hasEffectivDetected)));
   }

   @Test
   void testEvasion_EvasionAngleNorthDirection() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 5, 75, 45, 5.625);
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(1, 7.1))
                  .build())
            .build();
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(5.625)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 1))
                  .build())
            .withHandler(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(detector)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .withVelocity(30)
            .build();
      double expectedEndAngle = 90;
      boolean expectedIsEvasion = false;

      // When
      moveable.moveForward();
      Direction direction = moveable.getPosition().getDirection();
      double effectEndAngle = direction.getAngle();
      boolean effectIsEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(effectIsEvasion, is(expectedIsEvasion));
      assertThat(effectEndAngle, is(expectedEndAngle));
   }

   @Test
   void testEvasion_EvasionAngle90DegreeDirection_InLowerRange() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(-1.8195117, 5))
                  .build())
            .build();
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 2, 45, 45, 5.625);
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(2)
            .withDetectorAngle(45)
            .withAngleInc(5.625)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 1))
                  .build())
            .withHandler(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(detector)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .withVelocity(15)
            .build();
      double expectedEndAngle = 120;
      boolean expectedIsEvasion = false;

      // When
      moveable.makeTurn(30);
      moveable.moveForward();
      Direction direction = moveable.getPosition().getDirection();
      double effectEndAngle = direction.getAngle();
      boolean effectIsEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(effectEndAngle, is(expectedEndAngle));
      assertThat(effectIsEvasion, is(expectedIsEvasion));
   }

   @Test
   void testEvasion_EvasionAngle100DegreeDirection_InUpperRange() {

      // Given
      Grid grid = GridBuilder.builder()
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(-2.8867, 7))
                  .build())
            .build();
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 5, 45, 45, 5.625);
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(5)
            .withDetectorAngle(45)
            .withAngleInc(5.625)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withHandler(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(detector)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .withVelocity(7)
            .build();
      double expectedEndAngle = 100;
      boolean expectedIsEvasion = false;

      // When
      moveable.makeTurn(10);
      moveable.moveForward();
      Direction direction = moveable.getPosition().getDirection();
      double effectEndAngle = direction.getAngle();
      boolean effectIsEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(effectEndAngle, is(expectedEndAngle));
      assertThat(effectIsEvasion, is(expectedIsEvasion));
   }

   @Test
   public void testEvasionDegreeZero() {
      // Given
      Grid grid = GridBuilder.builder(20, 20)
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(20, 20))
                  .build())
            .build();
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 8, 45, 45, 11.25);
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0))
                  .build())
            .withGrid(grid)
            .withHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(detector)
                  .withGrid(grid)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .build();
      double expectedEndAngle = 0;
      boolean expectedEvasion = false;

      // When
      double effectEndAngle = detector.getEvasionAngleRelative2(obstacle.getPosition());
      boolean effectIsEvasion = detector.isEvasion(obstacle);

      // Then
      assertThat(effectEndAngle, is(expectedEndAngle));
      assertThat(effectIsEvasion, is(expectedEvasion));
   }

   @Test
   public void testNotEvasionObstactleAlreadyPassed() {

      // Given
      Grid grid = GridBuilder.builder(10, 10)
            .build();
      Obstacle obstacle = ObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(5, 5))
                  .build())
            .build();
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 8, 45, 45, 11.25);
      Detector detector = DetectorBuilder.builder()
            .withDetectorReach(8)
            .withDetectorAngle(45)
            .withAngleInc(11.25)
            .build();
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(6, 6))
                  .build())
            .withHandler(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(detector)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .build();
      // Must not be an evasion since we are placed 'in front' of the obstacle
      boolean expectedEvasion = false;

      // When
      moveable.makeTurn(-45);

      // Then
      boolean effectIsEvasion = detector.isEvasion(obstacle);
      assertThat(effectIsEvasion, is(expectedEvasion));
   }
}
