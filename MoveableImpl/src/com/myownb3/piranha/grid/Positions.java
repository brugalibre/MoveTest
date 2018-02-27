/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.direction.Directions;
import com.myownb3.piranha.util.MathUtil;

/**
 * @author Dominic
 *
 */
public class Positions {

    /**
     * Creates a new {@link Position} with the given coordinates
     * 
     * @param x
     *            the x-axis coordinate
     * @param y
     *            the y-axis coordinate
     * @return a new created Position
     */
    public static Position of(double x, double y) {
	return new PositionImpl(x, y);
    }

    /**
     * Creates a new {@link Position} with the given coordinates
     * 
     * @param x
     *            the x-axis coordinate
     * @param y
     *            the y-axis coordinate
     * @return a new created Position
     */
    public static Position of(Direction direction, double x, double y) {
	return new PositionImpl(direction, x, y);
    }

    /**
     * Creates a copy of the given {@link Position}
     * 
     * @param position
     * @return a copy of the given {@link Position}
     */
    public static Position of(Position position) {
	Direction direction = Directions.of(position.getDirection());
	return Positions.of(direction, position.getX(), position.getY());
    }

    /**
     * Returns a new {@link PositionImpl} within the borders of the given
     * {@link Dimension} considering the given height and width of the Position
     * 
     * @param dimension
     * @param height
     * @param width
     * @return a new created Position
     */
    public static Position getRandomPosition(Dimension dimension, int height, int width) {

	int minX = dimension.getX();
	int maxX = minX + dimension.getWidth();
	int minY = dimension.getY();
	int maxY = minX + dimension.getHeight();

	int x = (int) Math.min(MathUtil.getRandom(maxX) + minX, maxX - width / 2);
	int y = (int) Math.min(MathUtil.getRandom(maxY) + minY, maxY - height / 2);

	return Positions.of(x, y);
    }

    private static class PositionImpl implements Position {

	private double y;
	private double x;
	private Direction direction;

	/**
	 * @param x
	 * @param y
	 */
	public PositionImpl(double x, double y) {
	    this(Directions.N, x, y);
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
	public void rotate(double degree) {
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
	    double sqrt = Math.sqrt(
		    distanceVector.getX() * distanceVector.getX() + distanceVector.getY() * distanceVector.getY());
	    return MathUtil.roundThreePlaces(sqrt);
	}

	@Override
	public double calcAngleRelativeTo(Position position) {

	    double absDeltaX = position.getX() - x;
	    double absDeltaY = position.getY() - y;
	    Position distanceVector = Positions.of(absDeltaX, absDeltaY);

	    return distanceVector.calcAbsolutAngle() - direction.getAngle();
	}

	/**
	 * Returns the angle of the {@link GridElement}
	 */
	@Override
	public double calcAbsolutAngle() {

	    double angleAsRadiant = getAngleAsRadiant();
	    double angleAsDegree = MathUtil.toDegree(angleAsRadiant);

	    // x-axis is negative -> absolute value of angle + 90 (since we are looking from
	    // the absolute zero point)
	    angleAsDegree = getAbsolutAngle(angleAsDegree);
	    return MathUtil.roundThreePlaces(angleAsDegree);
	}

	/**
	 * @return the current angle of this position as radiant. Special case to
	 *         handle: If this position is currently at 0.0, then we return PI/2 or
	 *         -PI/2 since a calculation of {@link Math#atan(0/0)} returns a NAN
	 * 
	 */
	private double getAngleAsRadiant() {

	    if (getY() == 0 && getX() == 0) {
		return MathUtil.toRadian(direction.getAngle());
	    }
	    return Math.atan(getY() / getX());
	}

	/*
	     * Calculates the absolute value depending on the quadrant this position is located
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
	    } else if (y >= 0 && x < 0) {
		// 2. Quadrant
		angleAsDegree = 180 + angleAsDegree;
	    } else if (y < 0) {
		// 4. Quadrant
		angleAsDegree = angleAsDegree + 360;
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
}
