/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.PositionImpl;

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
	Position erwarteteEndPosition = new PositionImpl(0, -maxMovements);

	// When
	for (int i = 0; i < maxMovements; i++) {
	    moveable.moveBackward();
	}

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, is(new PositionImpl(0, 0)));
	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    @Test
    void testMoveForwardNegativeValues() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	Executable ex = () -> moveable.moveBackward(-3);
	// Then
	Assertions.assertThrows(IllegalArgumentException.class, ex);
    }

    @Test
    void testMoveForwardNTimes() {

	// Given
	Moveable moveable = new SimpleMoveable();

	int maxMovements = 5;
	Position effectStartPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(0, maxMovements);

	// When
	for (int i = 0; i < maxMovements; i++) {
	    moveable.moveForward();
	}

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, is(new PositionImpl(0, 0)));
	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    ///////////////////////////////////////////////
    // Turn Right //
    ///////////////////////////////////////////////

    @Test
    public void testTurnRight() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Direction[] resultList = new DirectionImpl[] { DirectionDefs.O, DirectionDefs.S, DirectionDefs.W,
		DirectionDefs.N };

	// When
	for (int i = 0; i < resultList.length; i++) {

	    moveable.turnRight();

	    // Then
	    Position endPosition = moveable.getPosition();
	    Position erwarteteEndPosition = new PositionImpl(DirectionDefs.N, 0, 0);
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
	expectedPositionToTurnMap.put(Integer.valueOf(0), DirectionDefs.O);
	expectedPositionToTurnMap.put(Integer.valueOf(1), DirectionDefs.S);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnRight();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(DirectionDefs.S, 0, -1);

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
	expectedPositionToTurnMap.put(Integer.valueOf(0), DirectionDefs.O);
	expectedPositionToTurnMap.put(Integer.valueOf(1), DirectionDefs.S);
	expectedPositionToTurnMap.put(Integer.valueOf(2), DirectionDefs.W);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnRight();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(DirectionDefs.W, -1, 0);

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
	Direction[] resultList = new DirectionImpl[] { DirectionDefs.W, DirectionDefs.S, DirectionDefs.O,
		DirectionDefs.N };

	// When
	for (int i = 0; i < resultList.length; i++) {
	    moveable.turnLeft();

	    // Then
	    Position endPosition = moveable.getPosition();
	    Position erwarteteEndPosition = new PositionImpl(DirectionDefs.N, 0, 0);
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
	Position erwarteteEndPosition = new PositionImpl(DirectionDefs.W, 1, 0);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    @Test
    public void testTurnLeftTwoTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), DirectionDefs.W);
	expectedPositionToTurnMap.put(Integer.valueOf(1), DirectionDefs.S);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnLeft();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(DirectionDefs.S, 0, 1);

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
	expectedPositionToTurnMap.put(Integer.valueOf(0), DirectionDefs.W);
	expectedPositionToTurnMap.put(Integer.valueOf(1), DirectionDefs.S);
	expectedPositionToTurnMap.put(Integer.valueOf(2), DirectionDefs.O);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnLeft();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(DirectionDefs.O, -1, 0);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testMoveBackward() {

	// Given
	Moveable moveable = new SimpleMoveable();
	DirectionImpl expectedDirection = DirectionDefs.N;

	// When
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(0, -1);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
	Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
    }

    @Test
    public void testTurn45DegreeAndMoveForward() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	moveable.makeTurn(45);
	moveable.moveForward(10);

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(null, -7.07, 7.07);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }

    @Test
    public void testTurnXDegreeAndMoveForward() {

	// Given
	Moveable moveable = new SimpleMoveable();
	Position erwarteterZwischenStop1 = new PositionImpl(null, -3.535, 3.535);
	Position erwarteterZwischenStop2 = new PositionImpl(null, -6.433, 4.312);
	Position erwarteterZwischenStop3 = new PositionImpl(null, -5.397, 0.4483);

	// When
	moveable.makeTurn(45); // 135; x:-0.7071 ; y:+0.7071
	moveable.moveForward(5); // x:-3.54 ; y: +3.54

	Position effectZwischenStop1 = moveable.getPosition();

	moveable.makeTurn(30); // 165; x:-0.965925 ; y: 0.25882
	moveable.moveForward(3); // x: -2.8978; y: 0.7764
	Position effectZwischenStop2 = moveable.getPosition();

	moveable.makeTurn(-60); // 105; x: -0.258819; y:0.965925
	moveable.moveBackward(4); // x:1.035276 ; y=3.8637
	Position effectZwischenStop3 = moveable.getPosition();

	// Then
	Assert.assertThat(effectZwischenStop1, is(erwarteterZwischenStop1));
	Assert.assertThat(effectZwischenStop2, is(erwarteterZwischenStop2));
	Assert.assertThat(effectZwischenStop3, is(erwarteterZwischenStop3));
    }

    @Test
    public void testTurnMinus30DegreeAndMoveBackward() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	moveable.makeTurn(-30);
	moveable.moveBackward(10);

	// Then
	Position endPosition = moveable.getPosition();
	Position erwarteteEndPosition = new PositionImpl(null, -5, -8.66);

	Assert.assertThat(endPosition, is(erwarteteEndPosition));
    }
}
