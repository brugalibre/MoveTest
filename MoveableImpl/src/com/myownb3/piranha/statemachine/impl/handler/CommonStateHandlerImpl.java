package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.util.vector.VectorUtil.getVector;

import org.jscience.mathematics.vector.Float64Vector;

import com.myownb3.piranha.grid.gridelement.Position;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;
import com.myownb3.piranha.util.MathUtil;

public abstract class CommonStateHandlerImpl<T extends CommonEventStateInput> implements EvasionStatesHandler<T> {
   
    @Override
    public void init() {
	// Nothing to do
    }
    
    protected EvasionStates evalNextState(CommonEventStateInput evenStateInput, EvasionStates nextState) {
	DetectableMoveableHelper helper = evenStateInput.getHelper();
	// First re-check the surrounding. Maybe because of the correction of an EvasionStateHandler that situation may have changed. After verify if there is a evasion
	helper.checkSurrounding(evenStateInput.getGrid(), evenStateInput.getMoveable());
	boolean hasEvasion = helper.check4Evasion(evenStateInput.getGrid(), evenStateInput.getMoveable());
	return hasEvasion ? EvasionStates.EVASION: nextState;
    }

    protected CommonEventStateResult evalNextStateAndBuildResult(CommonEventStateInput evenStateInput,
	    EvasionStates prevState, EvasionStates nextState) {
	EvasionStates evaluatedNextState = evalNextState(evenStateInput, nextState);
	return CommonEventStateResult.of(prevState, evaluatedNextState, evenStateInput.getMoveablePosBefore());
    }
    

    protected Float64Vector getEndPosLine(Position positionBeforeEvasion, Position endPos) {
	Float64Vector posBeforEvasionVector = getVector(positionBeforeEvasion);
	Float64Vector endPosVector = getVector(endPos);
	return endPosVector.minus(posBeforEvasionVector);
    }
  
    protected int calcSignum(Position moveablePos, Float64Vector endPosLine, double returningAngle) {
	// Rotate negative and calculate angle
	Position moveablePosTurnNegative = Positions.of(moveablePos);
	moveablePosTurnNegative.rotate(-returningAngle);
	double angleAfterTurnNegative = calcAngle(moveablePosTurnNegative, endPosLine);

	// Rotate positive and calculate angle
	Position moveablePosTurnPositive = Positions.of(moveablePos);
	moveablePosTurnPositive.rotate(returningAngle);
	double angleAfterTurnPositive = calcAngle(moveablePosTurnPositive, endPosLine);

	// The angle after a turn with a positive number brings us closer to the
	// end-position-line -> positive signum
	return angleAfterTurnNegative >= angleAfterTurnPositive ? 1 : -1;
    }

    protected double calcAngle(Position moveablePos, Float64Vector endPosLine) {
	Float64Vector moveableVector = getVector(moveablePos.getDirection());
	return MathUtil.round(MathUtil.calcAngleBetweenVectors(endPosLine, moveableVector), 10);
    }
}