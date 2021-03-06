/**
 * 
 */
package com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jscience.mathematics.vector.Float64Vector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.postevasionstate.input.PostEvasionEventStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

/**
 * @author Dominic
 *
 */
class PostEvasionStateHandlerWithEndPosTest {

   @Test
   public void testHandlePostEvasion_UnknownState() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPostEvasionAngle(10)
            .withEndPos(Positions.of(10, 10))
            .withPositionBeforeEvasion(Positions.of(9, 9))
            .withEvasionStateHandler()
            .build()
            .withEventStateInput();
      tcb.handler.state = PostEvasionStates.NONE;

      // When
      Executable ex = () -> {
         tcb.handler.handle(tcb.evenStateInput);
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   public void testHandlePostEvasion_AngleCorrectionNotNecessary() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPostEvasionAngle(10)
            .withEndPos(Positions.of(10, 10))
            .withPositionBeforeEvasion(Positions.of(9, 9))
            .withEvasionStateHandler()
            .build()
            .withEventStateInput();

      // When
      CommonEvasionStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION.nextState()));
      verify(tcb.moveable, never()).makeTurnWithoutPostConditions(anyDouble());
      assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
   }

   @Test
   public void testHandlePostEvasion_AngleCorrectionNecessary() {

      // Given
      double mingAngle2Turn = 4.0;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withPostEvasionAngle(10)
            .withEndPos(Positions.of(10, 10))
            .withPositionBeforeEvasion(Positions.of(9, 9))
            .withEvasionStateHandler()
            .withCalculatedAngle(15)
            .withSignum(-1)
            .withMinAngle2Turn(mingAngle2Turn)
            .build()
            .withEventStateInput();

      // When
      CommonEvasionStateResult commonEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(commonEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
      assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
      verify(tcb.moveable).makeTurnWithoutPostConditions(
            Mockito.eq(tcb.handler.testSignum * mingAngle2Turn));
      verify(tcb.helper, times(1)).checkSurrounding(eq(tcb.moveable));
      verify(tcb.helper, times(1)).check4Evasion(eq(tcb.moveable));
   }

   @Test
   public void testHandlePostEvasion_AngleCorrectionNecessary_WithEvasionOnSecondCall_CalcAngleSmallerThanMin() {

      // Given
      int calculatedAngle = 2;
      double expectedAngle = calculatedAngle;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withHelper(spy(new TestDetectableMoveableHelper()))
            .withPostEvasionAngle(10)
            .withPostEvasionAngle(10)
            .withEndPos(Positions.of(10, 10))
            .withPositionBeforeEvasion(Positions.of(9, 9))
            .withEvasionStateHandler()
            .withCalculatedAngle(calculatedAngle)
            .withSignum(-1)
            .withMinAngle2Turn(4)
            .build()
            .withEventStateInput();

      // When
      CommonEvasionStateResult firstCEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // during the second call we're getting an evasion
      ((TestDetectableMoveableHelper) tcb.helper).isCheck4EvasionTrue = true;
      tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(firstCEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
      assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
      verify(tcb.moveable, times(2))
            .makeTurnWithoutPostConditions(Mockito.eq(tcb.handler.testSignum * expectedAngle));
      verify(tcb.helper, times(2)).checkSurrounding(eq(tcb.moveable));
      verify(tcb.helper, times(2)).check4Evasion(eq(tcb.moveable));
   }

   @Test
   public void testHandlePostEvasion_AngleCorrectionNecessary_WithEvasionOnSecondCall() {

      // Given
      double expectedAngle = 4;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withHelper(spy(new TestDetectableMoveableHelper()))
            .withPostEvasionAngle(10)
            .withPostEvasionAngle(10)
            .withEndPos(Positions.of(10, 10))
            .withPositionBeforeEvasion(Positions.of(9, 9))
            .withEvasionStateHandler()
            .withCalculatedAngle(15)
            .withSignum(-1)
            .withMinAngle2Turn(4)
            .build()
            .withEventStateInput();

      // When
      CommonEvasionStateResult firstCEventStateResult = tcb.handler.handle(tcb.evenStateInput);

      // during the second call we're getting an evasion
      ((TestDetectableMoveableHelper) tcb.helper).isCheck4EvasionTrue = true;
      tcb.handler.handle(tcb.evenStateInput);

      // Then
      assertThat(firstCEventStateResult.getNextState(), is(EvasionStates.POST_EVASION));
      assertThat(tcb.handler.state, is(PostEvasionStates.POST_EVASION));
      verify(tcb.moveable, times(2))
            .makeTurnWithoutPostConditions(Mockito.eq(tcb.handler.testSignum * expectedAngle));
      verify(tcb.helper, times(2)).checkSurrounding(eq(tcb.moveable));
      verify(tcb.helper, times(2)).check4Evasion(eq(tcb.moveable));
   }

   private static class TestCaseBuilder {

      private PostEvasionEventStateInput evenStateInput;
      private double postEvasionAngle;
      private Position endPos;
      private Position positionBeforeEvasion;
      private DetectableMoveableHelper helper;
      private Moveable moveable;
      private TestPostEvasionStateHandler handler;

      public TestCaseBuilder() {
         helper = mock(DetectableMoveableHelper.class);
         moveable = spyMoveable();
      }

      private TestCaseBuilder withHelper(DetectableMoveableHelper helper) {
         this.helper = helper;
         return this;
      }

      private TestCaseBuilder withPostEvasionAngle(double postEvasionAngle) {
         this.postEvasionAngle = postEvasionAngle;
         return this;
      }

      private TestCaseBuilder withEndPos(Position endPos) {
         this.endPos = endPos;
         return this;
      }

      private TestCaseBuilder withPositionBeforeEvasion(Position positionBeforeEvasion) {
         this.positionBeforeEvasion = positionBeforeEvasion;
         return this;
      }

      private TestCaseBuilder withEventStateInput() {
         evenStateInput = PostEvasionEventStateInput.of(helper, moveable, positionBeforeEvasion, EndPositions.of(1, 1));
         return this;
      }

      private PostEvasionStateHandlerBuilder withEvasionStateHandler() {
         return new PostEvasionStateHandlerBuilder();
      }

      private Moveable spyMoveable() {
         Moveable moveable = spy(Moveable.class);
         Mockito.when(moveable.getPosition()).thenReturn(Positions.of(1, 1));
         return moveable;
      }

      private class PostEvasionStateHandlerBuilder {

         private TestPostEvasionStateHandler handler;
         private double mingAngle2Turn;
         private int testSignum;
         private double calculatedAngle;

         private PostEvasionStateHandlerBuilder withCalculatedAngle(double calculatedAngle) {
            this.calculatedAngle = calculatedAngle;
            return this;
         }

         private PostEvasionStateHandlerBuilder withSignum(int signum) {
            this.testSignum = signum;
            return this;
         }

         private PostEvasionStateHandlerBuilder withMinAngle2Turn(double mingAngle2Turn) {
            this.mingAngle2Turn = mingAngle2Turn;
            return this;
         }

         private TestCaseBuilder build() {
            handler = new TestPostEvasionStateHandler(endPos, postEvasionAngle, mingAngle2Turn);
            handler.calculatedAngle = calculatedAngle;
            handler.testSignum = testSignum;
            TestCaseBuilder.this.handler = this.handler;
            return TestCaseBuilder.this;
         }
      }
   }

   private static class TestDetectableMoveableHelper extends DetectableMoveableHelper {

      private boolean isCheck4EvasionTrue;

      public TestDetectableMoveableHelper() {
         super(mock(Grid.class), mock(Detector.class));
         isCheck4EvasionTrue = false;
      }

      @Override
      public boolean check4Evasion(GridElement gridElement) {
         return isCheck4EvasionTrue;
      }
   }

   private static class TestPostEvasionStateHandler extends PostEvasionStateHandlerWithEndPos {

      private int testSignum;
      private double calculatedAngle;

      public TestPostEvasionStateHandler(Position endPos, double stepWidth, double mingAngle2Turn) {
         super(mingAngle2Turn);
      }

      @Override
      protected int calcSignumWithDistance(Position moveablePos, Position positionBeforeEvasion,
            Float64Vector endPosLine, double testTurnAngle) {
         return testSignum;
      }

      @Override
      protected double calcAngle(Position moveablePos, Float64Vector endPosLine) {
         return calculatedAngle;
      }
   }
}
