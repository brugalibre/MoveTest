/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;

/**
 * @author Dominic
 *
 */
public class MoveableController {

    private MovingStrategie strategie;

    private Moveable moveable;
    private Position endPos;

    /**
     * @param moveable
     * @param endPos
     */
    public MoveableController(Moveable moveable, Position endPos) {

	this(moveable, endPos, MovingStrategie.FORWRD);
    }

    /**
     * @param moveable
     * @param endPos
     */
    public MoveableController(Moveable moveable, Position endPos, MovingStrategie strategie) {

	this.moveable = moveable;
	this.endPos = endPos;
	this.strategie = strategie;
    }

    /**
     * 
     */
    public void leadMoveable() {

	switch (strategie) {
	case FORWRD:
	    leadMoveableByStrategieForward();
	    break;

	default:
	    throw new IllegalArgumentException("Not supported Strategie '" + strategie + "'");
	}
    }

    private void leadMoveableByStrategieForward() {

	// First turn the moveable in the right direction
	double diffAngle = calcDiffAngle(moveable.getPosition());
	moveable.makeTurn(diffAngle);
	Position startPos = Positions.of(moveable.getPosition());

	// now move forward until we reach our end position
	final double distanceOring = endPos.calcDistanceTo(startPos);
	double distance = distanceOring;
	while (distance >= 1) {
	    moveable.moveForward();
	    distance = endPos.calcDistanceTo(moveable.getPosition());
	}
    }

    /*
     * calculate the angle in which direction the moveable has to be turned in order
     * to reach the end position
     */
    private double calcDiffAngle(Position position) {
	double endAngle = endPos.calcAbsolutAngle();
	return endAngle - position.getDirection().getAngle();
    }
}
