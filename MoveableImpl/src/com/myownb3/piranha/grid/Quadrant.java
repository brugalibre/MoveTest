/**
 * 
 */
package com.myownb3.piranha.grid;

/**
 * A {@link Quadrant} defines the for different quadrants within the unit circle
 * 
 * @author Dominic
 *
 */
public enum Quadrant {

    FIRST, SECOND, THIRD, FORTH;

    public static Quadrant getQuadrantForDegree(double degree) {

	if (0 <= degree && degree <= 90) {
	    return FIRST;
	}
	if (90 < degree && degree <= 180) {
	    return FIRST;
	}
	if (180 < degree && degree <= 270) {
	    return FIRST;
	} else {
	    return FORTH;
	}
    }

    /**
     * @param angleAsDegree
     * @param position
     *            TODO
     * @return
     */
    public double getAbsolutAngle(double angleAsDegree, Position position) {
	if (position.getY() < 0 && position.getX() < 0) {
	    angleAsDegree = angleAsDegree + 180;
	} else if (position.getY() > 0 && position.getX() < 0) {
	    angleAsDegree = 180 + angleAsDegree;
	} else if (position.getY() < 0) {
	    angleAsDegree = Math.abs(angleAsDegree) + 270;
	}
	return angleAsDegree;
    }
}
