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
import com.myownb3.piranha.moveables.statemachine.states.EvasionStates;
=======
import com.myownb3.piranha.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.statemachine.states.EvasionStates;
>>>>>>> d27c863 Move EventStateMachine to new Package. Introduce new EventHandlers

/**
 * @author Dominic
 *
 */
class EvasionStateMachineTest {

    /**
     * Test method for
<<<<<<< Upstream, based on origin/master
     * {@link com.myownb3.piranha.statemachine.impl.handler.com.myownb3.piranha.moveables.statemachine.impl.EvasionStateMachine#handleEvasion4CurrentState(com.myownb3.piranha.grid.Grid, com.myownb3.piranha.moveables.Moveable)}.
=======
     * {@link com.myownb3.piranha.statemachine.impl.handler.com.myownb3.piranha.moveables.statemachine.impl.EvasionStateMachine#handleEvasion4CurrentState(com.myownb3.piranha.grid.Grid, com.myownb3.piranha.moveables.Moveable)}.
>>>>>>> d27c863 Move EventStateMachine to new Package. Introduce new EventHandlers
     */
    @Test
    void test_HandleEvasion4UnknownState() {

	// Given
	Grid grid = new DefaultGrid();
	Moveable moveable = new MoveableBuilder(grid)//
		.build();

	EvasionStateMachine evasionStateMachine = new EvasionStateMachine(new DetectorImpl());
	evasionStateMachine.data.evasionState = EvasionStates.NONE;
	

	// When
	Executable ex = () -> {
	    evasionStateMachine.handlePostConditions(grid, moveable);
	};

	// Then
	assertThrows(IllegalStateException.class, ex);
    }
}
