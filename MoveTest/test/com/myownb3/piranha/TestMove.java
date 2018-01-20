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
class TestMove {

    @Test
    void testMoveForwardOneTime() {

	// Given
	SimpleMoveable moveable = new SimpleMoveable(new Position(0, 0));

	Position startPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(0, 1);

	// When
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(startPosition, CoreMatchers.is(new Position(0, 0)));
	Assert.assertThat(endPosition, CoreMatchers.is(erwarteteEndPosition));
    }
}
