/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.grid.Positions;

/**
 * @author Dominic
 *
 */
public class MoveableController {

    private MovingStrategie strategie;

    private Moveable moveable;
    private List<Position> endPosList;

    /**
     * @param moveable
     * @param endPos
     */
    public MoveableController(Moveable moveable, Position endPos) {

	this(moveable, Collections.singletonList(endPos));
    }

    /**
     * @param moveable
     * @param endPos
     */
    public MoveableController(Moveable moveable, Position endPos, MovingStrategie strategie) {

	this(moveable, Collections.singletonList(endPos), strategie);
    }

    /**
     * @param moveable
     * @param endPos
     */
    public MoveableController(Moveable moveable, List<Position> endPosList, MovingStrategie strategie) {

	this.moveable = moveable;
	this.endPosList = endPosList;
	this.strategie = strategie;
    }

    /**
     * @param moveable2
     * @param asList
     */
    public MoveableController(Moveable moveable, List<Position> endPosList) {
	this(moveable, endPosList, MovingStrategie.FORWARD);
    }

    /**
     * 
     */
    public void leadMoveable() {

	switch (strategie) {
	case FORWARD:
	    leadMoveableByStrategieForward();
	    break;

	case FORWARD_CURVED:
	    leadMoveableByStrategieForwardCurved();
	    break;

	default:
	    throw new NotImplementedException("Not supported Strategie '" + strategie + "'");
	}
    }

    private void leadMoveableByStrategieForwardCurved() {

	for (Position position : endPosList) {
	    leadMoveable2EndPosCurved(position);
	}
    }

    private void leadMoveableByStrategieForward() {

	for (Position position : endPosList) {
	    leadMoveable2EndPos(position);
	}
    }

    /*
     * First turn the moveable in the right direction then move forward until we
     * reach our end position.
     */
    private void leadMoveable2EndPos(Position endPos) {

	Position startPos = Positions.of(moveable.getPosition());
	double diffAngle = moveable.getPosition().calcAngleRelativeTo(endPos);
	moveable.makeTurn(diffAngle);

	double distance = endPos.calcDistanceTo(startPos);
	while (distance >= 1) {
	    moveable.moveForward();
	    distance = endPos.calcDistanceTo(moveable.getPosition());
	}
    }

    /*
     * move forward until we reach our end position. Make a slightly turn before
     * each move
     */
    private void leadMoveable2EndPosCurved(Position endPos) {

	double distance = endPos.calcDistanceTo(moveable.getPosition());
	double origAngle2Turn = moveable.getPosition().calcAngleRelativeTo(endPos);

	int turnFactor = (int) (distance / 2);

	double angle2Turn = origAngle2Turn;
	while (distance >= 1 || angle2Turn != 0) {

	    angle2Turn = turnIfNecessary(endPos, origAngle2Turn, turnFactor);
	    distance = moveForwardIfNecessary(endPos, distance);
	}
    }

    private double turnIfNecessary(Position endPos, double origAngle2Turn, int turnFactor) {

	double angle2Turn = moveable.getPosition().calcAngleRelativeTo(endPos);
	if (angle2Turn != 0) {

	    double diffAngle = getAngle2TurnIncrement(origAngle2Turn, angle2Turn, turnFactor);
	    moveable.makeTurn(diffAngle);
	    angle2Turn = angle2Turn - diffAngle;
	}
	return angle2Turn;
    }

    private double moveForwardIfNecessary(Position endPos, double distance) {
	if (distance >= 1) {
	    moveable.moveForward();
	    return endPos.calcDistanceTo(moveable.getPosition());
	}
	return distance;
    }

    /*
     * Returns the next angle increment to turn the moveable around. If this
     * increment is bigger than the delta to its origin angle, than this delta is
     * returned
     */
    private double getAngle2TurnIncrement(double origAngle2Turn, double angle2Turn, int turnFactor) {

	double diffAngle = (int) (origAngle2Turn / turnFactor);

	if (Math.abs(diffAngle) >= Math.abs(angle2Turn)) {
	    diffAngle = angle2Turn;
	}
	return diffAngle;
    }
}