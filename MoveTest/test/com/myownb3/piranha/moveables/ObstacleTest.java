/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Obstacle;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Positions;

/**
 * @author Dominic
 *
 */
class ObstacleTest {

    @Test
    public void testMovebaleRecognizesObjectTrueRightSideOfNorht() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(2, 7));
	Moveable moveable = new SimpleMoveable(grid, Positions.of(1, 1));
	boolean isRecognized = true;

	// When
	boolean hasObjectRecognized = moveable.hasObjectRecognized(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(isRecognized));
    }

    @Test
    public void testMovebaleRecognizesObjectTrueLeftSideOfNorht() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(-2, 7));
	Moveable moveable = new SimpleMoveable(grid, Positions.of(1, 1));
	boolean isRecognized = true;

	// When
	boolean hasObjectRecognized = moveable.hasObjectRecognized(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(isRecognized));
    }

    @Test
    public void testMovebaleRecognizesObjectFalseWrongAngle() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(3, 7));
	Moveable moveable = new SimpleMoveable(grid, Positions.of(1, 1));
	boolean notRecognized = false;

	// When
	boolean hasObjectRecognized = moveable.hasObjectRecognized(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(notRecognized));
    }

    @Test
    public void testMovebaleRecognizesObjectFalseOutOfReach() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(20, 70));
	Moveable moveable = new SimpleMoveable(grid, Positions.of(1, 1));
	boolean notRecognized = false;

	// When
	boolean hasObjectRecognized = moveable.hasObjectRecognized(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(notRecognized));
    }
}
