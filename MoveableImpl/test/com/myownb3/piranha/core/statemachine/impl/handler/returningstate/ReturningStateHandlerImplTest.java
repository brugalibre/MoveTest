package com.myownb3.piranha.core.statemachine.impl.handler.returningstate;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.MoveableBuilder;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.handler.returningstate.ReturningStateHandler;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.handler.returningstate.input.ReturningEventStateInput;

class ReturningStateHandlerImplTest {

   @Test
   void testHandle_HandleReturningNotFacesSameDirection() {

      // Given
      Position position = Positions.of(5, 5);
      EndPosition endPosition = EndPositions.of(5, 5);
      Position positionBeforeEvasion = Positions.of(4, 4);
      double evasionAngleInc = 2;
      double returningAngleIncMultiplier = -3;
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGrid(mock(Grid.class))
            .withMoveable(position)
            .withEndPosition(endPosition)
            .withPositionBeforeEvasion(positionBeforeEvasion)
            .withEvasionAngleInc(evasionAngleInc)
            .withReturningAngleIncMultiplier(returningAngleIncMultiplier)
            .build();

      // When
      ReturningEventStateInput evenStateInput =
            ReturningEventStateInput.of(tcb.helper, tcb.grid, tcb.moveable, tcb.positionBeforeEvasion, tcb.endPosition);
      tcb.returningStateHandler.handle(evenStateInput);

      // Then
      verify(tcb.moveable).makeTurnWithoutPostConditions(eq(evasionAngleInc * returningAngleIncMultiplier));
   }

   private static class TestCaseBuilder {
      private DetectableMoveableHelper helper;
      private Grid grid;
      private Moveable moveable;
      private Position positionBeforeEvasion;
      private ReturningStateHandler returningStateHandler;
      private EndPosition endPosition;
      private double evasionAngleInc;
      private double returningAngleIncMultiplier;

      private TestCaseBuilder() {
         // private
      }

      public TestCaseBuilder withEndPosition(EndPosition endPosition) {
         this.endPosition = endPosition;
         return this;
      }

      public TestCaseBuilder withReturningAngleIncMultiplier(double returningAngleIncMultiplier) {
         this.returningAngleIncMultiplier = returningAngleIncMultiplier;
         return this;
      }

      public TestCaseBuilder withEvasionAngleInc(double evasionAngleInc) {
         this.evasionAngleInc = evasionAngleInc;
         return this;
      }

      public TestCaseBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TestCaseBuilder withPositionBeforeEvasion(Position positionBeforeEvasion) {
         this.positionBeforeEvasion = positionBeforeEvasion;
         return this;
      }

      public TestCaseBuilder withMoveable(Position pos) {
         moveable = spy(MoveableBuilder.builder()
               .withGrid(grid)
               .withPosition(pos)
               .withShape(CircleBuilder.builder()
                     .withRadius(4)
                     .withAmountOfPoints(4)
                     .withCenter(pos)
                     .build())
               .build());
         return this;
      }

      public TestCaseBuilder build() {
         helper = new DetectableMoveableHelper(mock(Detector.class));
         returningStateHandler = new ReturningStateHandlerImpl(EvasionStateMachineConfigBuilder.builder()
               .withDetectorConfig(DetectorConfigBuilder.builder()
                     .withEvasionAngleInc(evasionAngleInc)
                     .build())
               .withReturningAngleIncMultiplier(returningAngleIncMultiplier)
               .build());
         return this;
      }
   }
}
