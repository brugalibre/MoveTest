package com.myownb3.piranha.core.moveables.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigImpl;

class EndPointMoveableImplTest {

   @Test
   void testMoveBackwards() {
      // Given
      Object expectedPosition = Positions.of(0, 0.7);
      int movingIncrement = 2;
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withStartPosition(Positions.of(0, 0.9))
            .withMovingIncrement(movingIncrement)
            .withMoveablePostActionHandler((g, a) -> {
            })
            .build();

      // When
      moveable.moveBackward();

      // Then
      assertThat(moveable.getPosition(), is(expectedPosition));
   }

   @Test
   void testIsNotDone() {
      EndPosition endPos = EndPositions.of(0, 10);
      Position pos = Positions.of(0, 0.9);
      Grid grid = GridBuilder.builder()
            .build();
      Detector detector = mock(Detector.class);
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

      // Given
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(pos)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(detector)
                  .withEndPosition(endPos)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .build();
      moveable.setEndPosition(endPos);

      // When
      MoveResult moveResult = moveable.moveForward2EndPos();

      // Then
      assertThat(moveResult.isDone(), is(false));
   }

   @Test
   void testIsDone() {
      EndPosition endPos = EndPositions.of(0, 1);
      Position pos = Positions.of(0, 0.9);
      Grid grid = GridBuilder.builder()
            .build();
      Detector detector = mock(Detector.class);
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

      // Given
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(pos)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(detector)
                  .withEndPosition(endPos)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .build();
      moveable.setEndPosition(endPos);

      // When
      MoveResult moveResult = moveable.moveForward2EndPos();

      // Then
      assertThat(moveResult.isDone(), is(true));
   }

   @Test
   void testIsDone_BecauseAlreadyToFar() {
      EndPosition endPos = EndPositions.of(0, 1);
      Position pos = Positions.of(0, 0.9);
      Position posAfterMoveForward = EndPositions.of(0, 1.01);
      Grid grid = spy(GridBuilder.builder()
            .build());
      Detector detector = mock(Detector.class);
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

      // Given
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(pos)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(detector)
                  .withEndPosition(endPos)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .build();
      moveable.setEndPosition(endPos);
      doReturn(posAfterMoveForward).when(grid).moveForward(eq(moveable));

      // When
      MoveResult moveResult = moveable.moveForward2EndPos();

      // Then
      assertThat(moveResult.isDone(), is(true));
   }

   @Test
   void testIsDone_BecauseAlreadyToFarWithRotation() {
      // Given
      EndPosition endPos = EndPositions.of(-1, -1);
      Position pos = Positions.of(-0.9, -0.9);
      Position posAfterMoveForward = EndPositions.of(-1.01, -1.01).rotate(135);
      Grid grid = spy(GridBuilder.builder()
            .build());
      Detector detector = mock(Detector.class);
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(pos)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(detector)
                  .withEndPosition(endPos)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .build();
      doReturn(posAfterMoveForward).when(grid).moveForward(eq(moveable));
      moveable.setEndPosition(endPos);

      // When
      MoveResult moveResult = moveable.moveForward2EndPos();

      // Then
      assertThat(moveResult.isDone(), is(true));
   }

   @Test
   void testIsNotDone_BecauseStartPosWasAfterEndPosWithRotation() {
      EndPosition endPos = EndPositions.of(-1, -1);
      Position pos = Positions.of(-1.1, -1.1).rotate(135);
      Grid grid = GridBuilder.builder()
            .build();
      Detector detector = mock(Detector.class);
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

      // Given
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(pos)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(detector)
                  .withEndPosition(endPos)
                  .withEvasionStateMachineConfig(config)
                  .build())
            .withMovingIncrement(4)
            .build();
      moveable.setEndPosition(endPos);

      // When
      MoveResult moveResult = moveable.moveForward2EndPos();

      // Then
      assertThat(moveResult.isDone(), is(false));
   }

}
