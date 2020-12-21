package com.myownb3.piranha.core.moveables.engine.human;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.MovingDirections;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio;
import com.myownb3.piranha.core.moveables.engine.human.HumanMoveableEngine.HumanMoveableEngineBuilder;

class HumanMoveableEngineTest {

   @Test
   void testOnForward_DontMove() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.SLOWINGDOWN_NATURALLY);
      when(engineStateHandler.getCurrentVelocity()).thenReturn(50);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanMoveableEngine.onBackwardPressed(false);
      humanMoveableEngine.moveForward();

      // Then
      verify(humanMoveableEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnBackward_EngineIsMovingForward() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.SLOWINGDOWN_NATURALLY);
      when(engineStateHandler.isEngineMovingForward()).thenReturn(true);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanMoveableEngine.onBackwardPressed(true);
      MovingDirections newMovingDirection = humanMoveableEngine.evalNewMovingDirection();

      // Then
      assertThat(newMovingDirection, is(MovingDirections.NONE));
      verify(humanMoveableEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnForward_EngineIsMovingBackward() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.SLOWINGDOWN_NATURALLY);
      when(engineStateHandler.isEngineMovingBackward()).thenReturn(true);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanMoveableEngine.onForwardPressed(true);
      MovingDirections newMovingDirection = humanMoveableEngine.evalNewMovingDirection();

      // Then
      assertThat(newMovingDirection, is(MovingDirections.NONE));
      verify(humanMoveableEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testStopMoveForward() {

      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withEngineAudio(engineAudio)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanMoveableEngine.stopMoveForward();

      // Then
      verify(engineAudio).playEngineAudio(eq(EngineStates.IDLE));
   }

   @Test
   void testOnForward_WithAudio() {
      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withEngineAudio(engineAudio)
            .withVelocity(5)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanMoveableEngine.onForwardPressed(true);
      humanMoveableEngine.moveForward();

      // Then
      verify(engineAudio).playEngineAudio(any());
   }

   @Test
   void testOnForward_ButDontMove_StopedPressingForward() {
      // Given
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanMoveableEngine.onForwardPressed(false);
      humanMoveableEngine.moveForward();

      // Then
      verify(humanMoveableEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnForward_ButDontMove() {
      // Given
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanMoveableEngine.onForwardPressed(true);

      // Then
      verify(humanMoveableEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testWithCustomEngineStateHandler() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.NONE);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanMoveableEngine.onBackwardPressed(true);
      humanMoveableEngine.moveForward();

      // Then
      verify(engineStateHandler).handleEngineState(any(), any());
   }

   @Test
   void testOnBackward_ButDontMove_StopedPressingForward() {
      // Given
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withDefaultEngineStateHandler()
            .withVelocity(180)
            .build();

      // When
      humanMoveableEngine.onBackwardPressed(false);
      humanMoveableEngine.moveForward();

      // Then
      verify(humanMoveableEngine.getMoveable(), never()).moveBackward();
   }

   @Test
   void testOnTurnRight() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanMoveableEngine.onTurnRightPressed(true);
      humanMoveableEngine.moveForward();

      // Then
      verify(humanMoveableEngine.getMoveable()).makeTurn(eq(humanMoveableEngine.turnAngle));
   }

   @Test
   void testOnTurnLeft() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanMoveableEngine humanMoveableEngine = HumanMoveableEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanMoveableEngine.onTurnLeftPressed(true);
      humanMoveableEngine.moveForward();

      // Then
      verify(humanMoveableEngine.getMoveable()).makeTurn(eq(-humanMoveableEngine.turnAngle));
   }

}
