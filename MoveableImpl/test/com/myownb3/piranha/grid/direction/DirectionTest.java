/**
 * 
 */
package com.myownb3.piranha.grid.direction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.grid.direction.DirectionImpl;

/**
 * @author Dominic
 *
 */
class DirectionTest {

    @Test
    void testHashCode() {

	// Given
	Direction north = Directions.N;

	// When
	Direction anotherNorth = new DirectionImpl(90);

	// Then
	Assert.assertThat(north.hashCode(), is(anotherNorth.hashCode()));
    }

    @Test
    void testHashCodeNoCardinalDirection() {

	// Given
	Direction north = Directions.N;

	// When
	Direction anotherNorth = new DirectionImpl(90, null);

	// Then
	Assert.assertThat(north.hashCode(), is(not(anotherNorth.hashCode())));
    }

    @Test
    void testEquals() {

	// Given
	Direction north = Directions.N;

	// When
	Direction anotherNorth = new DirectionImpl(90, "N");
	Direction anotherNotExactlyNorth = new DirectionImpl(89, "N");

	// Then
	Assert.assertThat(north, is(anotherNorth));
	Assert.assertTrue(north.equals(anotherNorth));
	Assert.assertTrue(north.equals(north));
	Assert.assertFalse(north.equals(null));
	Assert.assertFalse(north.equals(new Object()));
	Assert.assertFalse(north.equals(anotherNotExactlyNorth));
	Assert.assertFalse(anotherNorth.equals(anotherNotExactlyNorth));
    }

    @Test
    void testEquals_NoCardinalDirection() {

	// Given
	Direction north = Directions.N;

	// When
	Direction anotherNorth = new DirectionImpl(90, null);

	// Then
	Assert.assertThat(anotherNorth, is(not(north)));
	Assert.assertThat(north, is(not(anotherNorth)));
    }

    @Test
    void testEquals_BothNoCardinalDirection() {

	// Given
	Direction north = new DirectionImpl(90, null);

	// When
	Direction anotherNorth = new DirectionImpl(90, null);

	// Then
	Assert.assertThat(anotherNorth, is(north));
	Assert.assertThat(north, is(anotherNorth));
    }

    @Test
    void testToString() {

	// Given
	String expectedToString = "Cardinal-Direction:N, Rotation: 90.0";
	// When
	Direction north = Directions.N;

	// Then
	Assert.assertThat(north.toString(), is(expectedToString));
    }
}