package com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.AbstractMoveableBuilder.MoveableBuilder;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.postaction.impl.DetectableMoveableHelper;
import com.myownb3.piranha.core.statemachine.impl.handler.common.output.CommonEvasionStateResult;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.input.OrientatingStateInput;
import com.myownb3.piranha.core.statemachine.states.EvasionStates;

class OrientatingStateHandlerTest {

   @Test
   void testHandle_NoCorrectionWithNecessary() {
      // Given
      Grid grid = mock(Grid.class);
      OrientatingStateHandler handler = new OrientatingStateHandler(90);
      EndPosition endPos = EndPositions.of(0, 0);
      Position moveablePos = spy(Positions.of(0, 0));
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(moveablePos)
                  .build())
            .build();

      OrientatingStateInput input =
            OrientatingStateInput.of(moveable, mock(DetectableMoveableHelper.class), endPos);

      // When
      CommonEvasionStateResult stateResult = handler.handle(input);

      // Then
      assertThat(stateResult.getNextState(), is(EvasionStates.DEFAULT));
      verify(moveablePos).calcAngleRelativeTo(eq(endPos));// 1 Times: 1. verification
   }

   @Test
   void testHandle_CorrectionWithOneTry() {
      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(50)
            .withMaxY(50)
            .build();
      OrientatingStateHandler handler = new OrientatingStateHandler(90);
      EndPosition endPos = EndPositions.of(0, 0);
      Position moveablePos = spy(Positions.of(Directions.O, 0, 0, 0));
      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(moveablePos)
                  .build())
            .build();

      OrientatingStateInput input =
            OrientatingStateInput.of(moveable, mock(DetectableMoveableHelper.class), endPos);

      // When
      CommonEvasionStateResult stateResult = handler.handle(input);

      // Then
      assertThat(stateResult.getNextState(), is(EvasionStates.DEFAULT));
      verify(moveablePos, times(2)).calcAngleRelativeTo(eq(endPos));// 3 Times: 1. verification, 2. actual turn, 3. last verification
   }

   @Test
   void testHandle_CorrectionWithTwoTry() {
      // Given
      Grid grid = GridBuilder.builder()
            .withMaxX(50)
            .withMaxY(50)
            .build();
      OrientatingStateHandler handler = new OrientatingStateHandler(45);
      EndPosition endPos = EndPositions.of(0, 0);
      Position moveablePos = spy(Positions.of(Directions.O, 0, 0, 0));

      Moveable moveable = MoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(moveablePos)
                  .build())
            .build();

      OrientatingStateInput input =
            OrientatingStateInput.of(moveable, mock(DetectableMoveableHelper.class), endPos);

      // When
      CommonEvasionStateResult stateResult1 = handler.handle(input);
      CommonEvasionStateResult stateResult2 = handler.handle(input);

      // Then
      assertThat(stateResult1.getNextState(), is(EvasionStates.ORIENTING));
      assertThat(stateResult2.getNextState(), is(EvasionStates.DEFAULT));
      verify(moveablePos, times(2)).calcAngleRelativeTo(eq(endPos));
   }

   @Test
   void testHandle_NoEndPosion() {
      // Given
      OrientatingStateHandler handler = new OrientatingStateHandler(10);
      EndPosition endPos = null;
      Position moveablePos = spy(Positions.of(0, 0));
      OrientatingStateInput input =
            OrientatingStateInput.of(mockMoveable(moveablePos), mock(DetectableMoveableHelper.class), endPos);

      // When
      CommonEvasionStateResult stateResult = handler.handle(input);

      // Then
      assertThat(stateResult.getNextState(), is(EvasionStates.DEFAULT));
      verify(moveablePos, never()).calcAngleRelativeTo(any());
   }

   private Moveable mockMoveable(Position position) {
      Moveable moveable = mock(Moveable.class);
      when(moveable.getPosition()).thenReturn(position);
      return moveable;
   }

}
