/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectedException;
import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder.EndPointMoveableBuilder;
import com.myownb3.piranha.moveables.MovingStrategie;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class MoveableControllerTest {

    @Test
    void test_MoveForward_NorthUnknownStrategie() {

	// Given
	Grid grid = GridBuilder.builder()//
		.build();
	Position expectedEndPos = Positions.of(0, 12);
	EndPointMoveable moveable = EndPointMoveableBuilder.builder()//
		.withGrid(grid)//
		.withStartPosition(Positions.of(0, 0))//
		.widthEndPosition(expectedEndPos)//
		.withHandler((g, m) -> {
		})//
		.build();

	MoveableController controller = new MoveableController(moveable, MovingStrategie.BACKWARD);

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
	Grid grid = GridBuilder.builder(20, 20)//
		.build();
	Position expectedEndPos = Positions.of(0, 12);
	EndPointMoveable moveable = EndPointMoveableBuilder.builder()//
		.withGrid(grid)
		.withStartPosition(Positions.of(0, 0))
		.widthEndPosition(expectedEndPos).withHandler((g, m) -> {
		}).build();

	MoveableController controller = new MoveableController(moveable);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_WithObstacleAndCollision() {

	// Given
	Grid grid = GridBuilder.builder()//
		.withMaxX(20)//
		.withMaxY(20)//
		.withDefaultCollisionDetectionHandler()//
		.build();
	Position expectedEndPos = Positions.of(0, 12);
	new ObstacleImpl(grid, Positions.of(0, 10));
	EndPointMoveable moveable =  EndPointMoveableBuilder.builder()//
		.withGrid(grid)//
		.withStartPosition(Positions.of(0, 0))//
		.widthEndPosition(expectedEndPos)//
		.withHandler((g, m) -> {
		}).build();

	MoveableController controller = new MoveableController(moveable);

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
	Grid grid = GridBuilder.builder(20, 20)//
		.build();
	Position expectedEndPos = Positions.of(0, -10);
	EndPointMoveable moveable =  EndPointMoveableBuilder.builder()//
		.withGrid(grid)//
		.withStartPosition(Positions.of(0, 0))//
		.withHandler((g, m) -> {
		}).widthEndPosition(expectedEndPos)//
		.build();

	MoveableController controller = new MoveableController(moveable);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_North_WithObstacle() throws InterruptedException {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withDefaultGrid(200, 200)//
		.withEndPos(Positions.of(0, 11))//
		.withObstacle(Positions.of(0, 8))//
		.withStateMachineConfig(1, 0.035, 0.7, 5, 70, 60, 10)//
		.withDetector()
		.withStateMachine()//
		.withMoveable()//
		.withMoveableController();

	// When
	tcb.controller.leadMoveable();

	// Then
	Position effectEndPos = tcb.moveable.getPosition();
	org.junit.Assert.assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
	org.junit.Assert.assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
	assertThat(tcb.moveable.getPositionHistory().contains(tcb.obstacle.getPosition()), is(not(true)));
	assertThat(tcb.moveable.getPositionHistory().isEmpty(), is(not(true)));
    }

    @Test
    void test_MoveForward_NorthEast_WithObstacle() throws InterruptedException {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withDefaultGrid(200, 200)//
		.withEndPos(Positions.of(28, 28))//
		.withObstacle(Positions.of(10, 10))//
		.withStateMachineConfig(1, 0.035, 0.7, 5, 70, 60, 10)//
		.withDetector()//
		.withStateMachine()//
		.withMoveable()//
		.withMoveableController();

	// When
	tcb.controller.leadMoveable();

	// Then
	Position effectEndPos = tcb.moveable.getPosition();
	List<Position> trackingList = tcb.moveable.getPositionHistory();

	assertThat(trackingList.isEmpty(), is(not(true)));
	assertThat(trackingList.contains(tcb.obstacle.getPosition()), is(not(true)));
	assertThat(tcb.stateMachine.evasionState, is(EvasionStates.DEFAULT));
	org.junit.Assert.assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
	org.junit.Assert.assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
    }

    @Test
    void test_MoveForward_NorthEast_WithTwoObstacles() throws InterruptedException {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withDefaultGrid(200, 200)//
		.withEndPos(Positions.of(25, 25))//
		.addObstacle(Positions.of(10, 10))//
		.addObstacle(Positions.of(20, 19.5))//
		.withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 10)//
		.withDetector()//
		.withStateMachine()//
		.withMoveable()//
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
	org.junit.Assert.assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
	org.junit.Assert.assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
    }

    @Test
    void test_MoveForward_NorthEast_WithMultipleObstacles() throws InterruptedException {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withDefaultGrid(200, 200)//
		.withEndPos(Positions.of(30, 30))//
		.addObstacle(Positions.of(10, 10))//
		.addObstacle(Positions.of(20, 19.5))//
		.addObstacle(Positions.of(23, 23))//
		.withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 10)//
		.withDetector()//
		.withStateMachine()//
		.withMoveable()//
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

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()//
		.withDefaultGrid(200, 200)//
		.withEndPos(Positions.of(34, 34))//
		.addObstacle(Positions.of(10, 10))//
		.addObstacle(Positions.of(20, 18.5))//
		.addObstacle(Positions.of(25, 25))
		.withStateMachineConfig(1, 0.14, 0.7, 5, 70, 60, 10)//
		.withDetector()//
		.withStateMachine()
		.withMovingIncrement(1)//
		.withMoveable()//
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
	org.junit.Assert.assertThat(round(effectEndPos.getY(), 0), is(round(tcb.endPos.getY(), 0)));
	org.junit.Assert.assertThat(round(effectEndPos.getX(), 0), is(round(tcb.endPos.getX(), 0)));
    }

    private static final class TestCaseBuilder {
	private Grid grid;
	private Position endPos;
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
	    this.controller = new MoveableController(moveable);
	    return this;
	}

	public TestCaseBuilder withDefaultGrid(int maxY, int maxX) {
	    this.grid = GridBuilder.builder(maxY, maxX)//
		    .build();
	    return this;
	}

	public TestCaseBuilder withEndPos(Position expectedEndPos) {
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
	    this.detector = new DetectorImpl(config.getDetectorReach(), config.getDetectorAngle(),
		    config.getEvasionAngle(), config.getEvasionAngleInc());
	    return this;
	}

	public TestCaseBuilder addObstacle(Position obstaclePos) {
	    Obstacle obstacle = new ObstacleImpl(grid, obstaclePos);
	    obstacles.add(obstacle);
	    return this;
	}

	public TestCaseBuilder withStateMachineConfig(int angleIncMultiplier, double minDistance, double angleMargin,
		int detectorReach, int detectorAngle, int evasionAngle, double evasionAngleInc) {
	    config = new EvasionStateMachineConfigImpl(angleIncMultiplier, minDistance, angleMargin, detectorReach,
		    detectorAngle, evasionAngle, evasionAngleInc);
	    return this;
	}

	public TestCaseBuilder withStateMachine() {
	    stateMachine = new EvasionStateMachine(detector, endPos, config);
	    return this;
	}

	public TestCaseBuilder withMovingIncrement(int movingIncrement) {
	    this.movingIncrement = movingIncrement;
	    return this;
	}

	public TestCaseBuilder withMoveable() {
	    moveable = EndPointMoveableBuilder.builder()//
		    .withGrid(grid)//
		    .withStartPosition(Positions.of(0, 0))
		    .widthEndPosition(endPos)//
		    .withHandler(stateMachine)//
		    .withMovingIncrement(movingIncrement)//
		    .build();
	    return this;
	}
    }
}
