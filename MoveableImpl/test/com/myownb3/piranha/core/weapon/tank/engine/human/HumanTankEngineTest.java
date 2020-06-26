package com.myownb3.piranha.core.weapon.tank.engine.human;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.weapon.tank.engine.human.HumanTankEngine.HumanTankEngineBuilder;

class HumanTankEngineTest {

   @Test
   void testOnForward_ButDontMove_StopedPressingForward() {
      // Given
      int velocity = 5;
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .build();

      // When
      humanTankEngine.onForward(false);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward(eq(velocity));
   }

   @Test
   void testOnForward_ButDontMove() {
      // Given
      int velocity = 5;
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .build();

      // When
      humanTankEngine.onForward(true);

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveForward(eq(velocity));
   }

   @Test
   void testOnForward_AndMove() {
      // Given
      int velocity = 5;
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
            .withLazyMoveable(() -> moveable)
            .build();

      // When
      humanTankEngine.onForward(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).moveForward(eq(velocity));
   }

   @Test
   void testOnBackward() {
      // Given
      int velocity = 5;
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
            .withLazyMoveable(() -> moveable)
            .build();

      // When
      humanTankEngine.onBackward(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).moveBackward(eq(velocity));
   }

   @Test
   void testOnBackward_ButDontMove_StopedPressingForward() {
      // Given
      int velocity = 5;
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
            .withLazyMoveable(() -> mock(EndPointMoveable.class))
            .build();

      // When
      humanTankEngine.onBackward(false);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable(), never()).moveBackward(eq(velocity));
   }

   @Test
   void testOnTurnRight() {
      // Given
      int velocity = 5;
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
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
      int velocity = 5;
      EndPointMoveable moveable = mock(EndPointMoveable.class);
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withVelocity(velocity)
            .withLazyMoveable(() -> moveable)
            .build();

      // When
      humanTankEngine.onTurnLeft(true);
      humanTankEngine.moveForward();

      // Then
      verify(humanTankEngine.getMoveable()).makeTurn(eq(-humanTankEngine.turnAngle));
   }

}
