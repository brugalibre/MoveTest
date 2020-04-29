/**
 * 
 */
package com.myownb3.piranha.statemachine.impl.handler.postevasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.detector.Detector;
import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.grid.gridelement.position.Positions;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class DefaultPostEvasionStateHandlerTest {

   @Test
   public void testHandlePostEvasion_AngleCorrectionNotNecessary() {

      // Given
      Position positionBeforeEvasion = Positions.of(9, 9);
      Position moveablePos = Positions.of(10, 10);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withStepWidth(10)
            .withPositionBeforeEvasion(positionBeforeEvasion)
            .withHelper(new NeverEvasionDetectableMoveableHelper())
            .withMoveable(moveablePos)
            .withEventStateInput()
            .withPostEvasionStateHandler();

      // When
      CommonEvasionStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION.nextState()));
      verify(tcb.moveable, never()).makeTurnWithoutPostConditions(anyDouble());
   }

   @Test
   public void testHandlePostEvasion_AngleCorrectionNecessary() {

      // Given
      Position positionBeforeEvasion = Positions.of(9, 9);
      Position moveablePos = Positions.of(11, 10);
      moveablePos.rotate(-10);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withStepWidth(10)
            .withPositionBeforeEvasion(positionBeforeEvasion)
            .withHelper(new NeverEvasionDetectableMoveableHelper())
            .withMoveable(moveablePos)
            .withEventStateInput()
            .withPostEvasionStateHandler();

      // When
      CommonEvasionStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
      verify(tcb.moveable).makeTurnWithoutPostConditions(anyDouble());
   }

   @Test
   public void testHandlePostEvasion_AngleCorrectionNecessaryWithEvasion() {

      // Given
      DetectableMoveableHelper helper = new OneTimeEvasionDetectableMoveableHelper();
      Position positionBeforeEvasion = Positions.of(9, 9);
      Position moveablePos = Positions.of(11, 10);
      moveablePos.rotate(-25);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withStepWidth(10)
            .withPositionBeforeEvasion(positionBeforeEvasion)
            .withHelper(helper)
            .withMoveable(moveablePos)
            .withEventStateInput()
            .withPostEvasionStateHandler();

      // When
      CommonEvasionStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
      verify(tcb.moveable, times(2)).makeTurnWithoutPostConditions(anyDouble());
   }

   private static class TestCaseBuilder {

      private PostEvasionEventStateInput evenStateInput;
      private double stepWidth;
      private Position positionBeforeEvasion;
      private DetectableMoveableHelper helper;
      private Grid grid;
      private Moveable moveable;
      private DefaultPostEvasionStateHandler handler;

      public TestCaseBuilder() {
         helper = mock(DetectableMoveableHelper.class);
         grid = mock(Grid.class);
      }

      private TestCaseBuilder withHelper(DetectableMoveableHelper helper) {
         this.helper = helper;
         return this;
      }

      private TestCaseBuilder withStepWidth(double stepWidth) {
         this.stepWidth = stepWidth;
         return this;
      }

      private TestCaseBuilder withPositionBeforeEvasion(Position positionBeforeEvasion) {
         this.positionBeforeEvasion = positionBeforeEvasion;
         return this;
      }

      private TestCaseBuilder withEventStateInput() {
         Objects.requireNonNull(helper, "We need a helper");
         Objects.requireNonNull(moveable, "We need a moveable");
         Objects.requireNonNull(grid, "We need a grid");
         Objects.requireNonNull(positionBeforeEvasion, "We need a positionBeforeEvasion");
         evenStateInput = PostEvasionEventStateInput.of(helper, grid, moveable, positionBeforeEvasion);
         return this;
      }

      private TestCaseBuilder withMoveable(Position moveablePos) {
         moveable = spy(Moveable.class);
         Mockito.when(moveable.getPosition()).thenReturn(moveablePos);
         return this;
      }

      private TestCaseBuilder withPostEvasionStateHandler() {
         handler = new DefaultPostEvasionStateHandler(stepWidth);
         return this;
      }
   }

   private static class NeverEvasionDetectableMoveableHelper extends DetectableMoveableHelper {

      public NeverEvasionDetectableMoveableHelper() {
         super(mock(Detector.class));
      }

      @Override
      public boolean check4Evasion(Grid grid, GridElement moveable) {
         return false;
      }
   }

   private static class OneTimeEvasionDetectableMoveableHelper extends DetectableMoveableHelper {

      private boolean isCheck4EvasionTrue;

      public OneTimeEvasionDetectableMoveableHelper() {
         super(mock(Detector.class));
         isCheck4EvasionTrue = true;
      }

      @Override
      public boolean check4Evasion(Grid grid, GridElement moveable) {
         boolean isCheck4EvasionTmp = isCheck4EvasionTrue;
         isCheck4EvasionTrue = !isCheck4EvasionTrue;
         return isCheck4EvasionTmp;
      }
   }
}
