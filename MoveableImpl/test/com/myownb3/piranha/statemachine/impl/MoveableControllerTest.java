/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import static com.myownb3.piranha.util.MathUtil.round;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
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

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.detector.collision.CollisionDetectedException;
import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.EndPointMoveable;
import com.myownb3.piranha.moveables.EndPointMoveableImpl;
import com.myownb3.piranha.moveables.MoveResult;
import com.myownb3.piranha.moveables.MoveResultImpl;
import com.myownb3.piranha.moveables.MoveableController;
import com.myownb3.piranha.moveables.MoveableController.MoveableControllerBuilder;
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
    void testBuilder() {
	
	// Given
	Position startPos = Positions.of(0, 12);
	Position endPos = Positions.of(0, 25);
	
	// When
	MoveableController moveableController = MoveableControllerBuilder.builder()//
		.withStrategie(MovingStrategie.FORWARD)//
		.withEndPositions(Collections.emptyList())//
		.withPostMoveForwardHandler(res -> {})//
        		.withEndPointMoveable()//
        		.withGrid(mock(DefaultGrid.class))//
        		.withStartPosition(startPos)//
        		.withEndPosition(endPos)//
        		.withHandler((a,b) -> {})//
        		.buildAndReturnParentBuilder()
        	.build();//
	
	// Then
	assertThat(moveableController.getMoveable().getPosition(), is (startPos));
	assertThat(moveableController.getMoveable().getPosition(), is (startPos));
    }
    
    @Test
    void test_leadForwardWith2Points() {
	
	// given
	Position endPos1 = Positions.of(0, 12);
	Position endPos2 = Positions.of(12, 24);
	EvasionStateMachine handler = spy(new EvasionStateMachine(mock(Detector.class), mock(EvasionStateMachineConfig.class)));
	EndPointMoveable moveable = spy(new EndPointMoveableImpl(mock(DefaultGrid.class), endPos1, handler, endPos1, 10));
	MoveResult result = new MoveResultImpl(0, 0, true);
	when(moveable.moveForward2EndPos()).thenReturn(result);

	MoveableController controller = MoveableControllerBuilder.builder()//
		.withStrategie(MovingStrategie.FORWARD)//
		.withEndPositions(Arrays.asList(endPos1, endPos2))//
		.withMoveable(moveable)//
		.withPostMoveForwardHandler(res -> {
		})
        	.build();//

	// When
	controller.leadMoveable();

	// Then
	verify(moveable, times(2)).prepare();
	verify(moveable).setEndPosition(eq(endPos1));
	verify(moveable).setEndPosition(eq(endPos2));
	verify(handler).setEndPosition(eq(endPos1));
	verify(handler).setEndPosition(eq(endPos2));
	verify(moveable, times(3)).moveForward2EndPos(); // + 1 because of the 'when()-Statement'
    }
    
    @Test
    void test_leadForwardWith2PointsAbortAfterTheFirstOne() {

	// given
	Position endPos1 = Positions.of(0, 12);
	Position endPos2 = Positions.of(12, 24);
	EndPointMoveable moveable = Mockito.mock(EndPointMoveable.class);
	MoveResult result = new MoveResultImpl(0, 0, true);
	when(moveable.moveForward2EndPos()).thenReturn(result);

	List<MoveableController> moveContrList = new ArrayList<>();
	MoveableController  	controller = MoveableControllerBuilder.builder()//
		.withStrategie(MovingStrategie.FORWARD)//
		.withEndPositions(Arrays.asList(endPos1, endPos2))//
		.withMoveable(moveable)//
		.withPostMoveForwardHandler(res -> {
		    moveContrList.get(0).stop();
		}).build();//
	moveContrList.add(controller);

	// When
	controller.leadMoveable();

	// Then
	verify(moveable, times(2)).prepare();
	verify(moveable).setEndPosition(eq(endPos1));
	verify(moveable).setEndPosition(eq(endPos2));
	verify(moveable).moveForward2EndPos();
    }
    
    @Test
    void test_MoveForward_NorthUnknownStrategie() {

	// Given
	Grid grid = GridBuilder.builder()//
		.build();
	Position expectedEndPos = Positions.of(0, 12);
	EndPointMoveable moveable = EndPointMoveableBuilder.builder()//
		.withGrid(grid)//
		.withStartPosition(Positions.of(0, 0))//
		.withEndPosition(expectedEndPos)//
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
		.withEndPosition(expectedEndPos).withHandler((g, m) -> {
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
		.withEndPosition(expectedEndPos)//
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
		}).withEndPosition(expectedEndPos)//
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
		.addObstacle(Positions.of(20, 17))//
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
		    .withEndPosition(endPos)//
		    .withHandler(stateMachine)//
		    .withMovingIncrement(movingIncrement)//
		    .build();
	    return this;
	}
    }
}
