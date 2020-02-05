/**
 * 
 */
package com.myownb3.piranha.moveables;

import static com.myownb3.piranha.util.MathUtil.round;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;

/**
 * @author Dominic
 *
 */
public class EndPointMoveableImpl extends AbstractMoveable implements EndPointMoveable {

    private Position endPos;
    private int movingIncrement;
    private double prevDistance;

    public EndPointMoveableImpl(Grid grid, Position position, MoveablePostActionHandler handler, Position endPosition,
	    int movingIncrement) {
	super(grid, position, handler);
	this.movingIncrement = movingIncrement;
	this.endPos = endPosition;
	prevDistance = endPos.calcDistanceTo(position);
    }

    @Override
    public void prepare() {
	double diffAngle = position.calcAngleRelativeTo(endPos);
	makeTurn(diffAngle);
    }

    /*
     * First turn the moveable in the right direction then move forward until we
     * reach our end position.
     */
    @Override
    public MoveResult moveForward2EndPos() {
	double distance = endPos.calcDistanceTo(position);
	if (distance >= getSmallestStepWith()) {
	    moveForward(movingIncrement);
	    distance = endPos.calcDistanceTo(position);
	    if (round(distance, 5) > round(prevDistance, 5)) {
		return new MoveResultImpl(distance, prevDistance, true);
	    }
	    prevDistance = distance;
	    return new MoveResultImpl(distance, prevDistance);
	}
	return new MoveResultImpl(distance, prevDistance, true);
    }

//    private boolean has2MoveOr2Turn(double distance, double angle2Turn) {
//	return (distance >= getSmallestStepWith() || angle2Turn != 0);
//    }
//
//    private double turnIfNecessary(Position endPos, double origAngle2Turn, int turnFactor) {
//	double angle2Turn = position.calcAngleRelativeTo(endPos);
//	if (angle2Turn != 0) {
//	    double diffAngle = getAngle2TurnIncrement(origAngle2Turn, angle2Turn, turnFactor);
//	    makeTurn(diffAngle);
//	    angle2Turn = angle2Turn - diffAngle;
//	}
//	return angle2Turn;
//    }
//
//    private double moveForwardIfNecessary(Position endPos, double distance) {
//	if (distance >= getSmallestStepWith()) {
//	    moveForward();
//	    return endPos.calcDistanceTo(position);
//	}
//	return distance;
//    }
//
//    /*
//     * Returns the next angle increment to turn the moveable around. If this
//     * increment is bigger than the delta to its origin angle, than this delta is
//     * returned
//     */
//    private double getAngle2TurnIncrement(double origAngle2Turn, double angle2Turn, int turnFactor) {
//
//	double diffAngle = (int) (origAngle2Turn / turnFactor);
//
//	if (Math.abs(diffAngle) >= Math.abs(angle2Turn)) {
//	    diffAngle = angle2Turn;
//	}
//	return diffAngle;
//    }
}
