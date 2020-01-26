/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import static com.myownb3.piranha.statemachine.states.EvasionStates.DEFAULT;
import static com.myownb3.piranha.statemachine.states.EvasionStates.EVASION;
import static com.myownb3.piranha.statemachine.states.EvasionStates.PASSING;
import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;
import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;

import java.util.HashMap;
import java.util.Map;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.helper.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.DefaultStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.EvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.PassingStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.PostEvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.ReturningStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.input.PassingEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.output.DefaultStateResult;
import com.myownb3.piranha.statemachine.impl.handler.output.PostEvasionEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * An {@link EvasionStateMachine} completes the {@link EvasionMoveableHelper}
 * because the {@link EvasionStateMachine} can handle the complete evasion
 * maneuvre evasion
 * 
 * @author Dominic
 *
 */
public class EvasionStateMachine extends DetectableMoveableHelper {

    protected EvasionStates evasionState;

    private Map<EvasionStates, EvasionStatesHandler<?>> evasionStatesHandler2StateMap;

    private Position positionBeforeEvasion;

    public EvasionStateMachine(Detector detector) {
	this(detector, null);
    }
    
    public EvasionStateMachine(Detector detector, Position endPos) {
	super(detector);
	createAndInitHandlerMap(endPos);
	evasionState = DEFAULT;
	positionBeforeEvasion = null;
    }

    private void createAndInitHandlerMap(Position endPos) {
	evasionStatesHandler2StateMap = new HashMap<>();
	evasionStatesHandler2StateMap.put(DEFAULT, new DefaultStateHandler());
	evasionStatesHandler2StateMap.put(EVASION, new EvasionStateHandler());
	evasionStatesHandler2StateMap.put(POST_EVASION, new PostEvasionStateHandler());
	evasionStatesHandler2StateMap.put(PASSING, new PassingStateHandler());
	evasionStatesHandler2StateMap.put(RETURNING, new ReturningStateHandler(endPos));
    }

    @Override
    public void handlePostConditions(Grid grid, Moveable moveable) {
	super.handlePostConditions(grid, moveable);
	switch (evasionState) {
	case DEFAULT:
	    DefaultStateHandler defaultStateHandler = getHandler(DefaultStateHandler.class);
	    DefaultStateResult defaultStateResult = defaultStateHandler.handle(CommonEventStateInput.of(grid, moveable, this));
	    setPositionBeforeEvasion(defaultStateResult);
	    evasionState = defaultStateResult.getNextState();
	    break;
	case EVASION:
	    EvasionStateHandler evasionStateHandler = getHandler(EvasionStateHandler.class);
	    CommonEventStateResult evasionStateResult = evasionStateHandler.handle(EvasionEventStateInput.of(grid, moveable, detector, this));
	    evasionState = evasionStateResult.getNextState();
	    break;
	case POST_EVASION:
	    PostEvasionStateHandler postEvastionStateHandler = getHandler(PostEvasionStateHandler.class);
	    PostEvasionEventStateResult postEvasionEventStateResult = postEvastionStateHandler.handle(PostEvasionEventStateInput.of(positionBeforeEvasion, moveable));
	    evasionState = postEvasionEventStateResult.getNextState();
	    break;
	case PASSING:
	    PassingStateHandler passingStateHandler = getHandler(PassingStateHandler.class);
	    CommonEventStateResult passingStateResult = passingStateHandler.handle(PassingEventStateInput.of(grid, moveable, positionBeforeEvasion, this, detector.getEvasionDistance()));
	    evasionState = passingStateResult.getNextState();
	    break;
	case RETURNING:
	    ReturningStateHandler returningStateHandler = getHandler(ReturningStateHandler.class);
	    CommonEventStateResult eventStateResult = returningStateHandler.handle(ReturningEventStateInput.of(detector, positionBeforeEvasion, moveable));
	    evasionState = eventStateResult.getNextState();
	    break;
	default:
	    throw new IllegalStateException("Unknown state'" + evasionState + "'");
	}
    }

    private Position setPositionBeforeEvasion(DefaultStateResult evenStateResult) {
	return positionBeforeEvasion = evenStateResult.getPositionBeforeEvasion().orElse(positionBeforeEvasion);
    }

    @SuppressWarnings("unchecked")
    private <T extends EvasionStatesHandler<?>> T getHandler(Class<T> eventStateInputClass) {
	if (evasionStatesHandler2StateMap.containsKey(evasionState)) {
	    return (T) evasionStatesHandler2StateMap.get(evasionState);
	}
	throw new IllegalStateException("No EvasionStatesHandler registered for state '" + evasionState + "'");
    }
}
