/**
 * 
 */
package com.myownb3.piranha.moveables;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.moveables.Direction;
import com.myownb3.piranha.moveables.DirectionImpl;

/**
 * @author Dominic
 *
 */
class DirectionTest {

    @Test
    void testHashCode() {

	// Given
	Direction north = DirectionImpl.N;

	// When
	Direction anotherNorth = new DirectionImpl(90);

	// Then
	Assert.assertThat(north.hashCode(), is(anotherNorth.hashCode()));
    }

    @Test
    void testEquals() {

	// Given
	Direction north = DirectionImpl.N;

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
    void testToString() {

	// Given
	String expectedToString = "N";
	// When
	DirectionImpl north = DirectionImpl.N;

	// Then
	Assert.assertThat(north.toString(), is(expectedToString));
    }
}
