package com.myownb3.piranha.core.statemachine.impl.handler.passingstate;

import static com.myownb3.piranha.core.statemachine.states.EvasionStates.PASSING;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.OneTimeDetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.passingstate.input.PassingEventStateInput;

class PassingStateHandlerTest {

   @Test
   void testHandle_Distance2Small_PassingStillNecessary() {
      // Given
      int passingDistance = 5;
      Position positionBeforeEvasion = Positions.of(0, 0);
      Moveable moveable = mockMoveable(Positions.of(1, 1));
      DetectableMoveableHelper helper = mockDetectableMoveableHelper(false);
      PassingStateHandler passingStateHandler = new PassingStateHandler(passingDistance);

      PassingEventStateInput evenStateInput = build(positionBeforeEvasion, moveable, helper);

      // When
      CommonEvasionStateResult eventStateResult = passingStateHandler.handle(evenStateInput);

      // Then
      assertThat(eventStateResult.getNextState(), is(PASSING));
   }

   @Test
   void testHandle_DistancePassedButStillEvading_PassingStillNecessary() {
      // Given
      int passingDistance = 5;
      Position positionBeforeEvasion = Positions.of(0, 0);
      Moveable moveable = mockMoveable(Positions.of(10, 10));
      DetectableMoveableHelper helper = new OneTimeDetectableMoveableHelper(null);
      PassingStateHandler passingStateHandler = new PassingStateHandler(passingDistance);

      PassingEventStateInput evenStateInput = build(positionBeforeEvasion, moveable, helper);

      // When
      CommonEvasionStateResult eventStateResult = passingStateHandler.handle(evenStateInput);

      // Then
      assertThat(eventStateResult.getNextState(), is(PASSING));
   }

   @Test
   void testHandle_AllPassed() {
      // Given
      int passingDistance = 5;
      Position positionBeforeEvasion = Positions.of(0, 0);
      Moveable moveable = mockMoveable(Positions.of(10, 10));
      DetectableMoveableHelper helper = mockDetectableMoveableHelper(false);
      PassingStateHandler passingStateHandler = new PassingStateHandler(passingDistance);

      PassingEventStateInput evenStateInput = build(positionBeforeEvasion, moveable, helper);

      // When
      CommonEvasionStateResult eventStateResult = passingStateHandler.handle(evenStateInput);

      // Then
      assertThat(eventStateResult.getNextState(), is(PASSING.nextState()));
   }

   private Moveable mockMoveable(Position position) {
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPosition()).thenReturn(position);
      return moveable;
   }

   private PassingEventStateInput build(Position positionBeforeEvasion, Moveable moveable, DetectableMoveableHelper helper) {
      return PassingEventStateInput.of(helper, mock(Grid.class), moveable, positionBeforeEvasion);
   }

   private static DetectableMoveableHelper mockDetectableMoveableHelper(boolean isEvasion) {
      DetectableMoveableHelper helper = mock(DetectableMoveableHelper.class);
      when(helper.check4Evasion(any(), any())).thenReturn(isEvasion);
      return helper;
   }

}
