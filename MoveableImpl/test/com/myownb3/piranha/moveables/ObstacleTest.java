/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;

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
	Detector detector = new DetectorImpl();
	MoveableBuilder.builder(grid, Positions.of(1, 1))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean isRecognized = true;

	// When
	boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(isRecognized));
    }

    @Test
    public void testMovebaleRecognizesObjectTrueLeftSideOfNorht() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(-1, 7));
	Detector detector = new DetectorImpl();
	MoveableBuilder.builder(grid, Positions.of(1, 1))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean isRecognized = true;

	// When
	boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(isRecognized));
    }

    @Test
    public void testMovebaleRecognizesObjectFalseWrongAngle() {

	// Given
	Grid grid = new DefaultGrid();
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(4, 7));
	Detector detector = new DetectorImpl();

	MoveableBuilder.builder(grid, Positions.of(1, 1))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();

	boolean notRecognized = false;

	// When
	boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(notRecognized));
    }

    @Test
    public void testMovebaleRecognizesObjectFalseOutOfReach() {

	// Given
	Grid grid = new DefaultGrid(100, 100);
	Obstacle obstacle = new ObstacleImpl(grid, Positions.of(20, 70));
	Detector detector = new DetectorImpl();
	MoveableBuilder.builder(grid, Positions.of(1, 1))//
		.withHandler(new DetectableMoveableHelper(detector))//
		.build();
	boolean notRecognized = false;

	// When
	boolean hasObjectRecognized = detector.hasObjectDetected(obstacle);

	// Then
	Assert.assertThat(hasObjectRecognized, is(notRecognized));
    }
}