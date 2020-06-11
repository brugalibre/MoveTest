package com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

class PostEvasionStateHandler4MazeTest {

   @Test
   void testReturninStateHandler_ReturnsNextState() {

      // Given
      PostEvasionStateHandler4Maze returingStateHandler4Maze = new PostEvasionStateHandler4Maze();

      // When
      CommonEvasionStateResult evasionStateResult = returingStateHandler4Maze.handle(buildEmptyInput());

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.POST_EVASION.nextState()));
   }

   private static PostEvasionEventStateInput buildEmptyInput() {
      return PostEvasionEventStateInput.of(mock(DetectableMoveableHelper.class), mockMoveable(), Positions.of(0, 0));
   }

   private static Moveable mockMoveable() {
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPosition()).thenReturn(Positions.of(0, 0));
      return moveable;
   }
}
