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
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.grid.direction.DirectionImpl;

/**
 * @author Dominic
 *
 */
class TestMove {

    @Test
    void testMoveBackwardNTimes() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

	int maxMovements = 5;
	Position effectStartPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(0, -maxMovements);

	// When
	for (int i = 0; i < maxMovements; i++) {
	    moveable.moveBackward();
	}

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, is(Positions.of(0, 0)));
	Assert.assertThat(endPosition, is(expectedEndPosition));
    }

    @Test
    void testMoveForwardNegativeValues() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

	// When
	Executable ex = () -> moveable.moveBackward(-3);
	// Then
	Assertions.assertThrows(IllegalArgumentException.class, ex);
    }

    @Test
    void testMoveForwardNTimes() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

	int maxMovements = 5;
	Position effectStartPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(0, maxMovements);

	// When
	for (int i = 0; i < maxMovements; i++) {
	    moveable.moveForward();
	}

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, is(Positions.of(0, 0)));
	Assert.assertThat(endPosition, is(expectedEndPosition));
    }

    ///////////////////////////////////////////////
    // Turn Right //
    ///////////////////////////////////////////////

    @Test
    public void testTurnRight() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Direction[] resultList = new DirectionImpl[] { Directions.O, Directions.S, Directions.W, Directions.N };

	// When
	for (int i = 0; i < resultList.length; i++) {

	    moveable.turnRight();

	    // Then
	    Position endPosition = moveable.getPosition();
	    Position expectedEndPosition = Positions.of(Directions.N, 0, 0);
	    Direction expectedDirection = resultList[i];

	    Assert.assertThat(endPosition, is(expectedEndPosition));
	    Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
	}
    }

    @Test
    public void testTurnRightTwoTimesAndMoveForwardOnce() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.O);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnRight();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(Directions.S, 0, -1);

	Assert.assertThat(endPosition, is(expectedEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testTurnRightThreeTimesAndMoveForwardOnce() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.O);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);
	expectedPositionToTurnMap.put(Integer.valueOf(2), Directions.W);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnRight();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(Directions.W, -1, 0);

	Assert.assertThat(endPosition, is(expectedEndPosition));

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
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Direction[] resultList = new DirectionImpl[] { Directions.W, Directions.S, Directions.O, Directions.N };

	// When
	for (int i = 0; i < resultList.length; i++) {
	    moveable.turnLeft();

	    // Then
	    Position endPosition = moveable.getPosition();
	    Position expectedEndPosition = Positions.of(Directions.N, 0, 0);
	    Direction expectedDirection = resultList[i];

	    Assert.assertThat(endPosition, is(expectedEndPosition));
	    Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
	}
    }

    ///////////////////////////////////////////////
    // Move Backwards & Turn Left //
    ///////////////////////////////////////////////

    @Test
    public void testTurnLeftOnceTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

	// When
	moveable.turnLeft();
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(Directions.W, 1, 0);

	Assert.assertThat(endPosition, is(expectedEndPosition));
    }

    @Test
    public void testTurnLeftTwoTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.W);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnLeft();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(Directions.S, 0, 1);

	Assert.assertThat(endPosition, is(expectedEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testTurnLeftThreeTimesAndMoveBackwardOnce() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Map<Integer, Direction> effectPositionToTurnMap = new HashMap<>();
	Map<Integer, Direction> expectedPositionToTurnMap = new HashMap<>();
	expectedPositionToTurnMap.put(Integer.valueOf(0), Directions.W);
	expectedPositionToTurnMap.put(Integer.valueOf(1), Directions.S);
	expectedPositionToTurnMap.put(Integer.valueOf(2), Directions.O);

	// When
	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    moveable.turnLeft();
	    effectPositionToTurnMap.put(Integer.valueOf(i), moveable.getPosition().getDirection());
	}
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(Directions.O, -1, 0);

	Assert.assertThat(endPosition, is(expectedEndPosition));

	for (int i = 0; i < expectedPositionToTurnMap.size(); i++) {
	    Assert.assertThat(effectPositionToTurnMap.get(i), is(expectedPositionToTurnMap.get(i)));
	}
    }

    @Test
    public void testMoveBackward() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	DirectionImpl expectedDirection = Directions.N;

	// When
	moveable.moveBackward();

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(0, -1);

	Assert.assertThat(endPosition, is(expectedEndPosition));
	Assert.assertThat(endPosition.getDirection(), is(expectedDirection));
    }

    @Test
    public void testTurn45DegreeAndMoveForward() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

	// When
	moveable.makeTurn(45);
	moveable.moveForward(10);

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(null, -7.07, 7.07);

	Assert.assertThat(endPosition, is(expectedEndPosition));
    }

    @Test
    public void testTurnXDegreeAndMoveForward() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
	Position expectedStopover1 = Positions.of(null, -3.535, 3.535);
	Position expectedStopover2 = Positions.of(null, -6.433, 4.312);
	Position expectedStopover3 = Positions.of(null, -5.397, 0.4483);

	// When
	moveable.makeTurn(45); // 135; x:-0.7071 ; y:+0.7071
	moveable.moveForward(5); // x:-3.54 ; y: +3.54

	Position effectStopover1 = moveable.getPosition();

	moveable.makeTurn(30); // 165; x:-0.965925 ; y: 0.25882
	moveable.moveForward(3); // x: -2.8978; y: 0.7764
	Position effectStopover2 = moveable.getPosition();

	moveable.makeTurn(-60); // 105; x: -0.258819; y:0.965925
	moveable.moveBackward(4); // x:1.035276 ; y=3.8637
	Position effectStopover3 = moveable.getPosition();

	// Then
	Assert.assertThat(effectStopover1, is(expectedStopover1));
	Assert.assertThat(effectStopover2, is(expectedStopover2));
	Assert.assertThat(effectStopover3, is(expectedStopover3));
    }

    @Test
    public void testTurnMinus30DegreeAndMoveBackward() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

	// When
	moveable.makeTurn(-30);
	moveable.moveBackward(10);

	// Then
	Position endPosition = moveable.getPosition();
	Position expectedEndPosition = Positions.of(null, -5, -8.66);

	Assert.assertThat(endPosition, is(expectedEndPosition));
    }
}
