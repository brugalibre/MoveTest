package com.myownb3.piranha.application.battle.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.application.battle.impl.MultiMoveableAdderImpl.MultiMovealbeAdderBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.statemachine.EvasionStateMachineConfig;

class MultiMoveableAdderImplTest {

   @Test
   void testIncrementCounter() {

      // Given
      MoveableAdderImpl moveableAdder1 = mock(MoveableAdderImpl.class);
      MoveableAdderImpl moveableAdder2 = mock(MoveableAdderImpl.class);
      MultiMoveableAdderImpl moveableAdder = MultiMovealbeAdderBuilder.builder()
            .addMoveableAdder(moveableAdder1)
            .setAsync()
            .addMoveableAdder(moveableAdder2)
            .build();

      // When
      moveableAdder.incrementCounter();

      // Then
      verify(moveableAdder1).incrementCounter();
      verify(moveableAdder2).incrementCounter();
   }

   @Test
   void testIsCycleDone() {
      // Given
      MoveableAdderImpl moveableAdder1 = mockMoveableAdder(false);
      MoveableAdderImpl moveableAdder2 = mockMoveableAdder(true);
      MultiMoveableAdderImpl moveableAdder = MultiMovealbeAdderBuilder.builder()
            .addMoveableAdder(moveableAdder1)
            .addMoveableAdder(moveableAdder2)
            .build();

      // When
      boolean actualIsCycleDone = moveableAdder.isCycleDone();

      // Then
      assertThat(actualIsCycleDone, is(true));
      verify(moveableAdder1).isCycleDone();
      verify(moveableAdder2).isCycleDone();
   }

   private MoveableAdderImpl mockMoveableAdder(Boolean isCycleDone) {
      MoveableAdderImpl moveableAdder1 = mock(MoveableAdderImpl.class);
      when(moveableAdder1.isCycleDone()).thenReturn(isCycleDone);
      return moveableAdder1;
   }

   @Test
   void testCheck4NewMoveables2Add() {
      // Given
      Grid grid = mock(Grid.class);
      EvasionStateMachineConfig evasionStateMachineConfig = mock(EvasionStateMachineConfig.class);

      MoveableAdderImpl moveableAdder1 = mockMoveableAdder(true);
      MoveableAdderImpl moveableAdder2 = mockMoveableAdder(true);
      MultiMoveableAdderImpl moveableAdder = MultiMovealbeAdderBuilder.builder()
            .addMoveableAdder(moveableAdder1)
            .addMoveableAdder(moveableAdder2)
            .setAsync()
            .build();

      // When
      moveableAdder.check4NewMoveables2Add(grid, evasionStateMachineConfig);

      // Then
      verify(moveableAdder1).check4NewMoveables2Add(eq(grid), eq(evasionStateMachineConfig));
      verify(moveableAdder2).check4NewMoveables2Add(eq(grid), eq(evasionStateMachineConfig));
   }

}
