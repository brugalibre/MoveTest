/**
 * 
 */
package com.myownb3.piranha.util;

/**
 * @author Dominic
 *
 */
public class MathUtil {

    public static double round(double value, int places) {

	if (places < 0 || places > 10) {
	    throw new IllegalArgumentException("The amount of decimal places must be between 0 and 10!");
	}
	double factor = calcFactor(places);
	return (double) Math.round(value * factor) / factor;
    }

    private static double calcFactor(int places) {
	double factor = 1;

	for (int i = 1; i <= places; i++) {
	    factor = factor * 10;
	}
	return factor;
    }

    public static double roundThreePlaces(double value) {
	return round(value, 3);
    }

    /**
     * Returns a random number considering the given offset
     * 
     * @param offset
     *            the offset
     * @return a random number
     */
    public static double getRandom(int offset) {
	return Math.random() * offset;
    }
}
