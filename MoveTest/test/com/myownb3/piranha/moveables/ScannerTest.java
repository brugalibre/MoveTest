/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Obstacle;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.detector.Detector;
import com.myownb3.piranha.moveables.detector.DetectorImpl;
<<<<<<< Upstream, based on origin/master
import com.myownb3.piranha.moveables.statemachine.impl.DetectableMoveableHelper;
import com.myownb3.piranha.moveables.statemachine.impl.EvasionStateMachine;
=======
import com.myownb3.piranha.statemachine.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
>>>>>>> d27c863 Move EventStateMachine to new Package. Introduce new EventHandlers

/**
 * @author Dominic
 *
 */
class ScannerTest {

    @Test
    void testEvasion_DistanceCloseEnough() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new MoveableBuilder(grid, Positions.of(1, 1))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean isEvasion = true;

	// When
	moveable.moveForward(50);
	boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

	// Then
	assertThat(isEffectivelyEvasion, is(isEvasion));
    }

    @Test
    void testEvasion_DistanceCloseEnough_ButAlreadyOvertaken() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(7, 7));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	new MoveableBuilder(grid, Positions.of(8, 8))//
		.withHandler(new DetectableMoveableHelper(detector))//
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
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7));
	Detector detector = new DetectorImpl(5, 45, 15, 5.625);
	Moveable moveable = new MoveableBuilder(grid, Positions.of(3, 2))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	// GridElement angle is 81.87°

	// When
	moveable.moveForward(4);
	boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

	// Then
	assertThat(isEffectivelyEvasion, is(not(true)));
    }

    @Test
    void testEvasion_DistanceNotCloseEnough() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7.1));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new MoveableBuilder(grid, Positions.of(1, 1))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean isEvasion = true;

	// When
	moveable.moveForward(3);// Only moving forward 3 - so the obstacle stays out of 'avoiding' range
	boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

	// Then
	assertThat(isEffectivelyEvasion, is(not(isEvasion)));
	// The distance is effectively about 3.1 - just about 0.1 to far away
    }

    @Test
    void testEvasion_OpositDirectionBackward() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -1));
	Detector detector = new DetectorImpl();
	Moveable moveable = new MoveableBuilder(grid, Positions.of(1, 2))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean isEvasion = true;

	// When
	moveable.moveBackward(3);
	boolean isEffectivelyEvasion = detector.isEvasion(obstacle);

	// Then
	assertThat(isEffectivelyEvasion, is(not(isEvasion)));
    }

    @Test
    void testEvasion_CorrectDirectionBackward() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -7));
	Detector detector = new DetectorImpl();

	Moveable moveable = new MoveableBuilder(grid, Positions.of(1, 2))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean isEvasionAfterTurn = true;
	boolean isEvasionBeforeTurn = false;

	// When
	moveable.moveBackward(60);
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
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -7));
	Detector detector = new DetectorImpl();
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
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(0, 7.1));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new MoveableBuilder(grid, Positions.of(0, 1))//
		.withHandler(new EvasionStateMachine(detector))//
		.build();
	double expectedEndAngle = 90;
	boolean expectedIsEvasion = false;

	// When
	moveable.moveForward(30);
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
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(-1.8195117, 5));
	Detector detector = new DetectorImpl(2, 45, 5.625);
	Moveable moveable = new MoveableBuilder(grid, Positions.of(0, 1))//
		.withHandler(new EvasionStateMachine(detector))//
		.build();
	double expectedEndAngle = 120;
	boolean expectedIsEvasion = false;

	// When
	moveable.makeTurn(30);
	moveable.moveForward(15);
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
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(-2.8867, 7));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new MoveableBuilder(grid)//
		.withHandler(new EvasionStateMachine(detector))//
		.build();
	double expectedEndAngle = 100;
	boolean expectedIsEvasion = false;

	// When
	moveable.makeTurn(10);
	moveable.moveForward(7);
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
	Grid grid = new DefaultGrid(20, 20);
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(20, 20));
	Detector detector = new DetectorImpl();
	new MoveableBuilder(grid)//
		.withHandler(new EvasionStateMachine(detector))//
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
}
