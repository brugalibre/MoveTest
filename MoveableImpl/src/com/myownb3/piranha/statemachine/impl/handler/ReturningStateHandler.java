package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.grid.vector.VectorUtil.getVector;
import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;
import static java.util.Objects.isNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public class ReturningStateHandler extends CommonStateHandlerImpl<ReturningEventStateInput> {

    private Position endPos;
    private int angleIncMultiplier;
    private double evasionAngleInc;
    private double distanceMargin;
    private double angleMargin;

    private double prevDistance;
    private ReturnStates state;
    private int signum;

    /**
     * Creates a new {@link ReturningStateHandler} with the given
     * end-{@link Position}
     * 
     * @param endPos
     *            the final end position
     * @param angleIncMultiplier
     *            the multiplier used to calculate the angle for correction maneuver
     * @param distanceMargin
     *            the minimal distance to the vector which shows the direction to
     *            the end-point (a {@link Moveable} has to reach
     *            @param angleMargin margin between the actual angle from the {@link Moveable} to it's position before the evasion
     * @param evasionAngleInc
     *            the angle used to turn the {@link Moveable}
     */
    public ReturningStateHandler(Position endPos, int angleIncMultiplier, double distanceMargin, double angleMargin, double evasionAngleInc) {
	super();
	this.endPos = endPos;
	this.angleIncMultiplier = angleIncMultiplier;
	this.evasionAngleInc = evasionAngleInc;
	this.distanceMargin = distanceMargin;
	this.angleMargin = angleMargin;
	init();
    }

    @Override
    public void init() {
	super.init();
	signum = 1;
	prevDistance = 0;
	state = ReturnStates.ENTER_RETURNING;
    }

    @Override
    public CommonEventStateResult handle(ReturningEventStateInput evenStateInput) {
	if (isNull(endPos)) {
	    return CommonEventStateResult.of(evalNextState(evenStateInput, RETURNING.nextState()));
	}
	EvasionStates nextState = handleReturning(evenStateInput.getPositionBeforeEvasion(), evenStateInput.getMoveable());
	return evalNextStateAndBuildResult(evenStateInput, nextState);
    }

    private EvasionStates handleReturning(Position positionBeforeEvasion, Moveable moveable) {

	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion);

	switch (state) {
	case ENTER_RETURNING:
	    handleFirstAngleCorrection(positionBeforeEvasion, moveable, endPosLine);
	    break;
	case ANGLE_CORRECTION:
	    handleStateAngleCorrection(moveable, positionBeforeEvasion, endPosLine);
	    break;
	case GETTING_CLOSER:
	    handleStateGettingCloser(moveable, positionBeforeEvasion, endPosLine);
	    break;
	default:
	    break;
	}

	double angle = calcAngle(moveable, endPosLine);
	// The moveable is on the endPosLine and faces into the right direction -> we
	// are done here
	if (isMoveableOnEndPosDirection(endPosLine, positionBeforeEvasion, moveable.getPosition()) && facesSameDirection(angle)) {
	    return RETURNING.nextState();
	}

	return RETURNING;
    }

    private void handleStateAngleCorrection(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double angleBetweenMoveableAndStartPos = calcAngleBetweenMoveableAndStartPos(moveable, positionBeforeEvasion.getDirection().getAngle());
	double angle2Turn = evasionAngleInc / angleIncMultiplier;
	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion, endPosLine);
	if (angleBetweenMoveableAndStartPos < 0) {
	    moveable.makeTurnWithoutPostConditions(Math.max(angleBetweenMoveableAndStartPos, -angle2Turn));
	} else if (angleBetweenMoveableAndStartPos > 0) {
	    moveable.makeTurnWithoutPostConditions(Math.min(angleBetweenMoveableAndStartPos, angle2Turn));
	} else {
	    state = ReturnStates.GETTING_CLOSER;
	}
	prevDistance = currentDistance;
    }
    
    private void handleStateGettingCloser(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double angle2Turn;
	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion, endPosLine);
	if (currentDistance <= prevDistance) {
	    angle2Turn = -evasionAngleInc / angleIncMultiplier;
	} else {
	    angle2Turn = evasionAngleInc / angleIncMultiplier;
	}
	moveable.makeTurnWithoutPostConditions(angle2Turn);
	prevDistance = currentDistance;
    }

    private double calcAngleBetweenMoveableAndStartPos(Moveable moveable, double startPosAngle) {
	double angleDiff = startPosAngle - moveable.getPosition().getDirection().getAngle();
	double stepWidth = 10;
	if (angleDiff > stepWidth) {
	    return angleDiff / stepWidth;
	}
	return angleDiff;
    }

    private void handleFirstAngleCorrection(Position positionBeforeEvasion, Moveable moveable, Float64Vector endPosLine) {
	double angle = calcAngle(moveable, endPosLine);

	double initialDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion, endPosLine);
	makeAngleCorrection(moveable, angle, initialDistance);

	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion, endPosLine);
	makeAngleCorrection(moveable, angle, currentDistance);

	prevDistance = initialDistance;
	state = ReturnStates.ANGLE_CORRECTION;
    }

    private static double calcAngle(Moveable moveable, Float64Vector endPosLine) {
	Float64Vector moveableVector = getVector(moveable.getPosition().getDirection());
	return MathUtil.round(MathUtil.calcAngleBetweenVectors(endPosLine, moveableVector), 10);
    }
    
    private void makeAngleCorrection(Moveable moveable, double angle, double currentDistance) {
	double angle2Turn = calcAngle2Turn(angle, currentDistance);
	moveable.makeTurnWithoutPostConditions(-signum * angle2Turn);
    }
    
    // Angle is between 0 and 90 degrees, we turn left until the moveable is ordinal
    // to the end-Pos-line. From there we turn right until the angle is 0 by turning
    // right
    private double calcAngle2Turn(double currentAngle, double currentDistance) {
	double actualDiff = 90.0d - currentAngle;
	return Math.min(actualDiff, evasionAngleInc * angleIncMultiplier);
    }

    /*
     * Defines if the moveable faces into the same direection then the
     * 'end-Position'-line
     * 
     * Mathematically this is defined by the angle between the line created by 'the
     * point bevore evasion' and 'the final end-position' and the line defined by
     * the direction of the moveable
     */
    private boolean facesSameDirection(double angle) {
	return angleMargin >= angle && angle >= 0.0d;
    }

    /*
     * Verifies if the Movebale is on the line created by 'the point bevore evasion'
     * and 'the final end-position'
     * 
     */
    private boolean isMoveableOnEndPosDirection(Float64Vector endPosDirectionVector, Position positionBeforeEvasion, Position moveablePos) {
	double distance = MathUtil.calcDistanceFromPositionToLine(moveablePos, positionBeforeEvasion, endPosDirectionVector);
	return (int) distance <= distanceMargin;
    }

    private Float64Vector getEndPosLine(Position positionBeforeEvasion) {
	Float64Vector posBeforEvasionVector = getVector(positionBeforeEvasion);
	Float64Vector endPosVector = getVector(endPos);

	return endPosVector.minus(posBeforEvasionVector);
    }

    public final void setSignum(int signum) {
        this.signum = signum;
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
	ANGLE_CORRECTION,

	/**
	 * After the angle of the {@link Moveable} is corrected we need to get closer to
	 * the line created until the target position and the position the
	 * {@link Moveable} detected the evasion the last time the angle of the
	 * {@link Moveable} is corrected
	 */
	GETTING_CLOSER;
    }
}