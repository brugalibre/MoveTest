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
	Position expectedStartPosition = new Position(0, 0);
	SimpleMoveable moveable = new SimpleMoveable(expectedStartPosition);

	Position effectStartPosition = moveable.getPosition();
	Position erwarteteEndPosition = new Position(0, 1);

	// When
	moveable.moveForward();

	// Then
	Position endPosition = moveable.getPosition();

	Assert.assertThat(effectStartPosition, CoreMatchers.is(expectedStartPosition));
	Assert.assertThat(endPosition, CoreMatchers.is(erwarteteEndPosition));
    }
}
