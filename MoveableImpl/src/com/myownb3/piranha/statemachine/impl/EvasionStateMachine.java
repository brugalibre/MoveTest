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
import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.defaultstate.DefaultStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.EvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.passingstate.PassingStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.passingstate.input.PassingEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.postevasion.PostEvasionStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.postevasion.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.returning.ReturningStateHandler;
import com.myownb3.piranha.statemachine.impl.handler.returning.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * An {@link EvasionStateMachine} completes the {@link EvasionMoveableHelper}
 * because the {@link EvasionStateMachine} can handle the complete evasion
 * maneuvre evasion
 * 
 * The minimum distance to correctly recognize an {@link Obstacle} and handle an evasion maneuvre
 * is three ints. So e.g the first Obstacle is placed at Position (5, 5) and
 * that means, the second can be placed the earliest at Position (8, 8)
 * 
 * @author Dominic
 *
 */
public class EvasionStateMachine extends DetectableMoveableHelper {

    @Visible4Testing
    EvasionStates evasionState;
    private Map<EvasionStates, EvasionStatesHandler<?>> evasionStatesHandler2StateMap;
    private EvasionStateMachineConfig config;
    private Position positionBeforeEvasion;

    public EvasionStateMachine(Detector detector, EvasionStateMachineConfig config) {
	this(detector, null, config);
    }

    public EvasionStateMachine(Detector detector, Position endPos, EvasionStateMachineConfig config) {
	super(detector);
	this.config = config;
	createAndInitHandlerMap(endPos);
	evasionState = DEFAULT;
	positionBeforeEvasion = null;
    }

    private void createAndInitHandlerMap(Position endPos) {
	evasionStatesHandler2StateMap = new HashMap<>();
	evasionStatesHandler2StateMap.put(DEFAULT, new DefaultStateHandler());
	evasionStatesHandler2StateMap.put(EVASION, new EvasionStateHandler());
	evasionStatesHandler2StateMap.put(POST_EVASION, new PostEvasionStateHandler(endPos, config.getPostEvasionAngleAdjustStepWidth()));
	evasionStatesHandler2StateMap.put(PASSING, new PassingStateHandler(config.getPassingDistance()));
	evasionStatesHandler2StateMap.put(RETURNING, new ReturningStateHandler(endPos, config));
    }

    @Override
    public void handlePostConditions(Grid grid, Moveable moveable) {
	beforePostConditions(grid, moveable);
	CommonEventStateResult eventStateResult = handlePostConditionsInternal(grid, moveable);
	afterPostConditions (eventStateResult);
    }

    private CommonEventStateResult handlePostConditionsInternal(Grid grid, Moveable moveable) {
	CommonEventStateResult eventStateResult = null;
	switch (evasionState) {
	case DEFAULT:
	    DefaultStateHandler defaultStateHandler = getHandler();
	    eventStateResult = defaultStateHandler.handle(CommonEventStateInput.of(grid, moveable, this));
	    evasionState = eventStateResult.getNextState();
	    break;
	case EVASION:
	    EvasionStateHandler evasionStateHandler = getHandler();
	    eventStateResult = evasionStateHandler.handle(EvasionEventStateInput.of(grid, moveable, detector, this));
	    evasionState = eventStateResult.getNextState();
	    break;
	case POST_EVASION:
	    PostEvasionStateHandler postEvastionStateHandler = getHandler();
	    eventStateResult = postEvastionStateHandler.handle(PostEvasionEventStateInput.of(this, grid, moveable, positionBeforeEvasion));
	    evasionState = eventStateResult.getNextState();
	    break;
	case PASSING:
	    PassingStateHandler passingStateHandler = getHandler();
	    eventStateResult = passingStateHandler.handle(PassingEventStateInput.of(this, grid, moveable, positionBeforeEvasion));
	    evasionState = eventStateResult.getNextState();
	    break;
	case RETURNING:
	    ReturningStateHandler returningStateHandler = getHandler();
	    eventStateResult = returningStateHandler.handle(ReturningEventStateInput.of(this, grid, moveable, positionBeforeEvasion));
	    evasionState = eventStateResult.getNextState();
	    break;
	default:
	    throw new IllegalStateException("Unknown state'" + evasionState + "'");
	}
	return eventStateResult;
    }

    private void afterPostConditions(CommonEventStateResult eventStateResult) {
	boolean isEvadingNow = eventStateResult.getPrevState() != EVASION && evasionState == EVASION;
	if (isEvadingNow) {
	    setPositionBeforeEvasion(eventStateResult.getPositionBeforeEvasion());
	    initHandlers();
	}
    }

    private void beforePostConditions(Grid grid, Moveable moveable) {
	super.handlePostConditions(grid, moveable);
    }

    private void initHandlers() {
	evasionStatesHandler2StateMap.values()
		.stream()
		.forEach(EvasionStatesHandler::init);
    }
    
    private Position setPositionBeforeEvasion(Optional<Position> optionalPos) {
	return positionBeforeEvasion = optionalPos.orElse(positionBeforeEvasion);
    }

    private <T extends EvasionStatesHandler<?>> T getHandler() {
	return getHandler4State(evasionState);
    }

    @SuppressWarnings("unchecked")
    private <T extends EvasionStatesHandler<?>> T getHandler4State(EvasionStates state) {
	if (evasionStatesHandler2StateMap.containsKey(state)) {
	    return (T) evasionStatesHandler2StateMap.get(state);
	}
	throw new IllegalStateException("No EvasionStatesHandler registered for state '" + state + "'");
    }
}
