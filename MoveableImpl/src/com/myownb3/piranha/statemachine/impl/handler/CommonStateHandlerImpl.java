package com.myownb3.piranha.statemachine.impl.handler;

import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.CommonEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

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
}