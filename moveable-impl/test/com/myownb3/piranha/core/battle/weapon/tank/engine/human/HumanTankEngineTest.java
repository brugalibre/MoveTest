package com.myownb3.piranha.core.battle.weapon.tank.engine.human;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine.HumanTankEngineBuilder;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.EngineStates;
import com.myownb3.piranha.core.moveables.engine.MovingDirections;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio;

class HumanTankEngineTest {

   @Test
   void testOnForward_DontMove() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.SLOWINGDOWN_NATURALLY);
      when(engineStateHandler.getCurrentVelocity()).thenReturn(50);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanTankEngine.onBackwardPressed(false);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnBackward_EngineIsMovingForward() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.SLOWINGDOWN_NATURALLY);
      when(engineStateHandler.isEngineMovingForward()).thenReturn(true);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanTankEngine.onBackwardPressed(true);
      MovingDirections newMovingDirection = humanTankEngine.evalNewMovingDirection();

      // Then
      assertThat(newMovingDirection, is(MovingDirections.NONE));
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnForward_EngineIsMovingBackward() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.SLOWINGDOWN_NATURALLY);
      when(engineStateHandler.isEngineMovingBackward()).thenReturn(true);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanTankEngine.onForwardPressed(true);
      MovingDirections newMovingDirection = humanTankEngine.evalNewMovingDirection();

      // Then
      assertThat(newMovingDirection, is(MovingDirections.NONE));
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testStopMoveForward() {

      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withEngineAudio(engineAudio)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanTankEngine.stopMoveForward();

      // Then
      verify(engineAudio).playEngineAudio(eq(EngineStates.IDLE));
   }

   @Test
   void testOnForward_WithAudio() {
      // Given
      EngineAudio engineAudio = mock(EngineAudio.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withEngineAudio(engineAudio)
            .withVelocity(5)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanTankEngine.onForwardPressed(true);
      humanTankEngine.moveForward();

      // Then
      verify(engineAudio).playEngineAudio(any());
   }

   @Test
   void testOnForward_ButDontMove_StopedPressingForward() {
      // Given
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanTankEngine.onForwardPressed(false);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnForward_ButDontMove() {
      // Given
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanTankEngine.onForwardPressed(true);

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testWithCustomEngineStateHandler() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      EngineStateHandler engineStateHandler = mock(EngineStateHandler.class);
      when(engineStateHandler.handleEngineState(any(), any())).thenReturn(EngineStates.NONE);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withEngineStateHandler(engineStateHandler)
            .build();

      // When
      humanTankEngine.onBackwardPressed(true);
      humanTankEngine.moveForward();

      // Then
      verify(engineStateHandler).handleEngineState(any(), any());
   }

   @Test
   void testOnBackward_ButDontMove_StopedPressingForward() {
      // Given
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .withDefaultEngineStateHandler()
            .withVelocity(180)
            .build();

      // When
      humanTankEngine.onBackwardPressed(false);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveBackward();
   }

   @Test
   void testOnTurnRight() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanTankEngine.onTurnRightPressed(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).makeTurn(eq(humanTankEngine.turnAngle));
   }

   @Test
   void testOnTurnLeft() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .withVelocity(180)
            .withDefaultEngineStateHandler()
            .build();

      // When
      humanTankEngine.onTurnLeftPressed(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).makeTurn(eq(-humanTankEngine.turnAngle));
   }

}
