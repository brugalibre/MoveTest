package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;
import static java.util.Objects.isNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public class ReturningStateHandler extends CommonStateHandlerImpl<ReturningEventStateInput> {

    private Position endPos;
    private int angleIncMultiplier;
    private double returningAngle;
    private double distanceMargin;
    private double angleMargin;

    private ReturnStates state;
    private double initialDistance;

    /**
     * Creates a new {@link ReturningStateHandler} with the given
     * end-{@link Position}
     * 
     * @param endPos             the final end position
     * @param angleIncMultiplier the multiplier used to calculate the angle for
     *                           correction maneuver
     * @param distanceMargin     the minimal distance to the vector which shows the
     *                           direction to the end-point (a {@link Moveable} has
     *                           to reach
     * @param angleMargin        margin between the actual angle from the
     *                           {@link Moveable} to it's position before the
     *                           evasion
     * @param evasionAngleInc    the angle used to turn the {@link Moveable} back to it's origin rout
     */
    public ReturningStateHandler(Position endPos, int angleIncMultiplier, double distanceMargin, double angleMargin,
	    double evasionAngleInc) {
	super();
	this.endPos = endPos;
	this.angleIncMultiplier = angleIncMultiplier;
	this.returningAngle = evasionAngleInc;
	this.distanceMargin = distanceMargin;
	this.angleMargin = angleMargin;
	init();
    }

    @Override
    public void init() {
	super.init();
	state = ReturnStates.ENTER_RETURNING;
	this.initialDistance = 0;
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
	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion);
	handleReturning(positionBeforeEvasion, moveable, endPosLine);
	return evalNextState(positionBeforeEvasion, moveable, endPosLine);
    }

    private void handleReturning(Position positionBeforeEvasion, Moveable moveable, Float64Vector endPosLine) {
	switch (state) {
	case ENTER_RETURNING:
	    handleFirstAngleCorrection(positionBeforeEvasion, moveable, endPosLine);
	    state = ReturnStates.ANGLE_TO_START_POS_CORRECTION;
	    break;
	case ANGLE_TO_START_POS_CORRECTION:
	    doAngleCorrection(moveable, positionBeforeEvasion, endPosLine);
	    break;
	default:
	    break;
	}
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
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	this.initialDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	if (facesSameDirection(moveable, endPosLine)) {
	    return;
	}
	int signum = calcSignum(moveable, endPosLine, currentAngle);
	double angle2Turn = -signum * returningAngle * angleIncMultiplier;
	moveable.makeTurnWithoutPostConditions(angle2Turn);
    }

    private void doAngleCorrection(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	// to the end-Pos-line. From there we turn right until the angle is 0 by turning
	// right
	double angle2Turn;
	int signum = calcSignum(moveable, endPosLine, currentAngle);
	boolean wasOrdinal = currentDistance <= (initialDistance / 2) || currentAngle >= 90.0d;
	if (!wasOrdinal) {
	    double actualDiff = 90.0d - currentAngle;
	    angle2Turn = Math.min(actualDiff, -signum * returningAngle * angleIncMultiplier);
	} else {
	    double actualDiff = 0 - currentAngle;
	    angle2Turn = Math.max(actualDiff, signum * returningAngle * angleIncMultiplier);
	}
	moveable.makeTurnWithoutPostConditions(angle2Turn);
    }

    private static double calcAngle(Position moveablePos, Float64Vector endPosLine) {
	Float64Vector moveableVector = getVector(moveablePos.getDirection());
	return MathUtil.round(MathUtil.calcAngleBetweenVectors(endPosLine, moveableVector), 10);
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

    private Float64Vector getEndPosLine(Position positionBeforeEvasion) {
	Float64Vector posBeforEvasionVector = getVector(positionBeforeEvasion);
	Float64Vector endPosVector = getVector(endPos);

	return endPosVector.minus(posBeforEvasionVector);
    }

    private int calcSignum(Moveable moveable, Float64Vector endPosLine, double currentAngle) {

	Position moveablePos = Positions.of(moveable.getPosition());
	double returninAngle = returningAngle * angleIncMultiplier;

	// Rotate negativ and calc diff
	moveablePos.rotate(-returninAngle);
	double angleAfterTurnNegativ = calcAngle(moveablePos, endPosLine);
	double diffNegativ = MathUtil.round(Math.abs(currentAngle - angleAfterTurnNegativ), 10);

	// Rotate positiv and calc diff
	moveablePos = Positions.of(moveable.getPosition());
	moveablePos.rotate(returninAngle);
	double angleAfterTurnPositiv = calcAngle(moveablePos, endPosLine);
	double diffPositiv = MathUtil.round(Math.abs(currentAngle - angleAfterTurnPositiv), 10);

	// Wenn wir uns nach links (minus) drehen, wird der winkel grösser -> dann
	// lieber nach rechts drehen
	if (angleAfterTurnNegativ > angleAfterTurnPositiv && diffNegativ > diffPositiv) {
	    return 1;
	} else /* if (angleAfterTurnNegativ <= currentAngle) */ {
	    return -1;
	}
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
	ANGLE_TO_START_POS_CORRECTION,
    }
}