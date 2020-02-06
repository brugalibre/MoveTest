package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
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
    private double evasionAngleInc;
    private double distanceMargin;
    private double angleMargin;

    private ReturnStates state;

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
     * @param evasionAngleInc    the angle used to turn the {@link Moveable}
     */
    public ReturningStateHandler(Position endPos, int angleIncMultiplier, double distanceMargin, double angleMargin,
	    double evasionAngleInc) {
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
	state = ReturnStates.ENTER_RETURNING;
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
//	    handleFirstAngleCorrection(positionBeforeEvasion, moveable, endPosLine);
//	    break;
	case ANGLE_TO_START_POS_CORRECTION:
//	    handleStateAngleCorrection(moveable, positionBeforeEvasion, endPosLine);
//	    break;
	case ANGLE_TO_END_POS_LINE_CORRECTION:
	    double calcAngleBetweenPositions = MathUtil.calcAngleBetweenPositions(positionBeforeEvasion, moveable.getPosition());
	    if (calcAngleBetweenPositions == 0.0) {
		break;
	    }
	    double signum = calcSignum(moveable, endPosLine);
	    moveable.makeTurnWithoutPostConditions(-signum * calcAngleBetweenPositions);
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

    private void handleStateAngleCorrection(Moveable moveable, Position positionBeforeEvasion,
	    Float64Vector endPosLine) {
	
	double calcAngleRelativeTo = positionBeforeEvasion.calcAngleRelativeTo(moveable.getPosition());
	double angle2Turn = evasionAngleInc / angleIncMultiplier;
	if (calcAngleRelativeTo < 0) {
	    moveable.makeTurnWithoutPostConditions(Math.max(calcAngleRelativeTo, angle2Turn));
	} else if (calcAngleRelativeTo > 0) {
	    moveable.makeTurnWithoutPostConditions(Math.min(calcAngleRelativeTo, -angle2Turn));
	} else {
	    state = ReturnStates.ANGLE_TO_END_POS_LINE_CORRECTION;
	}
    }

    private static double calcAngleBetweenMoveableAndStartPos(Moveable moveable, double startPosAngle) {
	double angleDiff = startPosAngle - moveable.getPosition().getDirection().getAngle();
	double stepWidth = 10;
	if (angleDiff > stepWidth) {
	    return angleDiff / stepWidth;
	}
	return angleDiff;
    }

    private void handleFirstAngleCorrection(Position positionBeforeEvasion, Moveable moveable,
	    Float64Vector endPosLine) {
	double angle = calcAngle(moveable.getPosition(), endPosLine);
	double initialDistance = calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	int signum = calcSignum(moveable, endPosLine);
	makeAngleCorrection(moveable, angle, initialDistance, -signum);

	double currentDistance = calcDistanceFromPositionToLine(moveable.getPosition(), positionBeforeEvasion,
		endPosLine);
	makeAngleCorrection(moveable, angle, currentDistance, -signum);

	state = ReturnStates.ANGLE_TO_END_POS_LINE_CORRECTION;
    }

    private static double calcAngle(Position moveablePos, Float64Vector endPosLine) {
	Float64Vector moveableVector = getVector(moveablePos.getDirection());
	return MathUtil.round(MathUtil.calcAngleBetweenVectors(endPosLine, moveableVector), 10);
    }

    private void makeAngleCorrection(Moveable moveable, double angle, double currentDistance, int signum) {
	double angle2Turn = calcAngle2Turn(angle, currentDistance);
	moveable.makeTurnWithoutPostConditions(signum * angle2Turn);
    }

    private double calcAngle2Turn(double currentAngle, double currentDistance) {
	double actualDiff = 90.0d - currentAngle;
	return Math.min(actualDiff, evasionAngleInc * angleIncMultiplier);
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
	return (int) distance <= distanceMargin;
    }

    private Float64Vector getEndPosLine(Position positionBeforeEvasion) {
	Float64Vector posBeforEvasionVector = getVector(positionBeforeEvasion);
	Float64Vector endPosVector = getVector(endPos);

	return endPosVector.minus(posBeforEvasionVector);
    }

    private int calcSignum(Moveable moveable, Float64Vector endPosLine) {

	Position moveablePos = Positions.of(moveable.getPosition());
	double currentAngle = calcAngle(moveablePos, endPosLine);
	
	moveablePos.rotate(-5);
	double angleAfterTurnNegativ = calcAngle(moveablePos, endPosLine);
	
	double diffNegativ = Math.abs(currentAngle-angleAfterTurnNegativ);
	
	moveablePos = Positions.of(moveable.getPosition());
	moveablePos.rotate(5);
	double angleAfterTurnPositiv = calcAngle(moveablePos, endPosLine);
	double diffPositiv= Math.abs(currentAngle-angleAfterTurnPositiv);

	// Wenn wir uns nach links (minus) drehen, wird der winkel grösser -> dann lieber nach rechts drehen
	if (angleAfterTurnNegativ > angleAfterTurnPositiv 
		&& diffNegativ > diffPositiv) {
	    return 1;
	}
	else /* if (angleAfterTurnNegativ <= currentAngle) */{
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

	/**
	 * After the second time we enter the {@link EvasionStates#RETURNING} and until
	 * the angle of the {@link Moveable} is corrected
	 */
	ANGLE_TO_END_POS_LINE_CORRECTION,
    }
}