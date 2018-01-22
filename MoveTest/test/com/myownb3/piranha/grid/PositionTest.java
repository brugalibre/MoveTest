/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.moveables.DirectionDefs;

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
	Assert.assertThat(expectedAngle, is(effectAngle));
    }

    @Test
    public void testAngleCalculationSecondQuadrant() {

	// Given
	Position pos = Positions.of(-5, 5);
	double expectedAngle = 135;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(expectedAngle, is(effectAngle));
    }

    @Test
    public void testAngleCalculationThirdQuadrant() {

	// Given
	Position pos = Positions.of(-5, -5);
	double expectedAngle = 225;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(expectedAngle, is(effectAngle));
    }

    @Test
    public void testAngleCalculationForthQuadrant() {

	// Given
	Position pos = Positions.of(5, -5);
	double expectedAngle = 315;

	// When
	double effectAngle = pos.calcAbsolutAngle();

	// Then
	Assert.assertThat(expectedAngle, is(effectAngle));
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
	Assert.assertThat(expectedDistance, is(effectDistance));
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
	Assert.assertThat(expectedDistance, is(effectDistance));
    }

    @Test
    void testHashCode() {

	// Given
	Position pos = Positions.of(DirectionDefs.N, 0, 0);

	// When
	Position anotherPos = Positions.of(DirectionDefs.N, 0, 0);

	// Then
	Assert.assertThat(anotherPos.hashCode(), is(pos.hashCode()));
    }

    @Test
    void testEquals() {

	// Given
	Position pos = Positions.of(DirectionDefs.N, 0, 0);

	// When
	Position anotherPos = Positions.of(DirectionDefs.N, 0, 0);
	Position anotherNotExactlySamePos = Positions.of(DirectionDefs.N, 0, 1);
	Position anotherNotExactlySamePos2 = Positions.of(DirectionDefs.S, 0, 1);

	// Then
	Assert.assertThat(pos, CoreMatchers.is(anotherPos));
	Assert.assertTrue(pos.equals(anotherPos));
	Assert.assertTrue(pos.equals(pos));
	Assert.assertFalse(pos.equals(null));
	Assert.assertFalse(pos.equals(new Object()));
	Assert.assertFalse(pos.equals(anotherNotExactlySamePos));
	Assert.assertFalse(pos.equals(anotherNotExactlySamePos2));
    }

    @Test
    void testToString() {

	// Given
	Position pos = Positions.of(DirectionDefs.N, 0, 0);
	// When
	Position anotherPos = Positions.of(DirectionDefs.N, 0, 0);

	// Then
	Assert.assertThat(pos.toString(), is(anotherPos.toString()));
    }
}
