/**
 * 
 */
package com.myownb3.piranha;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author Dominic
 *
 */
class GridTest {

    @Test
    public void testDefaultGridBoundsMoveForward() {

	// Given
	Grid grid = new Grid(10, 10);
	Position expectedPosition = new Position(Direction.N, 10, 1);
	Position expectedPosition2 = new Position(Direction.O, 1, 10);

	// When
	Position createdPosition = grid.moveForward(new Position(Direction.N, 10, 10));
	Position createdPosition2 = grid.moveForward(new Position(Direction.O, 10, 10));

	// Then
	Assert.assertThat(createdPosition, is(expectedPosition));
	Assert.assertThat(createdPosition2, is(expectedPosition2));
    }

    @Test
    public void testDefaultGridBoundsMoveBackward() {

	// Given
	Grid grid = new Grid(10, 10);
	Position expectedPosition = new Position(Direction.N, 4, 9);
	Position expectedPosition2 = new Position(Direction.O, 9, 4);

	// When
	Position createdPosition = grid.moveBackward(new Position(Direction.N, 4, 0));
	Position createdPosition2 = grid.moveBackward(new Position(Direction.O, 0, 4));

	// Then
	Assert.assertThat(createdPosition, is(expectedPosition));
	Assert.assertThat(createdPosition2, is(expectedPosition2));
    }

    @Test
    public void testOffsetGridBoundsMoveForward() {

	// Given
	Grid grid = new Grid(10, 10, 5, 5);
	Position expectedPosition = new Position(Direction.N, 10, 6);
	Position expectedPosition2 = new Position(Direction.O, 6, 10);
	Position expectedPosition3 = new Position(Direction.S, 10, 9);

	// When
	Position createdPosition = grid.moveForward(new Position(Direction.N, 10, 10));
	Position createdPosition2 = grid.moveForward(new Position(Direction.O, 10, 10));
	Position createdPosition3 = grid.moveForward(new Position(Direction.S, 10, 5));

	// Then
	Assert.assertThat(createdPosition, is(expectedPosition));
	Assert.assertThat(createdPosition2, is(expectedPosition2));
	Assert.assertThat(createdPosition3, is(expectedPosition3));
    }

    @Test
    public void testOffsetGridBoundsMoveBackward() {

	// Given
	Grid grid = new Grid(10, 10, 5, 5);
	Position expectedPosition = new Position(Direction.N, 9, 9);
	Position expectedPosition2 = new Position(Direction.O, 9, 9);
	Position expectedPosition3 = new Position(Direction.S, 5, 6);

	// When
	Position createdPosition = grid.moveBackward(new Position(Direction.N, 9, 5));
	Position createdPosition2 = grid.moveBackward(new Position(Direction.O, 5, 9));
	Position createdPosition3 = grid.moveBackward(new Position(Direction.S, 5, 10));

	// Then
	Assert.assertThat(createdPosition, is(expectedPosition));
	Assert.assertThat(createdPosition2, is(expectedPosition2));
	Assert.assertThat(createdPosition3, is(expectedPosition3));
    }
}
