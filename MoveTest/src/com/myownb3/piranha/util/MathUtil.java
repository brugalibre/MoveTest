/**
 * 
 */
package com.myownb3.piranha.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Dominic
 *
 */
public class MathUtil {

    public static double round(double value, int places) {
	if (places < 0) {
	    throw new IllegalArgumentException("No negativ values allowe for '" + places + "'!");
	}

	BigDecimal bd = new BigDecimal(value);
	bd = bd.setScale(places, RoundingMode.HALF_UP);
	return bd.doubleValue();
    }

    public static double roundTwoPlaces(double value) {
	return round(value, 2);
    }
}
