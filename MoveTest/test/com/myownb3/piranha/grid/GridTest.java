/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.exception.GridElementOutOfBoundsException;
import com.myownb3.piranha.moveables.DirectionDefs;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.SimpleMoveable;

/**
 * @author Dominic
 *
 */
class GridTest {

    @Test
    public void testDefaultGridBoundsMoveForward() {

	// Given
	Grid grid = new SwappingGrid(10, 10);
	Position expectedPosition = Positions.of(DirectionDefs.N, 10, 1);
	Position expectedPosition2 = Positions.of(DirectionDefs.O, 1, 10);

	// When
	Position createdPosition = grid.moveForward(Positions.of(DirectionDefs.N, 10, 10));
	Position createdPosition2 = grid.moveForward(Positions.of(DirectionDefs.O, 10, 10));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
    }

    @Test
    public void testDefaultGridBoundsMoveBackward() {

	// Given
	Grid grid = new SwappingGrid(10, 10);
	Position expectedPosition = Positions.of(DirectionDefs.N, 4, 9);
	Position expectedPosition2 = Positions.of(DirectionDefs.O, 9, 4);

	// When
	Position createdPosition = grid.moveBackward(Positions.of(DirectionDefs.N, 4, 0));
	Position createdPosition2 = grid.moveBackward(Positions.of(DirectionDefs.O, 0, 4));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
    }

    @Test
    public void testOffsetGridBoundsMoveForward() {

	// Given
	Grid grid = new SwappingGrid(10, 10, 5, 5);
	Position expectedPosition = Positions.of(DirectionDefs.N, 10, 6);
	Position expectedPosition2 = Positions.of(DirectionDefs.O, 6, 10);
	Position expectedPosition3 = Positions.of(DirectionDefs.S, 10, 9);

	// When
	Position createdPosition = grid.moveForward(Positions.of(DirectionDefs.N, 10, 10));
	Position createdPosition2 = grid.moveForward(Positions.of(DirectionDefs.O, 10, 10));
	Position createdPosition3 = grid.moveForward(Positions.of(DirectionDefs.S, 10, 5));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
	assertThat(createdPosition3, is(expectedPosition3));
    }

    @Test
    public void testOffsetGridBoundsMoveBackward() {

	// Given
	Grid grid = new SwappingGrid(10, 10, 5, 5);
	Position expectedPosition = Positions.of(DirectionDefs.N, 9, 9);
	Position expectedPosition2 = Positions.of(DirectionDefs.O, 9, 9);
	Position expectedPosition3 = Positions.of(DirectionDefs.S, 5, 6);

	// When
	Position createdPosition = grid.moveBackward(Positions.of(DirectionDefs.N, 9, 5));
	Position createdPosition2 = grid.moveBackward(Positions.of(DirectionDefs.O, 5, 9));
	Position createdPosition3 = grid.moveBackward(Positions.of(DirectionDefs.S, 5, 10));

	// Then
	assertThat(createdPosition, is(expectedPosition));
	assertThat(createdPosition2, is(expectedPosition2));
	assertThat(createdPosition3, is(expectedPosition3));
    }

    @Test
    public void testAddElementOnGrid() {

	// Given
	Grid grid = new DefaultGrid();
	boolean isElementOnGrid = true;

	// When
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(1, 7.1));

	boolean isElementEffectivelyOnGrid = grid.containsElement(obstacle);

	// Then
	assertThat(isElementEffectivelyOnGrid, is(isElementOnGrid));
    }

    @Test
    public void testAddElementOnGridAndMove() {

	// Given
	Grid grid = new DefaultGrid(20, 20);
	boolean isElementOnGridBeforeMove = true;
	boolean isElementOnGridAfterMove = true;

	// When
	SimpleMoveable moveable = new SimpleMoveable(grid, Positions.of(1, 7.1));
	boolean isElementEffectivelyOnGridAfterMove = grid.containsElement(moveable);

	moveable.moveForward(5);
	boolean isElementEffectivelyOnGridBeforeMove = grid.containsElement(moveable);

	// Then
	assertThat(isElementEffectivelyOnGridBeforeMove, is(isElementOnGridBeforeMove));
	assertThat(isElementEffectivelyOnGridAfterMove, is(isElementOnGridAfterMove));
    }

    @Test
    public void testOutOfUpperBoundsXDefaultGrid() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	moveable.turnRight();
	Executable ex = () -> {
	    moveable.moveForward(11);
	};
	// Then
	assertThrows(GridElementOutOfBoundsException.class, ex);
    }

    @Test
    public void testOutOfUpperBoundsYDefaultGrid() {

	// Given
	Moveable moveable = new SimpleMoveable();

	// When
	Executable ex = () -> {
	    moveable.moveForward(11);
	};
	// Then
	assertThrows(GridElementOutOfBoundsException.class, ex);
    }

    @Test
    public void testOutOfLowerBoundsDefaultGrid() {

	// Given
	Moveable moveable = new SimpleMoveable(new DefaultGrid(10, 10, 0, 0), Positions.of(0, 0));

	// When

	Executable ex = () -> {
	    moveable.moveBackward(1);
	};
	// Then
	assertThrows(GridElementOutOfBoundsException.class, ex);
    }

    @Test
    public void testOutOfLowerBoundsXDefaultGrid() {

	// Given
	Moveable moveable = new SimpleMoveable(new DefaultGrid(10, 10, 0, 0), Positions.of(0, 0));

	// When
	Executable ex = () -> {
	    moveable.turnRight();
	    moveable.moveBackward(3);// tutet nicht so
	};
	// Then
	assertThrows(GridElementOutOfBoundsException.class, ex);
    }

    @Test
    public void testNotOutOfUpperBoundsYDefaultGrid() {

	// Given
	Moveable moveable = new SimpleMoveable(new DefaultGrid(10, 10), Positions.of(0, 0));
	Position expectedEndPosition = Positions.of(0, -1);
	// When
	moveable.moveBackward(1);
	// Then
	assertThat(moveable.getPosition(), is(expectedEndPosition));
    }

    @Test
    public void testAddElementNotOnGrid() {

	// Given
	Grid grid = new DefaultGrid();
	boolean isElementOnGrid = true;

	// When
	new ObstacleImpl(grid, Positions.of(1, 7.1));
	Obstacle antoherObstacle = new ObstacleImpl(new DefaultGrid(), Positions.of(1, 7.1));

	boolean isElementEffectivelyOnGrid = grid.containsElement(antoherObstacle);

	// Then
	assertThat(isElementEffectivelyOnGrid, is(not(isElementOnGrid)));
    }
}
