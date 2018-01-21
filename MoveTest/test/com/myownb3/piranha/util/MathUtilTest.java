/**
 * 
 */
package com.myownb3.piranha.util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author Dominic
 *
 */
class MathUtilTest {

    @Test
    void testRoundDown() {

	// Given
	double pi = 3.1234;
	double expectedRoundedPi = 3.12;

	// When
	double roundetPi = MathUtil.round(pi, 2);

	// Then
	Assert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
    }

    @Test
    void testRoundUp() {

	// Given
	double pi = 3.1264;
	double expectedRoundedPi = 3.13;

	// When
	double roundetPi = MathUtil.roundTwoPlaces(pi);

	// Then
	Assert.assertThat(roundetPi, CoreMatchers.is(expectedRoundedPi));
    }

    @Test
    void testRoundIllegalDecPlaces() {

	// Given
	double pi = 3.1234;

	// When
	Executable ex = () -> {
	    MathUtil.round(pi, -2);
	};
	// Then
	Assertions.assertThrows(IllegalArgumentException.class, ex);
    }
}
