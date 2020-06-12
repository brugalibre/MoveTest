package com.myownb3.piranha.core.statemachine.impl.handler.evasionstate;

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

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.OneTimeDetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.input.EvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.impl.handler.evasionstate.output.EvasionStateResult;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

class EvasionStateHandlerTest {
   @Test
   void testHandleEvasionState_DelayPostEvasion() {

      // Given
      Grid grid = mock(Grid.class);
      double expectedAngle = 10;
      int postEvasionDelayDistance = 2;
      Detector detector = mockDetector(expectedAngle, postEvasionDelayDistance);
      Moveable moveable = spyMoveable();

      DetectableMoveableHelper helper = spy(new OneTimeDetectableMoveableHelper(grid, detector));

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(moveable, detector, helper, Positions.of(0, 0));
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
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withPosition(Positions.of(0, 0))
            .withHandler((b) -> {
            }).build();

      DetectableMoveableHelper helper = new DummyDetectableMoveableHelper(grid, detector);

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(moveable, detector, helper, Positions.of(0, 0));
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

      DetectableMoveableHelper helper = spy(new OneTimeDetectableMoveableHelper(grid, detector));

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(moveable, detector, helper, Positions.of(0, 0));
      EvasionStateHandler test = new EvasionStateHandler(postEvasionDelayDistance);

      // When
      EvasionStateResult evasionStateResult = test.handle(evenStateInput);

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION.nextState()));
      verify(moveable).makeTurnWithoutPostConditions(Mockito.anyDouble());
      verify(helper).checkSurrounding(eq(moveable));
      verify(helper, times(2)).check4Evasion(eq(moveable));
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

      DetectableMoveableHelper helper = spy(new AlwaysEvasionDetectableMoveableHelper(grid, detector));

      EvasionEventStateInput evenStateInput = EvasionEventStateInput.of(moveable, detector, helper, Positions.of(0, 0));
      EvasionStateHandler test = new EvasionStateHandler(postEvasionDelayDistance);

      // When
      EvasionStateResult evasionStateResult = test.handle(evenStateInput);

      // Then
      assertThat(evasionStateResult.getNextState(), is(EvasionStates.EVASION));
      verify(moveable).makeTurnWithoutPostConditions(Mockito.anyDouble());
      verify(helper).checkSurrounding(eq(moveable));
      verify(helper, times(2)).check4Evasion(eq(moveable));
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
      public AlwaysEvasionDetectableMoveableHelper(Grid grid, Detector detector) {
         super(grid, detector);
      }

      @Override
      public boolean check4Evasion(GridElement gridElement) {
         return true;
      }

      @Override
      public void checkSurrounding(GridElement gridElement) {}
   }

   private static class DummyDetectableMoveableHelper extends DetectableMoveableHelper {
      public DummyDetectableMoveableHelper(Grid grid, Detector detector) {
         super(grid, detector);
      }

      @Override
      public boolean check4Evasion(GridElement gridElement) {
         return false;
      }

      @Override
      public void checkSurrounding(GridElement gridElement) {}
   }
}
