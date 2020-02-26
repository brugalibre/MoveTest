/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;

/**
 * @author Dominic
 *
 */
class MirrorGridTest {

    @Test
    public void testMirror1Quadrant_45_X() {

	// Given
	Grid grid = MirrorGridBuilder.builder()//
		.withMaxX(20)//
		.withMaxY(10)//
		.withMinX(0)//
		.withMinY(0)//
		.build();
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
	Grid grid = MirrorGridBuilder.builder(15, 20)//
		.build();
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
	Grid grid = MirrorGridBuilder.builder()//
			.withMaxX(20)//
			.withMaxY(15)//
			.withMinX(0)//
			.withMinY(-20)//
			.build();
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
	Grid grid = MirrorGridBuilder.builder()//
		.withMaxX(20)//
		.withMaxY(10)//
		.withMinX(0)//
		.withMinY(-20)//
		.build();
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
	Grid grid = MirrorGridBuilder.builder()//
		.withMaxX(10)//
		.withMaxY(200)//
		.withMinX(-200)//
		.withMinY(0)//
		.build(); 
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
	Grid grid = MirrorGridBuilder.builder()//
		.withMaxX(10)//
		.withMaxY(200)//
		.withMinX(-200)//
		.withMinY(0)//
		.build();  
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
	Grid grid = MirrorGridBuilder.builder()//
		.withMaxX(15)//
		.withMaxY(200)//
		.withMinX(-200)//
		.withMinY(0)//
		.build(); 
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
	Grid grid = MirrorGridBuilder.builder()//
		.withMaxX(2010)//
		.withMaxY(200)//
		.withMinX(-200)//
		.withMinY(0)//
		.build();
	Position position = Positions.of(Directions.S, 0, 0);
	Position position2 = Positions.of(Directions.S, 7.4, 0);
	position.rotate(45);
	position2.rotate(45);
	double expectedEndDegree = 45;

	testMirrorInternal(grid, position, position2, expectedEndDegree);
    }

}
