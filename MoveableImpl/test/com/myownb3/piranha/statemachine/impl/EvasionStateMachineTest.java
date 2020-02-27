/**
 * 
 */
package com.myownb3.piranha.statemachine.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.detector.DetectorImpl;
import com.myownb3.piranha.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class EvasionStateMachineTest {

    @Test
    void testHandleEvasionState_NoRegisteredStateHandler() {

	// Given
	EvasionStateMachineConfig config = mock(EvasionStateMachineConfig.class);
	Detector detector = mock(Detector.class);
	EvasionStateMachine evasionStateMachine = new EvasionStateMachine(detector, config);

	// When
	Executable ex = () -> {
	    evasionStateMachine.getHandler4State(EvasionStates.NONE);
	};

	// Then
	assertThrows(IllegalStateException.class, ex);
    }

    /**
     * Test method for
     * {@link com.myownb3.piranha.statemachine.impl.handler.com.myownb3.piranha.moveables.statemachine.impl.EvasionStateMachine#handleEvasion4CurrentState(com.myownb3.piranha.grid.Grid, com.myownb3.piranha.moveables.Moveable)}.
     */
    @Test
    void test_HandleEvasion4UnknownState() {

	// Given
	Grid grid = GridBuilder.builder()//
		.build();
	Moveable moveable = MoveableBuilder.builder(grid)//
		.build();

	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(4, 0.05, 0.7d, 8, 8, 45, 11.25);
	EvasionStateMachine evasionStateMachine = new EvasionStateMachine(new DetectorImpl(), config);
	evasionStateMachine.evasionState = EvasionStates.NONE;

	// When
	Executable ex = () -> {
	    evasionStateMachine.handlePostConditions(grid, moveable);
	};

	// Then
	assumeThat(EvasionStates.NONE.nextState(), is(EvasionStates.NONE));
	assertThrows(IllegalStateException.class, ex);
    }
}
