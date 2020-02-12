package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static java.util.Objects.isNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public class ReturningStateHandler extends CommonStateHandlerImpl<ReturningEventStateInput> {

    private Position endPos;
    private double returningAngle;
    private double distanceMargin;
    private double angleMargin;

    private ReturnStates state;
    private double initialDistance;
    private int signum;

    /**
     * Creates a new {@link ReturningStateHandler} with the given
     * {@link EvasionStateMachineConfig} and ent-position
     * 
     * @param endPos
     *            the final end position
     * @param config
     *            the {@link EvasionStateMachineConfig}
     */
    public ReturningStateHandler(Position endPos, EvasionStateMachineConfig config) {
	this(endPos, config.getReturningAngleIncMultiplier(), config.getReturningMinDistance(), config.getReturningAngleMargin(), config.getEvasionAngleInc());
    }

    @Override
    public void init() {
	super.init();
	state = ReturnStates.ENTER_RETURNING;
	this.initialDistance = 0;
	this.signum = 0;
    }
    
    private ReturningStateHandler(Position endPos, int angleIncMultiplier, double distanceMargin, double angleMargin,
	    double evasionAngleInc) {
	super();
	this.endPos = endPos;
	this.returningAngle = evasionAngleInc * angleIncMultiplier;
	this.distanceMargin = distanceMargin;
	this.angleMargin = angleMargin;
	init();
    }

    @Override
    public CommonEventStateResult handle(ReturningEventStateInput evenStateInput) {
	if (isReturningNotNecessary()) {
	    init();
	    return CommonEventStateResult.of(RETURNING, evalNextState(evenStateInput, RETURNING.nextState()), null);
	}
	EvasionStates nextState = handleReturning(evenStateInput.getPositionBeforeEvasion(),
		evenStateInput.getMoveable());
	return evalNextStateAndBuildResult(evenStateInput, RETURNING, nextState);
    }

    private EvasionStates handleReturning(Position positionBeforeEvasion, Moveable moveable) {
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion, endPos);
	switch (state) {
	case ENTER_RETURNING:
	    handleFirstAngleCorrection(positionBeforeEvasion, moveable, endPosLine);
	    state = ReturnStates.ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL;
	    return RETURNING;
	case ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL:
	    doCorrectionPhase1(moveable, positionBeforeEvasion, endPosLine);
	    return evalNextState4StateUntilOrdonal(positionBeforeEvasion, moveable, endPosLine);
	case ANGLE_CORRECTION_PHASE_FROM_ORDONAL:
	    doCorrectionPhase2(moveable, positionBeforeEvasion, endPosLine);
	    return evalNextState4StateFromOrdonal(positionBeforeEvasion, moveable, endPosLine);
	default:
	    throw new IllegalStateException("Unhandled State '" + state + "'");
	}
    }

    private EvasionStates evalNextState4StateFromOrdonal(Position positionBeforeEvasion, Moveable moveable,
	    Float64Vector endPosLine) {
	if (facesSameDirection(moveable, endPosLine)) {
	    return RETURNING.nextState();
	}
	return evalNextState(positionBeforeEvasion, moveable, endPosLine);
    }

    private EvasionStates evalNextState4StateUntilOrdonal(Position positionBeforeEvasion, Moveable moveable,
	    Float64Vector endPosLine) {
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	if (currentDistance <= (initialDistance / 2) || currentAngle >= 90.0d) {
	    state = ReturnStates.ANGLE_CORRECTION_PHASE_FROM_ORDONAL;
	    return RETURNING;
	}
	return evalNextState(positionBeforeEvasion, moveable, endPosLine);
    }

    private void doCorrectionPhase1(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	double actualDiff = 90.0d - currentAngle;
	double angle2Turn = Math.min(actualDiff, getAngle2Turn());
	moveable.makeTurnWithoutPostConditions(angle2Turn);
    }

    private void doCorrectionPhase2(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	double actualDiff = 0 - currentAngle;
	double angle2Turn = Math.max(actualDiff, getAngle2Turn());
	moveable.makeTurnWithoutPostConditions(-angle2Turn);
    }

    private EvasionStates evalNextState(Position positionBeforeEvasion, Moveable moveable, Float64Vector endPosLine) {
	// The moveable is on the endPosLine and faces into the right direction -> we
	// are done here
	if (isMoveableOnEndPosDirection(endPosLine, positionBeforeEvasion, moveable.getPosition())
		&& facesSameDirection(moveable, endPosLine)) {
	    init();
	    return RETURNING.nextState();
	}
	return RETURNING;
    }

    private void handleFirstAngleCorrection(Position positionBeforeEvasion, Moveable moveable,
	    Float64Vector endPosLine) {
	Position moveablePos = moveable.getPosition();
	this.initialDistance = calcDistanceFromPositionToLine(moveablePos, positionBeforeEvasion, endPosLine);
	signum = calcSignum(moveablePos, positionBeforeEvasion, endPosLine, returningAngle);
	if (facesSameDirection(moveable, endPosLine)) {
	    return;
	}
	double angle2Turn = getAngle2Turn();
	moveable.makeTurnWithoutPostConditions(angle2Turn);
    }

    private double getAngle2Turn() {
	return signum * returningAngle / 1;
    }

    /*
     * Defines if the moveable faces into the same direction then the
     * 'end-Position'-line
     * 
     * Mathematically this is defined by the angle between the line created by 'the
     * point before evasion' and 'the final end-position' and the line defined by
     * the direction of the moveable
     */
    private boolean facesSameDirection(Moveable moveable, Float64Vector endPosLine) {
	double angle = calcAngle(moveable.getPosition(), endPosLine);
	return angleMargin >= angle && angle >= 0.0d;
    }

    /*
     * Verifies if the Movebale is on the line created by 'the point bevore evasion'
     * and 'the final end-position'
     * 
     */
    private boolean isMoveableOnEndPosDirection(Float64Vector endPosDirectionVector, Position positionBeforeEvasion,
	    Position moveablePos) {
	double distance = MathUtil.calcDistanceFromPositionToLine(moveablePos, positionBeforeEvasion,
		endPosDirectionVector);
	return distance <= distanceMargin;
    }

    /*
     * If there is no end-point, then we can't really return anywhere. Thats why
     * we're done here
     */
    private boolean isReturningNotNecessary() {
	return isNull(endPos);
    }

    private static enum ReturnStates {

	/**
	 * This is the default state. The first time the {@link EvasionStateMachine}
	 * handles the {@link EvasionStates#RETURNING} this state is set
	 */
	ENTER_RETURNING,

	/**
	 * After the second time we enter the {@link EvasionStates#RETURNING} and until
	 * the angle of the {@link Moveable} is corrected
	 */
	ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL,

	ANGLE_CORRECTION_PHASE_FROM_ORDONAL,

	NONE,
    }
}