/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.exception.NotImplementedException;

/**
 * @author Dominic
 *
 */
public class MoveableController {

    private MovingStrategie strategie;

    private EndPointMoveable moveable;

    /**
     * @param moveable
     */
    public MoveableController(EndPointMoveable moveable) {
	this(moveable, MovingStrategie.FORWARD);
    }

    /**
     * @param moveable
     */
    public MoveableController(EndPointMoveable moveable, MovingStrategie strategie) {
	this.moveable = moveable;
	this.strategie = strategie;
    }

    public void leadMoveable() {
	switch (strategie) {
	case FORWARD:
	    leadMoveable2EndPos();
	    break;

	default:
	    throw new NotImplementedException("Not supported Strategie '" + strategie + "'");
	}
    }

    /*
     * First turn the moveable in the right direction then move forward until we
     * reach our end position.
     */
    private void leadMoveable2EndPos() {
	moveable.prepare();
	while (true) {
	    MoveResult moveResult = moveable.moveForward2EndPos();
	    if (moveResult.isDone()) {
		break;// We are done
	    }
	}
    }
}
