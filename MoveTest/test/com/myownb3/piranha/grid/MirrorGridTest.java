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
class MirrorGridTest {

    @Test
    public void testMirror1Quadrant_45_X() {

	// Given
	Grid grid = new MirrorGrid(20, 10, 0, 0);
	Position position = Positions.of(Directions.N, 0, 0);
	Position position2 = Positions.of(Directions.N, 6, 0);
	position.rotate(-45);
	position2.rotate(-45);
	double expectedEndDegree = 135;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    /**
     * @param grid
     * @param position
     * @param position2
     * @param expectedEndDegree
     */
    private void testMirrorInternal(Grid grid, Position position, Position position2, double expectedEndDegree) {
	// When
	for (int i = 0; i < 150; i++) {
	    position = grid.moveForward(position);
	    position2 = grid.moveForward(position2);
	}
	double actualEndDegree = position.getDirection().getAngle();
	double actualEndDegree2 = position2.getDirection().getAngle();

	// Then
	assertThat(actualEndDegree, is(expectedEndDegree));
	assertThat(actualEndDegree2, is(expectedEndDegree));
    }

    @Test
    public void testMirror2Quadrant_45_X() {

	// Given
	Grid grid = new MirrorGrid(20, 15);
	Position position = Positions.of(Directions.N, 0, 0);
	Position position2 = Positions.of(Directions.N, 4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 45;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    @Test
    public void testMirror3Quadrant_45_X() {

	// Given
	Grid grid = new MirrorGrid(20, 15, 0, -20);
	Position position = Positions.of(Directions.W, 0, 0);
	Position position2 = Positions.of(Directions.W, 6.5, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 315;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    @Test
    public void testMirror4Quadrant_45_X() {

	// Given
	Grid grid = new MirrorGrid(20, 10, 0, -20);
	Position position = Positions.of(Directions.S, 0, 0);
	Position position2 = Positions.of(Directions.S, 7.4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 225;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    @Test
    public void testMirror1Quadrant_45_Y() {

	// Given
	Grid grid = new MirrorGrid(10, 200, -200, 0);
	Position position = Positions.of(Directions.N, 0, 0);
	Position position2 = Positions.of(Directions.N, 7.4, 0);
	position.rotate(-45);
	position2.rotate(-45);
	double expectedEndDegree = 315;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    @Test
    public void testMirror2Quadrant_45_Y() {

	// Given
	Grid grid = new MirrorGrid(10, 200, -200, 0);
	Position position = Positions.of(Directions.N, 0, 0);
	Position position2 = Positions.of(Directions.N, 4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 225;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    @Test
    public void testMirror3Quadrant_45_Y() {

	// Given
	Grid grid = new MirrorGrid(15, 200, -200, 0);
	Position position = Positions.of(Directions.W, 0, 0);
	Position position2 = Positions.of(Directions.W, 6.5, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 135;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

    @Test
    public void testMirror4Quadrant_45_Y() {

	// Given
	Grid grid = new MirrorGrid(2010, 200, -200, 0);
	Position position = Positions.of(Directions.S, 0, 0);
	Position position2 = Positions.of(Directions.S, 7.4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 45;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

}
