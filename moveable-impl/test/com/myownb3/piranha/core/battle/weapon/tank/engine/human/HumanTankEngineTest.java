package com.myownb3.piranha.core.battle.weapon.tank.engine.human;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine.HumanTankEngineBuilder;
import com.myownb3.piranha.core.moveables.EndPointMoveable;

class HumanTankEngineTest {

   @Test
   void testOnForward_ButDontMove_StopedPressingForward() {
      // Given
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .build();

      // When
      humanTankEngine.onForward(false);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnForward_ButDontMove() {
      // Given
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .build();

      // When
      humanTankEngine.onForward(true);

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward();
   }

   @Test
   void testOnForward_AndMove() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .build();

      // When
      humanTankEngine.onForward(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).moveForward();
   }

   @Test
   void testOnBackward() {
      // Given
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> moveable)
            .build();

      // When
      humanTankEngine.onBackward(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).moveBackward();
   }

   @Test
   void testOnBackward_ButDontMove_StopedPressingForward() {
      // Given
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .build();

      // When
      humanTankEngine.onBackward(false);
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
            .build();

      // When
      humanTankEngine.onTurnRight(true);
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
            .build();

      // When
      humanTankEngine.onTurnLeft(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).makeTurn(eq(-humanTankEngine.turnAngle));
   }

}
