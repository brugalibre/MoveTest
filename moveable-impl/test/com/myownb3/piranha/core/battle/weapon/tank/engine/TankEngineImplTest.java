package com.myownb3.piranha.core.battle.weapon.tank.engine;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.MoveableEngineImpl;
import com.myownb3.piranha.core.moveables.engine.MoveableEngineImpl.MoveableEngineBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio;

class MoveableEngineImplTest {

   @Test
   void testWithCustomEngineStateHandler() {

      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      MoveableEngineImpl moveableEngineImpl = MoveableEngineBuilder.builder()
            .withMoveableController(mock(MoveableController.class))
            .withEngineAudio(engineAudio)
            .withVelocity(10)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      moveableEngineImpl.moveForward();

      // Then
      verify(engineStateHandler).handleEngineState(any(), any());
   }

   @Test
   void testStopMoveForward() {

      MoveableController moveableController = mock(MoveableController.class);

      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      MoveableEngineImpl moveableEngineImpl = MoveableEngineBuilder.builder()
            .withMoveableController(moveableController)
            .withEngineAudio(engineAudio)
            .withVelocity(10)
            .withDefaultEngineStateHandler()
            .build();

      // When
      moveableEngineImpl.stopMoveForward();

      // Then
      verify(moveableController, never()).leadMoveable();
      verify(engineAudio).playEngineAudio(eq(EngineStates.IDLE));
   }

   @Test
   void testMoveForward() {

      MoveableController moveableController = mock(MoveableController.class);

      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      MoveableEngineImpl moveableEngineImpl = MoveableEngineBuilder.builder()
            .withMoveableController(moveableController)
            .withEngineAudio(engineAudio)
            .withVelocity(10)
            .withDefaultEngineStateHandler()
            .build();

      // When
      moveableEngineImpl.moveForward();

      // Then
      verify(moveableController).leadMoveable();
      verify(engineAudio).playEngineAudio(any());
   }

   @Test
   void testGetMoveable() {

      MoveableController moveableController = mock(MoveableController.class);
      EndPointMoveable expectedMoveable = mock(EndPointMoveable.class);
      Mockito.when(moveableController.getMoveable()).thenReturn(expectedMoveable);

      // Given
      MoveableEngineImpl moveableEngineImpl = MoveableEngineBuilder.builder()
            .withMoveableController(moveableController)
            .withDefaultEngineStateHandler()
            .build();

      // When
      Moveable actualMoveable = moveableEngineImpl.getMoveable();

      // Then
      assertThat(actualMoveable, is(expectedMoveable));
   }
}
