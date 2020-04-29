package com.myownb3.piranha.statemachine.impl.handler.evasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.MoveableBuilder;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.OneTimeDetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.statemachine.impl.handler.evasionstate.output.EvasionStateResult;
import com.myownb3.piranha.statemachine.states.EvasionStates;

class EvasionStateHandlerTest {
   @Test
   void testHandleEvasionState_DelayPostEvasion() {

      // Given
      Grid grid = mock(Grid.class);
      double expectedAngle = 10;
      int postEvasionDelayDistance = 2;
      Detector detector = mockDetector(expectedAngle, postEvasionDelayDistance);
      Moveable moveable = spyMoveable();

      DetectableMoveableHelper helper = spy(new OneTimeDetectableMoveableHelper(detector));

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper, Positions.of(0, 0));
      EvasionStateHandler test = new EvasionStateHandler(postEvasionDelayDistance);

      // When
      EvasionStateResult evasionStateResult = test.handle(evenStateInput);

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION));
      assertThat(evasionStateResult.isEvasion(), is(false));
   }

   @Test
   void testHandleEvasionState_NonEvasionAtAll() {

      // Given
      int postEvasionDelayDistance = 0;
      Grid grid = mock(Grid.class);
      double expectedAngle = 0.0d;
      Detector detector = mockDetector(expectedAngle, postEvasionDelayDistance);
      Moveable moveable = MoveableBuilder.builder(grid).withHandler((a, b) -> {
      }).build();

      DetectableMoveableHelper helper = new DummyDetectableMoveableHelper(detector);

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper, Positions.of(0, 0));
      EvasionStateHandler test = new EvasionStateHandler(postEvasionDelayDistance);

      // When
      EvasionStateResult evasionStateResult = test.handle(evenStateInput);

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION.nextState()));
      assertThat(evasionStateResult.isEvasion(), is(false));
   }

   @Test
   void testHandleEvasionState_NotEvasionAnymoreAfterFirstTurn() {

      // Given
      Grid grid = mock(Grid.class);
      double expectedAngle = 10;
      int postEvasionDelayDistance = 0;
      Detector detector = mockDetector(expectedAngle, postEvasionDelayDistance);
      Moveable moveable = spyMoveable();

      DetectableMoveableHelper helper = spy(new OneTimeDetectableMoveableHelper(detector));

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper, Positions.of(0, 0));
      EvasionStateHandler test = new EvasionStateHandler(postEvasionDelayDistance);

      // When
      EvasionStateResult evasionStateResult = test.handle(evenStateInput);

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION.nextState()));
      verify(moveable).makeTurnWithoutPostConditions(Mockito.anyDouble());
      verify(helper).checkSurrounding(eq(grid), eq(moveable));
      verify(helper, times(2)).check4Evasion(eq(grid), eq(moveable));
      assertThat(evasionStateResult.isEvasion(), is(false));
   }

   @Test
   void testHandleEvasionState_StillEvasionAfterFirstTurn() {

      // Given
      Grid grid = mock(Grid.class);
      double expectedAngle = 10;
      int postEvasionDelayDistance = 0;
      Detector detector = mockDetector(expectedAngle, postEvasionDelayDistance);
      Moveable moveable = spyMoveable();

      DetectableMoveableHelper helper = spy(new AlwaysEvasionDetectableMoveableHelper(detector));

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(grid, moveable, detector, helper, Positions.of(0, 0));
      EvasionStateHandler test = new EvasionStateHandler(postEvasionDelayDistance);

      // When
      EvasionStateResult evasionStateResult = test.handle(evenStateInput);

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION));
      verify(moveable).makeTurnWithoutPostConditions(Mockito.anyDouble());
      verify(helper).checkSurrounding(eq(grid), eq(moveable));
      verify(helper, times(2)).check4Evasion(eq(grid), eq(moveable));
      assertThat(evasionStateResult.isEvasion(), is(true));
   }

   private static Detector mockDetector(Double expectedAngle, int postEvasionDelayDistance) {
      Detector detector = mock(Detector.class);
      when(detector.getEvasionAngleRelative2(any())).thenReturn(expectedAngle);
      when(detector.getEvasionDelayDistance()).thenReturn(postEvasionDelayDistance);
      return detector;
   }

   private Moveable spyMoveable() {
      Moveable moveable = spy(Moveable.class);
      Mockito.when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
      return moveable;
   }

   private static class AlwaysEvasionDetectableMoveableHelper extends DetectableMoveableHelper {
      public AlwaysEvasionDetectableMoveableHelper(Detector detector) {
         super(detector);
      }

      @Override
      public boolean check4Evasion(Grid grid, GridElement moveable) {
         return true;
      }

      @Override
      public void checkSurrounding(Grid grid, Moveable moveable) {}
   }

   private static class DummyDetectableMoveableHelper extends DetectableMoveableHelper {
      public DummyDetectableMoveableHelper(Detector detector) {
         super(detector);
      }

      @Override
      public boolean check4Evasion(Grid grid, GridElement moveable) {
         return false;
      }

      @Override
      public void checkSurrounding(Grid grid, Moveable moveable) {}
   }
}
