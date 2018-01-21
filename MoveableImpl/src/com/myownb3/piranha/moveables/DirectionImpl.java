/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dominic
 */
public class DirectionImpl implements Direction {

    public static final DirectionImpl N = new DirectionImpl(90, "N");
    public static final DirectionImpl O = new DirectionImpl(0, "O");
    public static final DirectionImpl S = new DirectionImpl(270, "S");
    public static final DirectionImpl W = new DirectionImpl(180, "W");

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
    DirectionImpl(int rotation, String cardinalDirection) {
	this.rotation = rotation;
	this.cardinalDirection = cardinalDirection;
    }

    /**
     * @param rotation
     * 
     */
    DirectionImpl(int rotation) {
	this.rotation = rotation;
	setCardinalDirection();
    }

    @Override
    public Direction makeTurn(int degree) {
	int rotationTmp = (this.rotation + degree) % 360;
	if (rotationTmp < 0) {
	    rotationTmp = 360 + rotationTmp;
	}
	return new DirectionImpl(rotationTmp);
    }

    @Override
    public double getForwardX() {
	return Math.cos(toRadian(rotation));
    }

    @Override
    public double getForwardY() {
	return Math.sin(toRadian(rotation));
    }

    @Override
    public double getBackwardX() {
	return -getForwardX();
    }

    @Override
    public double getBackwardY() {
	return -getForwardY();
    }

    /**
     * Returns the radiant for the given amount of degrees
     * 
     * @param rotation
     * @return the radiant for the given amount of degrees
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
	if (!(obj instanceof DirectionImpl)) {
	    return false;
	}
	DirectionImpl other = (DirectionImpl) obj;
	if (this.rotation != other.rotation) {
	    return false;
	}
	if (this.cardinalDirection == null && other.cardinalDirection != null) {
	    return false;
	} else if (!this.cardinalDirection.equals(other.cardinalDirection)) {
	    return false;
	}
	return true;
    }

}