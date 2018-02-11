/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.DefaultGrid;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.moveables.AbstractMoveable.MoveableBuilder;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.DetectorImpl;

/**
 * @author Dominic
 *
 */
class EvasionStateMachineTest {

    /**
     * Test method for
     * {@link com.myownb3.piranha.moveables.helper.EvasionStateMachine#handleEvasion4CurrentState(com.myownb3.piranha.grid.Grid, com.myownb3.piranha.moveables.Moveable)}.
     */
    @Test
    void test_HandleEvasion4UnknownState() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	EvasionStateMachine evasionStateMachine = new EvasionStateMachine(new DetectorImpl());
	evasionStateMachine.evasionState = EvasionStates.NONE;

	// When
	Executable ex = () -> {
	    evasionStateMachine.handleEvasion4CurrentState(grid, moveable);
	};

	// Then
	assertThrows(IllegalStateException.class, ex);
    }
}
