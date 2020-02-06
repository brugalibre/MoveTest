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
}
