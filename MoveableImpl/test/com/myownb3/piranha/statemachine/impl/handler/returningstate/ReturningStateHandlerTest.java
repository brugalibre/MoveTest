/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler.returningstate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class ReturningStateHandlerTest {

   @Test
   void testHandle_UnknownState() {

      // Given
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);
      ReturningStateHandler handler = new ReturningStateHandler(config);
      ReturningEventStateInput evenStateInput = mockInput(true);
      handler.state = ReturnStates.NONE;

      // When
      Executable ex = () -> {
         handler.handle(evenStateInput);
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   void testHandle_NoReturningNecessary2() {

      // Given
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);
      ReturningStateHandler handler = new ReturningStateHandler(config);
      ReturningEventStateInput evenStateInput = mockInput(false);

      // When
      CommonEvasionStateResult eventStateResult = handler.handle(evenStateInput);

      // Then
      assertThat(eventStateResult.getNextState(), is(EvasionStates.RETURNING.nextState()));
   }

   @Test
   void testHandle_NoReturningNecessary() {

      // Given
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);
      ReturningStateHandler handler = new ReturningStateHandler(config);
      ReturningEventStateInput evenStateInput = mockInput(false);

      // When
      CommonEvasionStateResult eventStateResult = handler.handle(evenStateInput);

      // Then
      assertThat(eventStateResult.getNextState(), is(EvasionStates.RETURNING.nextState()));
   }

   private ReturningEventStateInput mockInput(boolean withEndPos) {
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
      if (withEndPos) {

         return ReturningEventStateInput.of(mock(DetectableMoveableHelper.class), mock(Grid.class), moveable,
               Positions.of(0, 0), mock(EndPosition.class));
      }
      return ReturningEventStateInput.of(mock(DetectableMoveableHelper.class), mock(Grid.class), moveable,
            Positions.of(0, 0));
   }

}
