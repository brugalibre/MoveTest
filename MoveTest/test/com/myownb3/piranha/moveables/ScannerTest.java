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
	Detector detector = new DetectorImpl(5, 45);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 1), detector);
	boolean isAvoiding = true;

	// When
	moveable.moveForward(5);
	boolean isEffectivelyAvoiding = detector.isAvoiding(obstacle);

	// Then
	Assert.assertThat(isAvoiding, is(isEffectivelyAvoiding));
    }

    @Test
    void testAvoiding_DistanceNotCloseEnough() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7.1));
	Detector detector = new DetectorImpl(5, 45);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 1), detector);
	boolean isAvoiding = true;

	// When
	moveable.moveForward(3);// Only moving forward 3 - so the obstacle stays out of 'avoiding' range
	boolean isEffectivelyAvoiding = detector.isAvoiding(obstacle);

	// Then
	Assert.assertThat(isAvoiding, is(not(isEffectivelyAvoiding)));
	// The distance is effectively about 3.1 - just about 0.1 to far away
    }

    @Test
    void testAvoiding_OpositDirectionBackward() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -1));
	Detector detector = new DetectorImpl();
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 2), detector);
	boolean isAvoiding = true;

	// When
	moveable.moveBackward(3);
	boolean isEffectivelyAvoiding = detector.isAvoiding(obstacle);

	// Then
	Assert.assertThat(isAvoiding, is(not(isEffectivelyAvoiding)));
    }

    @Test
    void testAvoiding_CorrectDirectionBackward() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -7));
	Detector detector = new DetectorImpl();
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(1, 2), detector);
	boolean isAvoidingAfterTurn = true;
	boolean isAvoidingBeforeTurn = false;

	// When
	moveable.moveBackward(6);
	boolean isEffectivelyAvoidingBeforeTurn = detector.isAvoiding(obstacle);
	moveable.makeTurn(180);
	boolean isEffectivelyAvoiding = detector.isAvoiding(obstacle);

	// Then
	Assert.assertThat(isAvoidingBeforeTurn, is(isEffectivelyAvoidingBeforeTurn));
	Assert.assertThat(isAvoidingAfterTurn, is(isEffectivelyAvoiding));
    }

    @Test
    void testAvoiding_UnknownGridElement() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, -7));
	Detector detector = new DetectorImpl();
	boolean isAvoiding = true;
	boolean hasDetected = true;

	// When
	boolean isEffectivAvoiding = detector.isAvoiding(obstacle);
	boolean hasEffectivDetected = detector.hasObjectDetected(obstacle);

	// Then
	Assert.assertThat(isAvoiding, is(not(isEffectivAvoiding)));
	Assert.assertThat(hasDetected, is(not(hasEffectivDetected)));
    }

    @Test
    void testAvoiding_AvoidingAngleNorthDirection() {

	// Given
	Grid grid = new DefaultGrid();
	new ObstacleImpl(grid, Positions.of(0, 7.1));
	Detector detector = new DetectorImpl(5, 45);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(0, 1), detector);
	// boolean isAvoiding = true;
	double expectedEndAngle = 22.5;

	// When
	moveable.moveForward(5);
	// boolean isEffectivelyAvoiding = detector.isAvoiding(obstacle);
	double effectEndAngle = detector.getAvoidAngleRelative2(moveable.getPosition());

	// Then
	// Assert.assertThat(isAvoiding, is(isEffectivelyAvoiding));
	Assert.assertThat(expectedEndAngle, is(effectEndAngle));
    }

    @Test
    void testAvoiding_AvoidingAngle80DegreeDirection() {

	// Given
	Grid grid = new DefaultGrid();
	new ObstacleImpl(grid, Positions.of(0, 7.1));
	Detector detector = new DetectorImpl(5, 45);
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(0, 1), detector);
	// boolean isAvoiding = true;
	double expectedEndAngle = 22.5;

	// When
	moveable.moveForward(5);
	// boolean isEffectivelyAvoiding = detector.isAvoiding(obstacle);
	double effectEndAngle = detector.getAvoidAngleRelative2(moveable.getPosition());

	// Then
	// Assert.assertThat(isAvoiding, is(isEffectivelyAvoiding));
	Assert.assertThat(expectedEndAngle, is(effectEndAngle));
    }
}
