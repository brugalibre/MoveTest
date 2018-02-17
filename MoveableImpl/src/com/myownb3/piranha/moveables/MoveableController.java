/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Collections;
import java.util.List;

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

	default:
	    throw new IllegalArgumentException("Not supported Strategie '" + strategie + "'");
	}
    }

    /**
     * 
     */
    private void leadMoveableByStrategieForward() {

	for (Position position : endPosList) {
	    leadMoveable2EndPos(position);
	}
    }

    private void leadMoveable2EndPos(Position endPos) {

	// First turn the moveable in the right direction
	double diffAngle = moveable.getPosition().calcAngleRelativeTo(endPos);
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
}
