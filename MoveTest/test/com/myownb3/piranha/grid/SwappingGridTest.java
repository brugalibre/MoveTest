/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.direction.Directions;

/**
 * @author Dominic
 *
 */
class SwappingGridTest {

    @Test
    public void testDefaultGridBoundsMoveForward() {

	// Given
	Grid grid = new SwappingGrid(10, 10);
	Position expectedPosition = Positions.of(Directions.N, 10, 1);
	Position expectedPosition2 = Positions.of(Directions.O, 1, 10);

	// When
	Position createdPosition = grid.moveForward(Positions.of(Directions.N, 10, 10));
	Position createdPosition2 = grid.moveForward(Positions.of(Directions.O, 10, 10));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
    }

    @Test
    public void testDefaultGridBoundsMoveBackward() {

	// Given
	Grid grid = new SwappingGrid(10, 10);
	Position expectedPosition = Positions.of(Directions.N, 4, 9);
	Position expectedPosition2 = Positions.of(Directions.O, 9, 4);

	// When
	Position createdPosition = grid.moveBackward(Positions.of(Directions.N, 4, 0));
	Position createdPosition2 = grid.moveBackward(Positions.of(Directions.O, 0, 4));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
    }

    @Test
    public void testOffsetGridBoundsMoveForward() {

	// Given
	Grid grid = new SwappingGrid(10, 10, 5, 5);
	Position expectedPosition = Positions.of(Directions.N, 10, 6);
	Position expectedPosition2 = Positions.of(Directions.O, 6, 10);
	Position expectedPosition3 = Positions.of(Directions.S, 10, 9);

	// When
	Position createdPosition = grid.moveForward(Positions.of(Directions.N, 10, 10));
	Position createdPosition2 = grid.moveForward(Positions.of(Directions.O, 10, 10));
	Position createdPosition3 = grid.moveForward(Positions.of(Directions.S, 10, 5));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
	assertThat(createdPosition3, is(expectedPosition3));
    }

    @Test
    public void testOffsetGridBoundsMoveBackward() {

	// Given
	Grid grid = new SwappingGrid(10, 10, 5, 5);
	Position expectedPosition = Positions.of(Directions.N, 9, 9);
	Position expectedPosition2 = Positions.of(Directions.O, 9, 9);
	Position expectedPosition3 = Positions.of(Directions.S, 5, 6);

	// When
	Position createdPosition = grid.moveBackward(Positions.of(Directions.N, 9, 5));
	Position createdPosition2 = grid.moveBackward(Positions.of(Directions.O, 5, 9));
	Position createdPosition3 = grid.moveBackward(Positions.of(Directions.S, 5, 10));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
	assertThat(createdPosition3, is(expectedPosition3));
    }

}
