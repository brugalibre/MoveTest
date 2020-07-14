package com.myownb3.piranha.core.moveables.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.StroomTrooper;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.belligerent.rebelalliance.Rebel;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigImpl;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;

class EndPointMoveableImplTest {

   @Test
   void testCombineMoveablePostActionHandler() {
      // Given

      AtomicInteger firstCalled = new AtomicInteger(-10);
      AtomicInteger secondCalled = new AtomicInteger(-10);
      MoveablePostActionHandler firstHandler = moveable -> {
         firstCalled.set(1);
         return true;
      };
      MoveablePostActionHandler secondHandler = moveable -> {
         secondCalled.set(1);
         return true;
      };
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0.9))
                  .build())
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withMovingIncrement(1)
            .addMoveablePostActionHandler(firstHandler)
            .addMoveablePostActionHandler(secondHandler)
            .build();
      moveable.setEndPosition(EndPositions.of(5, 5));

      // When
      moveable.moveForward2EndPos();

      // Then
      assertThat(firstCalled.get(), is(1));
      assertThat(secondCalled.get(), is(1));
   }

   @Test
   void testIsEnemy() {
      // Given
      boolean expectedIsAimable = true;
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0.9))
                  .build())
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withMovingIncrement(1)
            .build();

      // When
      boolean actualIsEnemy = moveable.isEnemy(new StroomTrooper());

      // Then
      assertThat(actualIsEnemy, is(expectedIsAimable));
   }

   @Test
   void testIsNotEnemy() {
      // Given
      boolean expectedIsAimable = false;
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0.9))
                  .build())
            .withMovingIncrement(1)
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .build();

      // When
      boolean actualIsEnemy = moveable.isEnemy(new Rebel());

      // Then
      assertThat(actualIsEnemy, is(expectedIsAimable));
      assertThat(moveable.getBelligerentParty(), is(BelligerentPartyConst.REBEL_ALLIANCE));
   }

   @Test
   void testMoveBackwards() {
      // Given
      Object expectedPosition = Positions.of(0, 0.7);
      int movingIncrement = 2;
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(0, 0.9))
                  .build())
            .withMovingIncrement(movingIncrement)
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
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
   void testIsDoneBecauseToClose() {
      EndPosition endPos = EndPositions.of(0, 1.001);
      Position pos = Positions.of(0, 1);
      Grid grid = GridBuilder.builder()
            .build();
      Detector detector = mock(Detector.class);
      EvasionStateMachineConfig config = new EvasionStateMachineConfigImpl(0, 0, 0, 0, 0, 0, 0);

      // Given
      EndPointMoveable moveable = EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
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
            .withShape(PositionShapeBuilder.builder()
                  .withPosition(pos)
                  .build())
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
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
