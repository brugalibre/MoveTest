package com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.human;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

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
   void testOnKeepFired() {
      // Given
      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);

      // When
      handler.onStartFire(true);
      handler.handleTankStrategy();
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage, times(2)).fire();
   }

   @Test
   void testOnSingleShotFired() {
      // Given
      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);

      // When
      handler.onSingleShotFired();
      handler.handleTankStrategy();
      handler.handleTankStrategy();

      // Then
      verify(gunCarriage).fire();
   }

   @Test
   void testOnKeepFired_NotFIre() {
      // Given
      GunCarriage gunCarriage = mockGunCarriage();
      HumanControlledTurretStrategyHandler handler = new HumanControlledTurretStrategyHandler(gunCarriage);

      // When
      handler.onStartFire(false);
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
