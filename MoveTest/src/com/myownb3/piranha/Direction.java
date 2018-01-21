/**
 * 
 */
package com.myownb3.piranha;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dominic
 */
public class Direction {

    public static final Direction N = new Direction(90, "N");
    public static final Direction O = new Direction(0, "O");
    public static final Direction S = new Direction(270, "S");
    public static final Direction W = new Direction(180, "W");

    private static final Map<Integer, String> degree2DirectionMap;
    static {
	degree2DirectionMap = getDegree2DirectionMap();
    }

    private int rotation;
    private String cardinalDirection;

    /**
     * @param rotation
     * 
     */
    private Direction(int rotation, String cardinalDirection) {
	this.rotation = rotation;
	this.cardinalDirection = cardinalDirection;
    }

    /**
     * @param rotation
     * 
     */
    /* package */ Direction(int rotation) {
	this.rotation = rotation;
	setCardinalDirection();
    }

    public Direction turnDegree(int degree) {
	int rotationTmp = (this.rotation + degree) % 360;
	if (rotationTmp < 0) {
	    rotationTmp = 360 + rotationTmp;
	}
	return new Direction(rotationTmp);
    }

    /**
     * @return
     */
    public double getForwardX() {
	return Math.cos(toRadian(rotation));
    }

    /**
     * @return
     */
    public double getForwardY() {
	return Math.sin(toRadian(rotation));
    }

    /**
     * @return
     */
    public double getBackwardX() {
	return -getForwardX();
    }

    /**
     * @return
     */
    public double getBackwardY() {
	return -getForwardY();
    }

    /**
     * Returns the radian for the given amount of degrees
     * 
     * @param rotation
     * @return the radian for the given amount of degrees
     */
    private double toRadian(int rotation) {
	return rotation * (Math.PI / 180);
    }

    private void setCardinalDirection() {
	this.cardinalDirection = degree2DirectionMap.get(rotation);
    }

    @Override
    public String toString() {
	return cardinalDirection;
    }

    private static Map<Integer, String> getDegree2DirectionMap() {
	Map<Integer, String> degree2DirectionMap = new HashMap<>();

	degree2DirectionMap.put(90, "N");
	degree2DirectionMap.put(0, "O");
	degree2DirectionMap.put(270, "S");
	degree2DirectionMap.put(180, "W");

	return Collections.unmodifiableMap(degree2DirectionMap);
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((this.cardinalDirection == null) ? 0 : this.cardinalDirection.hashCode());
	result = prime * result + this.rotation;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Direction)) {
	    return false;
	}
	Direction other = (Direction) obj;
	if (this.cardinalDirection == null) {
	    if (other.cardinalDirection != null) {
		return false;
	    }
	} else if (!this.cardinalDirection.equals(other.cardinalDirection)) {
	    return false;
	}
	if (this.rotation != other.rotation) {
	    return false;
	}
	return true;
    }

}