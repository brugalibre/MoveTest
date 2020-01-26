package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.grid.vector.VectorUtil.getVector;
import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;
import static java.util.Objects.requireNonNull;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.util.MathUtil;

public class ReturningStateHandler implements EvasionStatesHandler<ReturningEventStateInput> {

    /**
     * 
     */
    private static final int ANGLE_INC_MULTIPLIER = 4;
    /**
     * 
     */
    private static final double MIN_DISTANCE = 0.032d;
    private Position endPos;
    private boolean wasOrdinal;	// as soon as the moveable has a 90deg angle to the 'returning' vector this flag is set to true

    /**
     * Creates a new {@link ReturningStateHandler} with the given
     * end-{@link Position}
     * 
     * @param endPos
     *            the final end position
     */
    public ReturningStateHandler(Position endPos) {
	super();
	this.endPos = endPos;
	this.wasOrdinal = false;
    }

    @Override
    public CommonEventStateResult handle(ReturningEventStateInput evenStateInput) {
	requireEndPosNonNull();
	return handleReturning(evenStateInput.getDetector(), evenStateInput.getPositionBeforeEvasion(), evenStateInput.getMoveable());
    }

    private CommonEventStateResult handleReturning(Detector detector, Position positionBeforeEvasion, Moveable moveable) {

	Float64Vector endPosLine = getEndPosLine(positionBeforeEvasion);

	boolean isMoveableOnEndPosDirection = isMoveableOnEndPosDirection(endPosLine, positionBeforeEvasion, moveable.getPosition());
	double angle = calcAngle(moveable, endPosLine);
	if (!facesSameDirection(angle) || isFirstTimeReturning(isMoveableOnEndPosDirection, angle)) {
	    makeDirectionCorretions(detector, moveable, angle);
	}

	setWasOrdinal(moveable, endPosLine);
	// The moveable is on the endPosLine and faces into the right direction
	// -> we are done here
	if (isMoveableOnEndPosDirection(endPosLine, positionBeforeEvasion, moveable.getPosition()) && facesSameDirection(angle)) {
	    return CommonEventStateResult.of(RETURNING.nextState());
	}
	return CommonEventStateResult.of(RETURNING);
    }


    private static double calcAngle(Moveable moveable, Float64Vector endPosLine) {
	Float64Vector moveableVector = getVector(moveable.getPosition().getDirection());
	return MathUtil.calcAngleBetweenVectors(endPosLine, moveableVector);
    }

    private void makeDirectionCorretions(Detector detector, Moveable moveable, double currentAngle) {
	// Angle is between 0 and 90 degrees, we turn left until the moveable is ordinal
	// to the end-Pos-line. From there we turn right until the angle is 0 by turning
	// right
	double angle2Turn;
	if (!wasOrdinal) {
	    double actualDiff = 90.0d - currentAngle;
	    angle2Turn = Math.min(actualDiff, detector.getAngleInc() * ANGLE_INC_MULTIPLIER);
	} else {
	    double actualDiff = 0 - currentAngle;
	    angle2Turn = Math.max(actualDiff, -detector.getAngleInc() * ANGLE_INC_MULTIPLIER);
	}
	moveable.makeTurnWithoutPostConditions(angle2Turn);
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
	return angle == 0.0d;
    }

    /*
     * Verifies if the Movebale is on the line created by 'the point bevore evasion'
     * and 'the final end-position'
     * 
     */
    private boolean isMoveableOnEndPosDirection(Float64Vector endPosDirectionVector, Position positionBeforeEvasion, Position movebalePos) {
	double distance = MathUtil.calcDistanceFromPositionToLine(movebalePos, positionBeforeEvasion, endPosDirectionVector);
	return distance <= MIN_DISTANCE;
    }

    /*
     * The first time we handle the 'returning' state we have already the same angle than the 'endPosLine' but the moveable has not the same position neither is the 'wasOrdinal' flag set to true
     */
    private boolean isFirstTimeReturning(boolean isMoveableOnEndPosDirection, double angle) {
	return !wasOrdinal && angle == 0d && !isMoveableOnEndPosDirection;
    }

    private void setWasOrdinal(Moveable moveable, Float64Vector endPosLine) {
	double angle = calcAngle(moveable, endPosLine);
	wasOrdinal = wasOrdinal || angle == 90.0d;
    }
    
    private Float64Vector getEndPosLine(Position positionBeforeEvasion) {
	Float64Vector posBeforEvasionVector = getVector(positionBeforeEvasion);
	Float64Vector endPosVector = getVector(endPos);

	return endPosVector.minus(posBeforEvasionVector);
    }
    
    private void requireEndPosNonNull() {
	requireNonNull(endPos, "For handling the Evasion-State 'Returning' an End-Position is required!");
    }

}