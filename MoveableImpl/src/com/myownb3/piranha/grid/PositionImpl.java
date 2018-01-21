/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.Direction;
import com.myownb3.piranha.moveables.DirectionDefs;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class PositionImpl implements Position {

    private double y;
    private double x;
    private Direction direction;

    /**
     * @param x
     * @param y
     */
    public PositionImpl(double x, double y) {
	this(DirectionDefs.N, x, y);
    }

    public PositionImpl(Direction direction, double x, double y) {
	this.direction = direction;
	this.x = MathUtil.roundThreePlaces(x);
	this.y = MathUtil.roundThreePlaces(y);
    }

    /**
     * @param dregree
     */
    @Override
    public void rotate(int dregree) {
	direction = direction.rotate(dregree);
    }

    /**
     * @return the direction
     */
    @Override
    public Direction getDirection() {
	return this.direction;
    }

    @Override
    public final double getY() {
	return this.y;
    }

    @Override
    public final double getX() {
	return this.x;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	double result = 1;
	result = prime * result + this.x;
	result = prime * result + this.y;
	return (int) result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	PositionImpl other = (PositionImpl) obj;
	if (this.x != other.x)
	    return false;
	if (this.y != other.y)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Direction: '" + direction + "', X-Axis: '" + x + "', Y-Axis: '" + y + "'";
    }
}
