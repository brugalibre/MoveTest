package com.myownb3.piranha.statemachine.impl.handler;

import static com.myownb3.piranha.statemachine.states.EvasionStates.NONE;
import static com.myownb3.piranha.statemachine.states.EvasionStates.PASSING;
import static com.myownb3.piranha.statemachine.states.EvasionStates.POST_EVASION;
import static com.myownb3.piranha.statemachine.states.EvasionStates.RETURNING;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.myownb3.piranha.moveables.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.EvasionStatesHandler;
import com.myownb3.piranha.statemachine.impl.handler.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

public class ReturningStateHandler implements EvasionStatesHandler<ReturningEventStateInput> {

    private Map<EvasionStates, Integer> recursivCheck;

    public ReturningStateHandler() {
	createAndInitMap();
    }
    
    private void createAndInitMap() {
 	recursivCheck = new HashMap<>();
 	recursivCheck.put(RETURNING, 0);
 	recursivCheck.put(POST_EVASION, 0);
 	recursivCheck.put(PASSING, 0);
     }
    
    @Override
    public CommonEventStateResult handle(ReturningEventStateInput evenStateInput) {
	return handleReturning(evenStateInput.getExecutors());
    }

    private CommonEventStateResult handleReturning(List<MoveableExecutor> reverseExecutors) {
	if (!isMethodCallAllowed(RETURNING)) {
	    return CommonEventStateResult.of(NONE);
	}
	return handleReturningInternal(reverseExecutors);
    }

    private CommonEventStateResult handleReturningInternal(List<MoveableExecutor> reverseExecutors) {
	registerForRecursivCall(RETURNING);
	for (MoveableExecutor moveableExecutor : reverseExecutors) {
	    moveableExecutor.execute();
	}
	reverseExecutors.clear();
	deregisterForRecursivCall(RETURNING);
	return CommonEventStateResult.of(RETURNING.nextState());
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
}