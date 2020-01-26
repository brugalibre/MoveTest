/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Obstacle;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.exception.NotImplementedException;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MovingStrategie;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class MoveableControllerTest {

    @Test
    void test_MoveForward_NorthUnknownStrategie() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position expectedEndPos = Positions.of(0, 12);
	MoveableController controller = new MoveableController(moveable, expectedEndPos, MovingStrategie.BACKWARD);

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
	Grid grid = new DefaultGrid(20, 20);
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position expectedEndPos = Positions.of(0, 12);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_South() {

	// Given
	Grid grid = new DefaultGrid(20, 20);
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position expectedEndPos = Positions.of(0, -10);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveMultipleTargets() {

	// Given
	Grid grid = new DefaultGrid(300, 300, 0, 0);
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position endPos1 = Positions.of(0, 10);
	Position expectedEndPos = Positions.of(5, 15);
	MoveableController controller = new MoveableController(moveable, Arrays.asList(endPos1, expectedEndPos));

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(expectedEndPos), 0);
    }

    @Test
    void test_MoveMultipleTargets_ForwardCurved() {

	// Given
	Grid grid = new DefaultGrid(15, 15);
	Position endPos1 = Positions.of(0, 10);
	Position expectedEndPos = Positions.of(5, 15);

	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	MoveableController controller = new MoveableController(moveable, Arrays.asList(endPos1, expectedEndPos), 
		MovingStrategie.FORWARD_CURVED);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(expectedEndPos), 0);
    }

    @Test
    void test_MoveForward_North_WithObstacle() {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()
		.withDefaultGrid(200, 200)
		.withEndPos(Positions.of(0, 12))
		.withObstacle(Positions.of(0, 8))
		.withDetector(5, 70, 60, 10)
		.withStateMachine(2, 0.011)
		.withMoveable()
		.withMoveableController();

	// When
	tcb.controller.leadMoveable();

	// Then
	Position effectEndPos = tcb.moveable.getPosition();
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(tcb.endPos), 0);
	assertThat(tcb.moveable.getPositionHistory().contains(tcb.obstacle.getPosition()), is(not(true)));
	assertThat(tcb.moveable.getPositionHistory().isEmpty(), is(not(true)));
    }

    @Test
    void test_MoveForward_NorthEast_WithObstacle() throws InterruptedException {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()
		.withDefaultGrid(200, 200)
		.withEndPos(Positions.of(28, 28))
		.withObstacle(Positions.of(10, 10))
		.withDetector(5, 70, 60, 10)
		.withStateMachine(2, 0.011)
		.withMoveable()
		.withMoveableController();

	// When
	tcb.controller.leadMoveable();

	// Then
	Position effectEndPos = tcb.moveable.getPosition();
	List<Position> trackingList = tcb.moveable.getPositionHistory();

	assertThat(trackingList.isEmpty(), is(not(true)));
	assertThat(trackingList.contains(tcb.obstacle.getPosition()), is(not(true)));
	assertThat(tcb.stateMachine.evasionState, is (EvasionStates.DEFAULT));
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(tcb.endPos), 0);
//	com.myownb3.piranha.launch.MoveableLauncher.visualizePositionsWithJFreeChart(trackingList, tcb.obstacle);
    }

    @Test
    void test_MoveForward_NorthEast_WithMultipleObstacles() throws InterruptedException {

	// Given
	TestCaseBuilder tcb = new TestCaseBuilder()
		.withDefaultGrid(200, 200)
		.withEndPos(Positions.of(28, 28))
		.addObstacle(Positions.of(10, 10))
		.addObstacle(Positions.of(17, 17))
		.addObstacle(Positions.of(22, 22))
		.withDetector(5, 60, 50, 5)
		.withStateMachine(4, 0.05)
		.withMoveable()
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
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(tcb.endPos), 0);
	assertThat(tcb.stateMachine.evasionState, is (EvasionStates.DEFAULT));
//	com.myownb3.piranha.launch.MoveableLauncher.visualizePositionsWithJFreeChart(trackingList, tcb.obstacles);
    }

    private static final class TestCaseBuilder {
	private Grid grid;
	private Position endPos;
	private Obstacle obstacle;
	private List<Obstacle> obstacles;
	private Detector detector;
	private Moveable moveable;
	private EvasionStateMachine stateMachine;
	private MoveableController controller;
	
	private TestCaseBuilder() {
	    obstacles = new ArrayList<>();
	}

	public TestCaseBuilder withMoveableController() {
	    this.controller = new MoveableController(moveable, endPos);
	    return this;
	}

	public TestCaseBuilder withDefaultGrid(int maxY, int maxX) {
	    this.grid = new DefaultGrid(maxY, maxX);
	    return this;
	}

	public TestCaseBuilder withEndPos(Position expectedEndPos) {
	    this.endPos = expectedEndPos;
	    return this;
	}

	public TestCaseBuilder withObstacle(Position obstaclePos) {
	    Objects.requireNonNull(grid, "We need a Grid to add any GridElement!");
	    obstacle = new ObstacleImpl(grid, obstaclePos);
	    return this;
	}

	public TestCaseBuilder withDetector(int detectorReach, int detectorAngle, int evasionAngle, double angleInc) {
	    this.detector = new DetectorImpl(detectorReach, detectorAngle, evasionAngle, angleInc);
	    return this;
	}

	public TestCaseBuilder addObstacle(Position obstaclePos) {
	    Obstacle obstacle = new ObstacleImpl(grid, obstaclePos);
	    obstacles.add(obstacle);
	    return this;
	}
	
	public TestCaseBuilder withStateMachine(int angleIncMultiplier, double minDistance){
	    stateMachine = new EvasionStateMachine(detector, endPos, angleIncMultiplier, minDistance);
	    return this;
	}
	public TestCaseBuilder withMoveable() {
	    moveable = new MoveableBuilder(grid)//
		    .withHandler(stateMachine)//
		    .build();
	    return this;
	}
    }
}
