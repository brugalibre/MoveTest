/**
 * 
 */
package com.myownb3.piranha.grid;

import static org.hamcrest.CoreMatchers.is;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.PositionImpl;
import com.myownb3.piranha.moveables.DirectionDefs;

/**
 * @author Dominic
 *
 */
class PositionTest {

    @Test
    void testHashCode() {

	// Given
	PositionImpl pos = new PositionImpl(DirectionDefs.N, 0, 0);

	// When
	PositionImpl anotherPos = new PositionImpl(DirectionDefs.N, 0, 0);

	// Then
	Assert.assertThat(anotherPos.hashCode(), is(pos.hashCode()));
    }

    @Test
    void testEquals() {

	// Given
	PositionImpl pos = new PositionImpl(DirectionDefs.N, 0, 0);

	// When
	PositionImpl anotherPos = new PositionImpl(DirectionDefs.N, 0, 0);
	PositionImpl anotherNotExactlySamePos = new PositionImpl(DirectionDefs.N, 0, 1);
	PositionImpl anotherNotExactlySamePos2 = new PositionImpl(DirectionDefs.S, 0, 1);

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
	PositionImpl pos = new PositionImpl(DirectionDefs.N, 0, 0);
	// When
	PositionImpl anotherPos = new PositionImpl(DirectionDefs.N, 0, 0);

	// Then
	Assert.assertThat(pos.toString(), is(anotherPos.toString()));
    }
}
