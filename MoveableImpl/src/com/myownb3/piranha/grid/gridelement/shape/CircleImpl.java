/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;

/**
 * @author Dominic
 *
 */
public class CircleImpl extends AbstractShape implements Circle {

    private int amountOfPoints;
    private int radius;
    private Position center;

    private CircleImpl(List<Position> path, Position center, int amountOfPoints, int radius) {
	super(path);
	this.radius = Math.abs(radius);
	this.center = center;
	this.amountOfPoints = verifyAmountOfPoints(amountOfPoints);
    }

    /**
     * @param amountOfPoints2
     * @return
     */
    private int verifyAmountOfPoints(int amountOfPoints) {
	if (amountOfPoints < 4) {
	    throw new IllegalArgumentException("We need at least 4 points for a circle!");
	}
	return amountOfPoints;
    }

    @Override
    public int getRadius() {
        return radius;
    }
    
    @Override
    public Position getCenter() {
	return center;
    }

    public static class CircleBuilder {

	private int radius;
	private int amountOfPoints;
	private Position center;

	public CircleBuilder(int radius) {
	    this.radius = radius;
	}

	public CircleBuilder withCenter(Position center) {
	    this.center = center;
	    return this;
	}

	public CircleBuilder withAmountOfPoints(int amountOfPoints) {
	    this.amountOfPoints = amountOfPoints;
	    return this;
	}

	public CircleImpl build() {
	    requireNonNull(center);
	    List<Position> path = buildCircleWithCenter(center, amountOfPoints, radius);
	    return new CircleImpl(path, center, amountOfPoints, radius);
	}
    }

    @Override
    public void transform(Position position) {
	this.center = position;
	this.path = buildCircleWithCenter(position, amountOfPoints, radius);
    }

    private static List<Position> buildCircleWithCenter(Position center, int amountOfPoints, int radius) {
	List<Position> path = new ArrayList<>();
	double degInc = 360 / amountOfPoints;
	double deg = 0;
	for (int i = 0; i < amountOfPoints; i++) {
	    Position pos = getNextCirclePos(center, radius, deg);
	    deg = deg + degInc;
	    path.add(pos);
	}

	return path;
    }

    private static Position getNextCirclePos(Position center, int radius, double deg) {
	Position pos = Positions.of(center);
	pos.rotate(deg);
	return Positions.movePositionForward(pos, radius);
    }
}
