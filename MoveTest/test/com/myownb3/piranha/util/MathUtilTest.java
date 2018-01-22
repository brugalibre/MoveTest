/**
 * 
 */
package com.myownb3.piranha.util;

import static org.hamcrest.CoreMatchers.is;

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
    public void testRadiant2Degree() {

	// Given
	int degree = 180;
	int degree2 = 90;
	double expectedResult = Math.PI;
	double expectedResult2 = Math.PI / 2;

	// When
	double effectResult = MathUtil.toRadian(degree);
	double effectResult2 = MathUtil.toRadian(degree2);
	// Then
	Assert.assertThat(expectedResult, is(effectResult));
	Assert.assertThat(expectedResult2, is(effectResult2));
    }

    @Test
    public void testDegree2Radiant() {

	// Given
	double radiant = Math.PI;
	double expectedResult = 180;
	double radiant2 = Math.PI * 2;
	double expectedResult2 = 360;

	// When
	double effectResult = MathUtil.toDegree(radiant);
	double effectResult2 = MathUtil.toDegree(radiant2);
	// Then
	Assert.assertThat(expectedResult, is(effectResult));
	Assert.assertThat(expectedResult2, is(effectResult2));
    }

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
    void testRoundUp2() {

	// Given
	double number = -3.53553;
	double expectedRoundedNumber = -3.54;

	// When
	double roundetNumber = MathUtil.round(number, 2);

	// Then
	Assert.assertThat(roundetNumber, CoreMatchers.is(expectedRoundedNumber));
    }

    @Test
    void testRoundUp() {

	// Given
	double pi = 3.1265;
	double expectedRoundedPi = 3.127;

	// When
	double roundetPi = MathUtil.roundThreePlaces(pi);

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
