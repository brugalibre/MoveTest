/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * @author Dominic
 *
 */
public class MoveableController {

    private MovingStrategie strategie;
    private EndPointMoveable moveable;
    private List<Position> endPosList;
    private PostMoveForwardHandler handler;

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
	this.endPosList = Collections.emptyList();
	handler = result -> {
	};
    }

    public void leadMoveable() {
	switch (strategie) {
	case FORWARD:
	    leadMoveableStrategieForward();
	    break;

	default:
	    throw new NotImplementedException("Not supported Strategie '" + strategie + "'");
	}
    }

    private void leadMoveableStrategieForward() {
	if (endPosList.isEmpty()) {
	    leadMoveable2EndPos();
	}else {
	    leadMoveableWithEndPoints();
	}
    }

    private void leadMoveableWithEndPoints() {
	for (Position position : endPosList) {
	    moveable.setEndPosition(position);
	    leadMoveable2EndPos();
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
	    handler.handlePostMoveForward(moveResult);
	    if (moveResult.isDone()) {
		break;// We are done
	    }
	}
    }
    
    public static final class MoveableControllerBuilder {
	
	private List<Position> endPosList;
	private MovingStrategie movingStrategie;
	private PostMoveForwardHandler postMoveForwardHandler;
	private EndPointMoveable endPointMoveable;

	private MoveableControllerBuilder() {
	    // private
	}

	public static MoveableControllerBuilder builder () {
	    return new MoveableControllerBuilder();
	}

	public MoveableControllerBuilder withMoveable(Moveable moveable) {
	    return this;
	}
	
	public MoveableControllerBuilder withEndPointMoveable(EndPointMoveable endPointMoveable) {
	    this.endPointMoveable = endPointMoveable;
	    return this;
	}

	public MoveableControllerBuilder withEndPositions(List<Position> endPosList) {
	    this.endPosList = endPosList;
	    return this;
	}

	public MoveableControllerBuilder withStrategie(MovingStrategie movingStrategie) {
	    this.movingStrategie = movingStrategie;
	    return this;
	}

	public MoveableControllerBuilder withPostMoveForwardHandler(PostMoveForwardHandler postMoveForwardHandler) {
	    this.postMoveForwardHandler = postMoveForwardHandler;
	    return this;
	}

	public MoveableController build() {
	    MoveableController moveableController = new MoveableController(endPointMoveable);
	    moveableController.endPosList = endPosList;
	    moveableController.strategie = movingStrategie;
	    moveableController.handler = postMoveForwardHandler;
	    return moveableController;
	}
    }
}
