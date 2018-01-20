/**
 * 
 */
package com.myownb3.piranha;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author Dominic
 *
 */
class DirectionTest {

    @Test
    void testHashCode() {

	// Given
	Direction north = Direction.N;

	// When
	Direction anotherNorth = new Direction(90);

	// Then
	Assert.assertThat(north.hashCode(), CoreMatchers.is(anotherNorth.hashCode()));
    }

    @Test
    void testEquals() {

	// Given
	Direction north = Direction.N;

	// When
	Direction anotherNorth = new Direction(90);
	Direction anotherNotExactlyNorth = new Direction(89);

	// Then
	Assert.assertThat(north, CoreMatchers.is(anotherNorth));
	Assert.assertTrue(north.equals(anotherNorth));
	Assert.assertFalse(north.equals(null));
	Assert.assertFalse(north.equals(new Object()));
	Assert.assertFalse(north.equals(anotherNotExactlyNorth));
    }
}
