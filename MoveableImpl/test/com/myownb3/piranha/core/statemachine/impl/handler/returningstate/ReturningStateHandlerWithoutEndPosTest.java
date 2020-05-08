package com.myownb3.piranha.core.statemachine.impl.handler.returningstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

class ReturningStateHandlerWithoutEndPosTest {


   @Test
   void testHandle_NoReturningNecessary() {

      // Given
      ReturningStateHandlerWithoutEndPos handler = new ReturningStateHandlerWithoutEndPos();
      ReturningEventStateInput evenStateInput = mockInput();

      // When
      CommonEvasionStateResult eventStateResult = handler.handle(evenStateInput);

      // Then
      assertThat(eventStateResult.getNextState(), is(EvasionStates.RETURNING.nextState()));
   }

   private ReturningEventStateInput mockInput() {
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
      return ReturningEventStateInput.of(mock(DetectableMoveableHelper.class), mock(Grid.class), moveable,
            Positions.of(0, 0));
   }
}
