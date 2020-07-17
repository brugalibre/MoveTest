/**
 * 
 */
package com.myownb3.piranha.core.statemachine.impl.handler.returningstate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;

/**
 * @author Dominic
 *
 */
class ReturningStateHandlerTest {

   @Test
   void testHandle_UnknownState() {

      // Given
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);
      ReturningStateHandlerImpl handler = new ReturningStateHandlerImpl(config);
      ReturningEventStateInput evenStateInput = mockInput();
      handler.state = ReturnStates.NONE;

      // When
      Executable ex = () -> {
         handler.handle(evenStateInput);
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   private ReturningEventStateInput mockInput() {
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
      return ReturningEventStateInput.of(mock(DetectableMoveableHelper.class), moveable, Positions.of(0, 0),
            EndPositions.of(50, 50));
   }

}
