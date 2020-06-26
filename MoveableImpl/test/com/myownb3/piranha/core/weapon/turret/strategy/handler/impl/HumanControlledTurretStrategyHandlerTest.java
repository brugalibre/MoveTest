package com.myownb3.piranha.core.weapon.turret.strategy.handler.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

class HumanControlledTurretStrategyHandlerTest {

   @Test
   void testGetTurretStatus() {

      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);

      // When
      TurretState actualStatus = handler.getTurretStatus();

      // Then
      assertThat(actualStatus, is(TurretState.NONE));
   }

   @Test
   void testOnFired() {
      // Given
      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);

      // When
      handler.onFired(true);
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage).fire();
   }

   @Test
   void testOnFired_NotFIre() {
      // Given
      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);

      // When
      handler.onFired(false);
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage, never()).fire();
   }

   @Test
   void testOnTurretTurned() {
      // Given
      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);
      Position turretPos = Positions.of(5, 5);

      // When
      handler.onTurretTurned(turretPos);
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage).aimTargetPos(eq(turretPos));
   }

   private GunCarriage mockGunCarriage() {
      GunCarriage gunCarriage = mock(GunCarriage.class);
      when(gunCarriage.getShape()).thenReturn(PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .build());
      return gunCarriage;
   }
}
