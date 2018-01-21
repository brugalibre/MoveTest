/**
 * 
 */
package com.myownb3.piranha;

import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author Dominic
 *
 */
class TestMove {

    @Test
    void testMoveBackwardNTimes() {

	// Given
	Moveable moveable = new SimpleMoveable();

	int maxMovements = 5;
	Position effectStartPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(0, -maxMovements);

	// When
	for (int i = 0; i < maxMovements; i++) {
	    moveable.moveBackward();
	}

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, is(new Position(0, 0)));
	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    @Test
    void testMoveForwardNTimes() {

	// Given
	Moveable moveable = new SimpleMoveable();

	int maxMovements = 5;
	Position effectStartPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(0, maxMovements);

	// When
	for (int i = 0; i < maxMovements; i++) {
	    moveable.moveForward();
	}

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, is(new Position(0, 0)));
	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    ///////////////////////////////////////////////
    // Turn Right //
    ///////////////////////////////////////////////

    @Test
    public void testTurnRight() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Direction[] resultList = new Direction[] { Direction.O, Direction.S, Direction.W, Direction.N };

	// When
	for (int i = 0; i < resultList.length; i++) {

	    moveable.turnRight();

	    // Then
	    Position endPosition = moveable.getPosition();
	    Position erwarteteEndPosition = new Position(Direction.N, 0, 0);
	    Direction expectedDirection = resultList[i];

	    Assert.assertThat(endPosition, is(erwarteteEndPosition));
	    Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
	}
    }

    @Test
    public void testTurnRightTwoTimesAndMoveForwardOnce() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Direction.O);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Direction.S);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnRight();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(Direction.S, 0, -1);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testTurnRightThreeTimesAndMoveForwardOnce() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Direction.O);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Direction.S);
	expectedPositionToTurnMap.put(Integer.valueOf(2), Direction.W);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnRight();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(Direction.W, -1, 0);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    ///////////////////////////////////////////////
    // Turn Left //
    ///////////////////////////////////////////////

    @Test
    public void testTurnLeft() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Direction[] resultList = new Direction[] { Direction.W, Direction.S, Direction.O, Direction.N };

	// When
	for (int i = 0; i < resultList.length; i++) {
	    moveable.turnLeft();

	    // Then
	    Position endPosition = moveable.getPosition();
	    Position erwarteteEndPosition = new Position(Direction.N, 0, 0);
	    Direction expectedDirection = resultList[i];

	    Assert.assertThat(endPosition, is(erwarteteEndPosition));
	    Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
	}
    }

    ///////////////////////////////////////////////
    // Move Backwards & Turn Left //
    ///////////////////////////////////////////////

    @Test
    public void testTurnLeftOnceTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	moveable.turnLeft();
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(Direction.W, 1, 0);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    @Test
    public void testTurnLeftTwoTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Direction.W);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Direction.S);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnLeft();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(Direction.S, 0, 1);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testTurnLeftThreeTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Direction.W);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Direction.S);
	expectedPositionToTurnMap.put(Integer.valueOf(2), Direction.O);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnLeft();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(Direction.O, -1, 0);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testMoveBackward() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Direction expectedDirection = Direction.N;

	// When
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(0, -1);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
	Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
    }

    @Test
    public void testTurn45DegreeAndMoveForward() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	moveable.turnDegree(45);
	moveable.moveForward(10);

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(null, -7.1, 7.1);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }
}
