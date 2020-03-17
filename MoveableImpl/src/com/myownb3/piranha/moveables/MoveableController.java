/**
 * 
 */
package com.myownb3.piranha.moveables;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.myownb3.piranha.exception.NotImplementedException;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.postaction.MoveablePostActionHandler;

/**
 * @author Dominic
 *
 */
public class MoveableController {

    private MovingStrategie strategie;
    private EndPointMoveable moveable;
    private List<Position> endPosList;
    private PostMoveForwardHandler handler;
    private boolean isRunning;

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
	isRunning = true;
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
	while (isRunning) {
	    MoveResult moveResult = moveable.moveForward2EndPos();
	    handler.handlePostMoveForward(moveResult);
	    if (moveResult.isDone()) {
		break;// We are done
	    }
	}
    }
    

    /**
     * Stops this {@link MoveableController}
     */
    public void stop() {
	isRunning = false;
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

	public MoveableControllerBuilder withMoveable(EndPointMoveable endPointMoveable) {
	    this.endPointMoveable = endPointMoveable;
	    return this;
	}
	
	public EndPointMoveableBuilder withEndPointMoveable() {
	    return new EndPointMoveableBuilder(this);
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
	
	public static final class EndPointMoveableBuilder {

	    private MoveableControllerBuilder controllerBuilder;
	    private EndPointMoveable moveable;
	    private MoveablePostActionHandler handler;
	    private Position startPosition;
	    private Grid grid;
	    private Position endPos;
	    private int movingIncrement;

	    public static EndPointMoveableBuilder builder() {
		return new EndPointMoveableBuilder();
	    }
	    
	    private EndPointMoveableBuilder() {
		movingIncrement = 1;
		handler = (a, b) -> {
		};
	    }

	    private EndPointMoveableBuilder(MoveableControllerBuilder moveableControllerBuilder) {
		this();
		this.controllerBuilder = moveableControllerBuilder;
	    }

	    public EndPointMoveableBuilder withGrid(Grid grid) {
		this.grid = grid;
		return this;
	    }
	    
	    public EndPointMoveableBuilder withStartPosition(Position position) {
		this.startPosition = position;
		return this;
	    }

	    public EndPointMoveableBuilder withEndPosition(Position position) {
		this.endPos = position;
		return this;
	    }

	    public EndPointMoveableBuilder withHandler(MoveablePostActionHandler handler) {
		this.handler = Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
		return this;
	    }

	    public EndPointMoveableBuilder withMovingIncrement(int movingIncrement) {
		this.movingIncrement = movingIncrement;
		return this;
	    }

	    public EndPointMoveable build() {
		Objects.requireNonNull(grid, "Attribute 'grid' must not be null!");
		Objects.requireNonNull(startPosition, "Attribute 'startPosition' must not be null!");
		moveable = new EndPointMoveableImpl(grid, startPosition, handler, endPos, movingIncrement);
		handler.handlePostConditions(moveable.getGrid(), moveable);
		return this.moveable;
	    }
	    
	    public MoveableControllerBuilder buildAndReturnParentBuilder() {
		build();
		controllerBuilder.endPointMoveable = moveable;
		return controllerBuilder;
	    }

	    public EndPointMoveableBuilder widthEndPosition(Position endPos) {
		this.endPos = endPos;
		return this;
	    }
	}
    }

    public Moveable getMoveable() {
	return moveable;
    }
}
