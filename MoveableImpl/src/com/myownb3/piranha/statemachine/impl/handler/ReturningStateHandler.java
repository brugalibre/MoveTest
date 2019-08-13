package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;

import java.util.List;
import java.util.Map;

import com.myownb3.piranha.moveables.statemachine.impl.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.handler.output.EvenStateResult;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class ReturningStateHandler implements EvasionStatesHandler<ReturningEventStateInput> {

    private Map<EvasionStates, Integer> recursivCheck;
    @Override
    public EvenStateResult handle(ReturningEventStateInput evenStateInput) {
	handleReturning(evenStateInput.getExecutors());
	return null;
    }

    private void handleReturning(List<MoveableExecutor> reverseExecutors) {
	if (!isMethodCallAllowed(RETURNING)) {
	    return;
	}
	handleReturningInternal(reverseExecutors);
    }

    private void handleReturningInternal(List<MoveableExecutor> reverseExecutors) {
	registerForRecursivCall(RETURNING);
	for (MoveableExecutor moveableExecutor : reverseExecutors) {
	    moveableExecutor.execute();
	}
	reverseExecutors.clear();
	deregisterForRecursivCall(RETURNING);
//	evasionState = EvasionStates.DEFAULT;
    }

    private void registerForRecursivCall(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	recursivCheck.put(state, counter + 1);
    }

    private void deregisterForRecursivCall(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	recursivCheck.put(state, counter - 1);
    }

    private boolean isMethodCallAllowed(EvasionStates state) {
	Integer counter = recursivCheck.get(state);
	return counter.intValue() < 1;
    }  
    
    @Override
    public EvasionStates getNextState() {
	return RETURNING.nextState();
    }
}