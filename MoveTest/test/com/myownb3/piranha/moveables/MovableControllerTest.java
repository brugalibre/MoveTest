/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;

/**
 * @author Dominic
 *
 */
class MovableControllerTest {

    @Test
    void test_MoveForward_NorthUnknownStrategie() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new SimpleMoveable(grid, Positions.of(0, 0));

	Position expectedEndPos = Positions.of(0, 12);
	MovableController controller = new MovableController(moveable, expectedEndPos, MovingStrategie.BACKWARD);

	// When
	Executable ex = () -> {
	    controller.leadMovable();
	};
	// Then
	Assertions.assertThrows(IllegalArgumentException.class, ex);
    }

    @Test
    void test_MoveForward_North() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new SimpleMoveable(grid, Positions.of(0, 0));

	Position expectedEndPos = Positions.of(0, 12);
	MovableController controller = new MovableController(moveable, expectedEndPos);

	// When
	controller.leadMovable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_South() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new SimpleMoveable(grid, Positions.of(0, 0));

	Position expectedEndPos = Positions.of(0, -10);
	MovableController controller = new MovableController(moveable, expectedEndPos);

	// When
	controller.leadMovable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_North_WithObstacle() {

	// Given
	Grid grid = new DefaultGrid();
	new ObstacleImpl(grid, Positions.of(0, 5));
	Moveable moveable = new SimpleAvoidableMoveable(grid, Positions.of(0, 0), new DetectorImpl(), true);

	Position expectedEndPos = Positions.of(0, 12);
	MovableController controller = new MovableController(moveable, expectedEndPos);

	// When
	controller.leadMovable();

	// Then
	// Position effectEndPos = moveable.getPosition();
	// Assert.assertThat(effectEndPos, is(expectedEndPos));
    }
}
