/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.Direction;
import com.myownb3.piranha.moveables.DirectionDefs;
import com.myownb3.piranha.moveables.GridElement;
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
     * @param degree
     */
    @Override
    public void rotate(int degree) {
	direction = direction.rotate(degree);
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
    public double calcDistanceTo(Position position) {

	Position distanceVector = Positions.of(position.getX() - x, position.getY() - y);
	double sqrt = Math
		.sqrt(distanceVector.getX() * distanceVector.getX() + distanceVector.getY() * distanceVector.getY());
	return MathUtil.roundThreePlaces(sqrt);
    }

    /**
     * Returns the angle of the {@link GridElement}
     */
    @Override
    public double calcAbsolutAngle() {

	double angleAsRadiant = Math.atan(getY() / getX());
	double angleAsDegree = MathUtil.toDegree(angleAsRadiant);

	// x-axis is negativ -> absolut value of angle + 90 (since we are looking from
	// the absolut zeor point
	angleAsDegree = getAbsolutAngle(angleAsDegree);
	return angleAsDegree;
    }

    /*
     * Calcualtes the absolut value depending on the quadrant this position is lying
     * on
     *
     *@formatter:off
     *  ___________
     * |     |     |
     * |  2. |  1. |
     * |____ |_____|
     * |     |     |
     * |  3. |  4. |
     * |_____|_____|
     * 
     *@formatter:on
     */
    private double getAbsolutAngle(double angleAsDegree) {

	if (y < 0 && x < 0) {
	    // 3. Quadrant
	    angleAsDegree = angleAsDegree + 180;
	} else if (y > 0 && x < 0) {
	    // 2. Quadrant
	    angleAsDegree = 180 + angleAsDegree;
	} else if (y < 0) {
	    // 4. Quadrant
	    angleAsDegree = Math.abs(angleAsDegree) + 270;
	}
	return angleAsDegree;
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
