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
    private double returningAngle;
    private double distanceMargin;
    private double angleMargin;

    private ReturnStates state;
    private double initialDistance;
    private int signum;

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
	this.returningAngle = evasionAngleInc * angleIncMultiplier;
	this.distanceMargin = distanceMargin;
	this.angleMargin = angleMargin;
	init();
    }

    @Override
    public void init() {
	super.init();
	state = ReturnStates.ENTER_RETURNING;
	this.initialDistance = 0;
	this.signum = 0;
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
	    state = ReturnStates.ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL;
	    break;
	case ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL:
	    doCorrectionPhase1(moveable, positionBeforeEvasion, endPosLine);
	    evalNextState4StateUntilOrdonal(positionBeforeEvasion, moveable, endPosLine);
	    break;
	case ANGLE_CORRECTION_PHASE_FROM_ORDONAL:
	    doCorrectionPhase2(moveable, positionBeforeEvasion, endPosLine);
	    evalNextState4StateFromOrdonal(positionBeforeEvasion, moveable, endPosLine);
	    break;
	default:
	    break;
	}
    }

    private void evalNextState4StateFromOrdonal(Position positionBeforeEvasion, Moveable moveable,
	    Float64Vector endPosLine) {
	if (facesSameDirection(moveable, endPosLine)) {
	    state = ReturnStates.NONE;
	}
    }

    private void evalNextState4StateUntilOrdonal(Position positionBeforeEvasion, Moveable moveable,
	    Float64Vector endPosLine) {
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	if (currentDistance <= (initialDistance / 2) || currentAngle >= 45.0d) {
	    state = ReturnStates.ANGLE_CORRECTION_PHASE_FROM_ORDONAL;
	}
    }

    private void doCorrectionPhase1(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	double actualDiff = 90.0d - currentAngle;
	    double angle2Turn = Math.min(actualDiff, getAngle2Turn());
	moveable.makeTurnWithoutPostConditions(angle2Turn);
    }

    private void doCorrectionPhase2(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	if (currentAngle >= 45.0d  && currentDistance > (initialDistance / 2)) {
	    return;
	}
	double actualDiff = 0 - currentAngle;
	double angle2Turn = Math.max(actualDiff, -getAngle2Turn());
	moveable.makeTurnWithoutPostConditions(angle2Turn);
	
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
	signum = calcSignum(moveable, endPosLine, currentAngle);
	if (facesSameDirection(moveable, endPosLine)) {
	    return;
	}
	double angle2Turn = getAngle2Turn();
	moveable.makeTurnWithoutPostConditions(angle2Turn);
    }

    private double getAngle2Turn() {
	return signum * returningAngle / 1;
    }

//    private void doAngleCorrection(Moveable moveable, Position positionBeforeEvasion, Float64Vector endPosLine) {
//	double currentDistance = MathUtil.calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
//		endPosLine);
//	double currentAngle = calcAngle(moveable.getPosition(), endPosLine);
//	double angle2Turn;
//	boolean wasOrdinal = currentDistance <= (initialDistance / 2) || currentAngle >= 90.0d;
//	if (!wasOrdinal) {
//	    double actualDiff = 90.0d - currentAngle;
//	    angle2Turn = Math.min(actualDiff, getAngle2Turn());
//	} else {
//	    double actualDiff = 0 - currentAngle;
//	    angle2Turn = Math.max(actualDiff, -getAngle2Turn());
//	}
//	moveable.makeTurnWithoutPostConditions(angle2Turn);
//    }

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

	// Rotate negativ and calc diff
	Position moveablePosTurnNegativ = Positions.of(moveable.getPosition());
	moveablePosTurnNegativ.rotate(-returningAngle);
	double angleAfterTurnNegativ = calcAngle(moveablePosTurnNegativ, endPosLine);

	// Rotate positiv and calc diff
	Position moveablePosTurnPositiv = Positions.of(moveable.getPosition());
	moveablePosTurnPositiv.rotate(returningAngle);
	double angleAfterTurnPositiv = calcAngle(moveablePosTurnPositiv, endPosLine);

	// Wenn wir uns nach links (minus) drehen, wird der winkel grösser -> dann
	// lieber nach rechts drehen
	if (angleAfterTurnNegativ >= angleAfterTurnPositiv) {
	    return 1;
	} else /* if (angleAfterTurnNegativ < currentAngle) */ {
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
	ANGLE_CORRECTION_PHASE_UNTIL_ORDONAL,
	
	ANGLE_CORRECTION_PHASE_FROM_ORDONAL,
	
	NONE,
    }
}