/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler.returningstate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEventStateResult;
import com.myownb3.piranha.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class ReturningStateHandlerTest {

    @Test
    void testHandle_NoReturningNecessary() {

	// Given
	EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);
	ReturningStateHandler handler = new ReturningStateHandler(null, config);
	ReturningEventStateInput evenStateInput = mockInput();

	// When
	CommonEventStateResult eventStateResult = handler.handle(evenStateInput);

	// Then
	assertThat(eventStateResult.getNextState(), is(EvasionStates.RETURNING.nextState()));
    }

    private ReturningEventStateInput mockInput() {
	Moveable moveable = mock(Moveable.class);
	when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
	return ReturningEventStateInput.of(mock(DetectableMoveableHelper.class), mock(Grid.class), moveable, Positions.of(0, 0));
    }

}
