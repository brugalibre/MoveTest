/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Obstacle;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Positions;

/**
 * @author Dominic
 *
 */
class ScannerTest {

    @Test
    void testAvoiding_DistanceCloseEnough() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 1), detector);
	boolean isEvasioning = true;

	// When
	moveable.moveForward(5);
	boolean isEffectivelyAvoiding = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(isEffectivelyAvoiding, is(isEvasioning));
    }

    @Test
    void testAvoiding_DistanceNotCloseEnough() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7.1));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 1), detector);
	boolean isEvasioning = true;

	// When
	moveable.moveForward(3);// Only moving forward 3 - so the obstacle stays out of 'avoiding' range
	boolean isEffectivelyAvoiding = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(isEffectivelyAvoiding, is(not(isEvasioning)));
	// The distance is effectively about 3.1 - just about 0.1 to far away
    }

    @Test
    void testAvoiding_OpositDirectionBackward() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -1));
	Detector detector = new DetectorImpl();
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 2), detector);
	boolean isEvasioning = true;

	// When
	moveable.moveBackward(3);
	boolean isEffectivelyAvoiding = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(isEffectivelyAvoiding, is(not(isEvasioning)));
    }

    @Test
    void testAvoiding_CorrectDirectionBackward() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -7));
	Detector detector = new DetectorImpl();
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 2), detector);
	boolean isEvasioningAfterTurn = true;
	boolean isEvasioningBeforeTurn = false;

	// When
	moveable.moveBackward(6);
	boolean isEffectivelyAvoidingBeforeTurn = detector.isEvasioning(obstacle);
	moveable.makeTurn(180);
	boolean isEffectivelyAvoiding = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(isEffectivelyAvoidingBeforeTurn, is(isEvasioningBeforeTurn));
	Assert.assertThat(isEffectivelyAvoiding, is(isEvasioningAfterTurn));
    }

    @Test
    void testAvoiding_UnknownGridElement() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -7));
	Detector detector = new DetectorImpl();
	boolean isEvasioning = true;
	boolean hasDetected = true;

	// When
	boolean isEffectivAvoiding = detector.isEvasioning(obstacle);
	boolean hasEffectivDetected = detector.hasObjectDetected(obstacle);

	// Then
	Assert.assertThat(isEvasioning, is(not(isEffectivAvoiding)));
	Assert.assertThat(hasDetected, is(not(hasEffectivDetected)));
    }

    @Test
    void testAvoiding_AvoidingAngleNorthDirection() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(0, 7.1));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(0, 1), detector, true);
	double expectedEndAngle = 67.5;
	boolean expectedIsEvasioning = false;

	// When
	moveable.moveForward(7);
	Direction direction = moveable.getPosition().getDirection();
	double effectEndAngle = direction.getAngle();
	boolean effectIsEvasioning = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(effectIsEvasioning, is(expectedIsEvasioning));
	Assert.assertThat(effectEndAngle, is(expectedEndAngle));
    }

    @Test
    void testAvoiding_AvoidingAngle90DegreeDirection_InLowerRange() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(-1.8195117, 5));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(0, 1), detector, true);
	double expectedEndAngle = 136.875;
	boolean expectedIsEvasioning = false;

	// When
	moveable.makeTurn(30);
	moveable.moveForward(5);
	Direction direction = moveable.getPosition().getDirection();
	double effectEndAngle = direction.getAngle();
	boolean effectIsEvasioning = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(effectEndAngle, is(expectedEndAngle));
	Assert.assertThat(effectIsEvasioning, is(expectedIsEvasioning));
    }

    @Test
    void testAvoiding_AvoidingAngle100DegreeDirection_InUpperRange() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(-2.8867, 7));
	Detector detector = new DetectorImpl(5, 45, 5.625);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(0, 0), detector, true);
	double expectedEndAngle = 88.75;
	boolean expectedIsEvasioning = false;

	// When
	moveable.makeTurn(10);
	moveable.moveForward(7);
	Direction direction = moveable.getPosition().getDirection();
	double effectEndAngle = direction.getAngle();
	boolean effectIsEvasioning = detector.isEvasioning(obstacle);

	// Then
	Assert.assertThat(effectEndAngle, is(expectedEndAngle));
	Assert.assertThat(effectIsEvasioning, is(expectedIsEvasioning));
    }
}
