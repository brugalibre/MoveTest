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
    public void testMirror1Quadrant_45() {

	// Given
	Grid grid = new MirrorGrid(10, 10);
	Position position = Positions.of(Directions.N, 0, 0);
	Position position2 = Positions.of(Directions.N, 6, 0);
	position.rotate(-45);
	position2.rotate(-45);
	double expectedEndDegree = 135;

	// When
	for (int i = 0; i < 15; i++) {
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
    public void testMirror2Quadrant_45() {

	// Given
	Grid grid = new MirrorGrid(10, 10);
	Position position = Positions.of(Directions.N, 0, 0);
	Position position2 = Positions.of(Directions.N, 4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 45;

	// When
	for (int i = 0; i < 15; i++) {
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
    public void testMirror3Quadrant_45() {

	// Given
	Grid grid = new MirrorGrid(10, 10);
	Position position = Positions.of(Directions.W, 0, 0);
	Position position2 = Positions.of(Directions.W, 6.5, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 315;

	// When
	for (int i = 0; i < 15; i++) {
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
    public void testMirror4Quadrant_45() {

	// Given
	Grid grid = new MirrorGrid(10, 10);
	Position position = Positions.of(Directions.S, 0, 0);
	Position position2 = Positions.of(Directions.S, 7.4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 225;

	// When
	for (int i = 0; i < 15; i++) {
	    position = grid.moveForward(position);
	    position2 = grid.moveForward(position2);
	}
	double actualEndDegree = position.getDirection().getAngle();
	double actualEndDegree2 = position2.getDirection().getAngle();

	// Then
	assertThat(actualEndDegree, is(expectedEndDegree));
	assertThat(actualEndDegree2, is(expectedEndDegree));
    }
}
