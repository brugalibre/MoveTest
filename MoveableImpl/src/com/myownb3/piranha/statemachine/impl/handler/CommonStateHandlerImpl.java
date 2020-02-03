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
	boolean hasEvasion = helper.check4Evasion(evenStateInput.getGrid(), evenStateInput.getMoveable());
	return hasEvasion ? EvasionStates.RE_INIT: nextState;
    }
    

    protected CommonEventStateResult evalNextStateAndBuildResult(CommonEventStateInput evenStateInput, EvasionStates nextState) {
	EvasionStates evaluatedNextState = evalNextState(evenStateInput, nextState);
	return CommonEventStateResult.of(evaluatedNextState);
    }
}