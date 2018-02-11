/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
class PositionTest {

    @Test
    public void testAngleCalculationFirstQuadrant() {

	// Given
	Position pos = Positions.of(5, 5);
	double expectedAngle = 45;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testNANAngleDirectionNorth() {

	// Given
	Position pos = Positions.of(0, 0);
	double actualAngle = 90;

	// When
	double expectedAngle = pos.calcAbsolutAngle();

	// Then
	assertThat(actualAngle, is(expectedAngle));
    }

    @Test
    public void testNANAngleDirectionSouth() {

	// Given
	Position pos = Positions.of(Directions.S, 0, 0);
	double actualAngle = 270;

	// When
	double expectedAngle = pos.calcAbsolutAngle();

	// Then
	assertThat(actualAngle, is(expectedAngle));
    }

    @Test
    public void testAngleCalculationSecondQuadrant1() {

	// Given
	Position pos = Positions.of(-5, 5);
	double expectedAngle = 135;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testAngleCalculationSecondQuadrant2() {

	// Given
	Position pos = Positions.of(-1, 7);
	double expectedAngle = 98.13;

	// When
	double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsolutAngle());

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testAngleCalculationSecondQuadrant3_YIsZero() {

	// Given
	Position pos = Positions.of(-5, 0);
	double expectedAngle = 180;

	// When
	double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsolutAngle());

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testAngleCalculationThirdQuadrant() {

	// Given
	Position pos = Positions.of(-5, -5);
	double expectedAngle = 225;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testAngleCalculationForthQuadrant1() {

	// Given
	Position pos = Positions.of(5, -5);
	double expectedAngle = 315;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testAngleCalculationForthQuadrant2() {

	// Given
	Position pos = Positions.of(1, -7);
	double expectedAngle = 278.13;

	// When
	double effectAngle = MathUtil.roundThreePlaces(pos.calcAbsolutAngle());

	// Then
	Assert.assertThat(effectAngle, is(expectedAngle));
    }

    @Test
    public void testSubstraction() {

	// Given
	Position startPoint = Positions.of(3, 7);
	Position endPoint = Positions.of(7, 4);
	double expectedDistance = 5;

	// When
	double effectDistance = endPoint.calcDistanceTo(startPoint);
	// Then
	Assert.assertThat(effectDistance, is(expectedDistance));
    }

    @Test
    public void testSubstraction2() {

	// Given
	Position startPoint = Positions.of(1, 2);
	Position endPoint = Positions.of(6, 3);
	double expectedDistance = 5.099;

	// When
	double effectDistance = endPoint.calcDistanceTo(startPoint);
	// Then
	Assert.assertThat(effectDistance, is(expectedDistance));
    }

    @Test
    void testHashCode() {

	// Given
	Position pos = Positions.of(Directions.N, 0, 0);

	// When
	Position anotherPos = Positions.of(Directions.N, 0, 0);

	// Then
	Assert.assertThat(anotherPos.hashCode(), is(pos.hashCode()));
    }

    @Test
    void testEquals() {

	// Given
	Position pos = Positions.of(Directions.N, 0, 0);

	// When
	Position anotherPos = Positions.of(Directions.N, 0, 0);

	// Then
	Assert.assertThat(pos, is(anotherPos));
	Assert.assertTrue(pos.equals(anotherPos));
	Assert.assertTrue(pos.equals(pos));
    }

    @Test
    void testNotEquals() {

	// Given
	Position pos = Positions.of(Directions.N, 0, 0);

	// When
	Position anotherNotExactlySamePos = Positions.of(Directions.N, 1, 0);
	Position anotherNotExactlySamePos2 = Positions.of(Directions.S, 0, 1);

	// Then
	Assert.assertFalse(pos.equals(null));
	Assert.assertFalse(pos.equals(new Object()));
	Assert.assertFalse(pos.equals(anotherNotExactlySamePos));
	Assert.assertFalse(pos.equals(anotherNotExactlySamePos2));
    }

    @Test
    void testToString() {

	// Given
	Position pos = Positions.of(Directions.N, 0, 0);
	// When
	Position anotherPos = Positions.of(Directions.N, 0, 0);

	// Then
	Assert.assertThat(pos.toString(), is(anotherPos.toString()));
    }
}
