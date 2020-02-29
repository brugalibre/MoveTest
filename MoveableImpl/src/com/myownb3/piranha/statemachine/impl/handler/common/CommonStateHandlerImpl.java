package com.myownb3.piranha.statemachine.impl.handler.common;

import static com.myownb3.piranha.grid.gridelement.Positions.movePositionForward;
import static com.myownb3.piranha.util.MathUtil.calcDistanceFromPositionToLine;
import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.direction.Direction;
import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.common.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public abstract class CommonStateHandlerImpl<T extends CommonEventStateInput, R extends CommonEventStateResult>
	implements EvasionStatesHandler<T, R> {

    private static final int ANGLE_PRECISION = 5;

    @Override
    public void init() {
	// Nothing to do
    }

    protected EvasionStates evalNextState(CommonEventStateInput evenStateInput, EvasionStates nextState) {
	DetectableMoveableHelper helper = evenStateInput.getHelper();
	// First re-check the surrounding. Maybe because of the correction of an
	// EvasionStateHandler that situation may have changed. After verify if there is
	// a evasion
	helper.checkSurrounding(evenStateInput.getGrid(), evenStateInput.getMoveable());
	boolean hasEvasion = helper.check4Evasion(evenStateInput.getGrid(), evenStateInput.getMoveable());
	return hasEvasion ? EvasionStates.EVASION : nextState;
    }

    protected CommonEventStateResult evalNextStateAndBuildResult(CommonEventStateInput evenStateInput,
	    EvasionStates prevState, EvasionStates nextState) {
	EvasionStates evaluatedNextState = evalNextState(evenStateInput, nextState);
	return CommonEventStateResult.of(prevState, evaluatedNextState, evenStateInput.getMoveablePosBefore());
    }

    protected Float64Vector getEndPosLine(Direction posBeforeEvasionDirection, Position endPos) {
	Float64Vector posBeforEvasionVector = getVector(posBeforeEvasionDirection);
	return getEndPosLineInternal(endPos, posBeforEvasionVector);
    }

    protected Float64Vector getEndPosLine(Position posBeforeEvasion, Position endPos) {
	Float64Vector posBeforEvasionVector = getVector(posBeforeEvasion);
	return getEndPosLineInternal(endPos, posBeforEvasionVector);
    }

    private Float64Vector getEndPosLineInternal(Position endPos, Float64Vector posBeforEvasionVector) {
	Float64Vector endPosVector = getVector(endPos);
	return endPosVector.minus(posBeforEvasionVector);
    }

    protected int calcSignumWithDistance(Position moveablePos, Position positionBeforeEvasion, Float64Vector endPosLine,
	    double testTurnAngle) {
	// Rotate negative and calculate angle
	CalcSignumRes res = calSignumRes(moveablePos, positionBeforeEvasion, endPosLine, testTurnAngle);

	// The angle after a turn with a positive number brings us closer to the
	// end-position-line -> positive signum
	return res.distanceAfterTurnNegative > res.distanceAfterTurnPositive ? 1 : -1;
    }

    protected int calcSignumWithAngle(Position moveablePos, Position positionBeforeEvasion, Float64Vector endPosLine,
	    double testTurnAngle) {
	// Rotate negative and calculate angle
	CalcSignumRes res = calSignumRes(moveablePos, positionBeforeEvasion, endPosLine, testTurnAngle);

	// The angle after a turn with a positive number brings us closer to the
	// end-position-line -> positive signum
	return res.angleAfterTurnNegative > res.angleAfterTurnPositive ? 1 : -1;
    }

    private CalcSignumRes calSignumRes(Position moveablePos, Position positionBeforeEvasion, Float64Vector endPosLine,
	    double testTurnAngle) {
	CalcSignumRes res = new CalcSignumRes();
	Position moveablePosTurnNegative = Positions.of(moveablePos);
	moveablePosTurnNegative.rotate(-testTurnAngle);
	moveablePosTurnNegative = movePositionForward(moveablePosTurnNegative);
	res.distanceAfterTurnNegative = calcDistanceFromPositionToLine(moveablePosTurnNegative, positionBeforeEvasion,
		endPosLine);
	res.angleAfterTurnNegative = calcAngle(moveablePosTurnNegative, endPosLine);

	// Rotate positive and calculate angle
	Position moveablePosTurnPositive = Positions.of(moveablePos);
	moveablePosTurnPositive.rotate(testTurnAngle);
	moveablePosTurnPositive = movePositionForward(moveablePosTurnPositive);
	res.distanceAfterTurnPositive = calcDistanceFromPositionToLine(moveablePosTurnPositive, positionBeforeEvasion,
		endPosLine);
	res.angleAfterTurnPositive = calcAngle(moveablePosTurnPositive, endPosLine);
	return res;
    }

    private static class CalcSignumRes{
	private double distanceAfterTurnNegative;
	private double angleAfterTurnNegative;  
	private double distanceAfterTurnPositive;
	private double angleAfterTurnPositive;  
    }

    protected double calcAngle(Position moveablePos, Float64Vector endPosLine) {
	Float64Vector moveableVector = getVector(moveablePos.getDirection());
	return MathUtil.round(MathUtil.calcAngleBetweenVectors(endPosLine, moveableVector), ANGLE_PRECISION);
    }
}