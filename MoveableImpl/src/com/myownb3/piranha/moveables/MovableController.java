/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
public class MovableController {

    private MovingStrategie strategie;

    private Moveable movable;
    private Position endPos;

    /**
     * @param moveable
     * @param endPos
     */
    public MovableController(Moveable moveable, Position endPos) {

	this(moveable, endPos, MovingStrategie.FORWRD);
    }

    /**
     * @param moveable
     * @param endPos
     */
    public MovableController(Moveable moveable, Position endPos, MovingStrategie strategie) {

	this.movable = moveable;
	this.endPos = endPos;
	this.strategie = strategie;
    }

    /**
     * 
     */
    public void leadMovable() {

	switch (strategie) {
	case FORWRD:
	    leadMovableByStrategieForward();
	    break;

	default:
	    throw new IllegalArgumentException("Unknown Strategi '" + strategie + "'");
	}
    }

    /**
     * 
     */
    private void leadMovableByStrategieForward() {
	Position startPos = movable.getPosition();

	// First turn the movable in the right direction
	double diffAngle = calcDiffAngle(movable.getPosition());
	movable.makeTurn(diffAngle);

	// now move forward until we reach ouer end position
	double distanceTo = endPos.calcDistanceTo(startPos);
	for (int i = 0; i < distanceTo; i++) {
	    movable.moveForward();
	}
    }

    /*
     * calculate the angle in which direction the movable has to be turned in order
     * to reach the end position
     */
    private double calcDiffAngle(Position position) {
	double endAngle = endPos.calcAbsolutAngle();
	return endAngle - position.getDirection().getAngle();
    }
}
