/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.ObstacleImpl;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.detector.DetectorImpl;
import com.myownb3.piranha.moveables.helper.EvasionStateMachine;

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
	Assertions.assertThrows(NotImplementedException.class, ex);
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
    void test_MoveMultipleTargets() {

	// Given
	Grid grid = new DefaultGrid(300, 300, 0, 0);
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	Position endPos1 = Positions.of(0, 10);
	Position expectedEndPos = Positions.of(5, 15);
	MoveableController controller = new MoveableController(moveable, Arrays.asList(endPos1, expectedEndPos));

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(expectedEndPos), 0);
    }

    @Test
    void test_MoveMultipleTargets_ForwardCurved() {

	// Given
	Grid grid = new DefaultGrid(15, 15);
	Position endPos1 = Positions.of(0, 10);
	Position expectedEndPos = Positions.of(5, 15);

	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	MoveableController controller = new MoveableController(moveable, Arrays.asList(endPos1, expectedEndPos),
		MovingStrategie.FORWARD_CURVED);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(expectedEndPos), 0);
    }

    @Test
    void test_MoveForward_North_WithObstacle() {

	// Given
	Grid grid = new DefaultGrid(200, 200);
	ObstacleImpl obstacle = new ObstacleImpl(grid, Positions.of(0, 8));
	DetectorImpl detector = new DetectorImpl(8, 45, 15, /* 11.25 */ 5.625);
	Moveable moveable = new MoveableBuilder(grid)//
		.withHandler(new EvasionStateMachine(detector))//
		.build();

	Position expectedEndPos = Positions.of(0, 12);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(expectedEndPos), 0);
	assertThat(moveable.getPositionHistory().contains(obstacle.getPosition()), is(not(true)));
	assertThat(moveable.getPositionHistory().isEmpty(), is(not(true)));
    }

    @Test
    void test_MoveForward_NorthEast_WithObstacle() throws InterruptedException {

	// Given
	Grid grid = new DefaultGrid(200, 200);
	ObstacleImpl obstacle = new ObstacleImpl(grid, Positions.of(10, 10));
	DetectorImpl detector = new DetectorImpl(5, 45, 15, 5.625);
	Moveable moveable = new MoveableBuilder(grid)//
		.withHandler(new EvasionStateMachine(detector))//
		.build();

	Position expectedEndPos = Positions.of(28, 28);
	MoveableController controller = new MoveableController(moveable, expectedEndPos);

	// When
	controller.leadMoveable();

	// Then
	Position effectEndPos = moveable.getPosition();
	List<Position> trackingList = moveable.getPositionHistory();

	assertThat(trackingList.isEmpty(), is(not(true)));
	assertThat(trackingList.contains(obstacle.getPosition()), is(not(true)));
	com.myownb3.piranha.test.Assert.assertThatPosition(effectEndPos, is(expectedEndPos), 0);
	com.myownb3.piranha.launch.MoveableLauncher.visualizePositionsWithJFreeChart(trackingList, obstacle);
    }
}
