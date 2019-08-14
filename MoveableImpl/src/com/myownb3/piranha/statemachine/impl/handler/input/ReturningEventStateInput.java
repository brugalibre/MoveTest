package com.myownb3.piranha.statemachine.impl.handler.input;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.moveables.MoveableExecutor;
import com.myownb3.piranha.statemachine.handler.input.EventStateInput;

public class ReturningEventStateInput implements EventStateInput {

    private List<MoveableExecutor> executors;

    private ReturningEventStateInput(List<MoveableExecutor> executors) {
	this.executors = requireNonNull(executors);
    }

    /**
     * Creates a new {@link ReturningEventStateInput}
     * 
     * @param executors
     *            the previously registered List with {@link MoveableExecutor}
     * @return a new {@link ReturningEventStateInput}
     */
    public static ReturningEventStateInput of(List<MoveableExecutor> executors) {
	return new ReturningEventStateInput(executors);
    }

    public List<MoveableExecutor> getExecutors() {
	return executors;
    }
}
