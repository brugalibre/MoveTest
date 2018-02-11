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
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.detector.DetectorImpl;
import com.myownb3.piranha.moveables.helper.EvasionStateMachine;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
class MoveableControllerTest {

    @Test
    void test_MoveForward_NorthUnknownStrategie() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position expectedEndPos = Positions.of(0, 12);
	MoveableController controller = new MoveableController(moveable, expectedEndPos, MovingStrategie.BACKWARD);

	// When
	Executable ex = () -> {
	    controller.leadMoveable();
	};
	// Then
	Assertions.assertThrows(IllegalArgumentException.class, ex);
    }

    @Test
    void test_MoveForward_North() {

	// Given
	Grid grid = new DefaultGrid(20, 20);
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position expectedEndPos = Positions.of(0, 12);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_South() {

	// Given
	Grid grid = new DefaultGrid(20, 20);
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position expectedEndPos = Positions.of(0, -10);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(effectEndPos, is(expectedEndPos));
    }

    @Test
    void test_MoveForward_North_WithObstacle() {

	// Given
	Grid grid = new DefaultGrid(200, 200);
	new ObstacleImpl(grid, Positions.of(0, 8));
	DetectorImpl detector = new DetectorImpl(8, 45, 15, /* 11.25 */ 5.625);
	Moveable moveable = new MoveableBuilder(grid)//
		.withHelper(new EvasionStateMachine(detector))//
		.build();

	Position expectedEndPos = Positions.of(0, 12);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	Assert.assertThat(MathUtil.round(effectEndPos.getX(), 0), is(expectedEndPos.getX()));
	Assert.assertThat(MathUtil.round(effectEndPos.getY(), 0), is(expectedEndPos.getY()));
    }
}
