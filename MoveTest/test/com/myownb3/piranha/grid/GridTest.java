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
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.Moveable;

/**
 * @author Dominic
 *
 */
class GridTest {

    @Test
    public void testGridDimensionGrid() {

	// Given
	int minX = -5;
	int minY = -5;
	int maxX = 5;
	int maxY = 5;

	int expectedX = -5;
	int expectedY = -5;
	int expectedHeight = 10;
	int expectedWidth = 10;

	// When
	Grid grid = new DefaultGrid(maxX, maxY, minX, minY);
	Dimension dimension = grid.getDimension();

	// Then
	assertThat(dimension.getX(), is(expectedX));
	assertThat(dimension.getY(), is(expectedY));
	assertThat(dimension.getHeight(), is(expectedHeight));
	assertThat(dimension.getWidth(), is(expectedWidth));
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

	Moveable moveable = new MoveableBuilder(grid, Positions.of(1, 7.1))//
		.build();
	boolean isElementEffectivelyOnGridAfterMove = grid.containsElement(moveable);

	moveable.moveForward(5);
	boolean isElementEffectivelyOnGridBeforeMove = grid.containsElement(moveable);

	// Then
	assertThat(isElementEffectivelyOnGridBeforeMove, is(isElementOnGridBeforeMove));
	assertThat(isElementEffectivelyOnGridAfterMove, is(isElementOnGridAfterMove));
    }

    @Test
    public void testOutOfBoundsWhenCreatingNewGridElement() {

	// Given
	Grid grid = new DefaultGrid();

	// When
	Executable ex = () -> {
	    new MoveableBuilder(grid, Positions.of(20, 20))//
		    .build();
	};
	// Then
	assertThrows(GridElementOutOfBoundsException.class, ex);
    }

    @Test
    public void testOutOfUpperBoundsXDefaultGrid() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();

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
	Moveable moveable = MoveableBuilder.builder()//
		.build();

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
	Moveable moveable = new MoveableBuilder(new DefaultGrid(10, 10, 0, 0))//
		.build();

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
	Moveable moveable = new MoveableBuilder(new DefaultGrid(10, 10, 0, 0))//
		.build();

	// When
	Executable ex = () -> {
	    moveable.turnRight();
	    moveable.moveBackward(3);
	};
	// Then
	assertThrows(GridElementOutOfBoundsException.class, ex);
    }

    @Test
    public void testNotOutOfUpperBoundsYDefaultGrid() {

	// Given
	Moveable moveable = MoveableBuilder.builder()//
		.build();
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
