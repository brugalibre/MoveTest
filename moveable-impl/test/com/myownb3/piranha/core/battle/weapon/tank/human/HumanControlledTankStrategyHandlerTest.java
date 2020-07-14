package com.myownb3.piranha.core.battle.weapon.tank.human;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

class HumanControlledTankStrategyHandlerTest {

   @Test
   void testHandleTankStrategy() {

      // Given
      TankEngine tankEngine = mock(TankEngine.class);
      Turret turret = mock(Turret.class);
      TankDetector tankDetector = mock(TankDetector.class);
      HumanControlledTankStrategyHandler tankStrategyHandler =
            new HumanControlledTankStrategyHandler(TankStrategyHandleInput.of(turret, tankEngine, tankDetector));

      // When
      tankStrategyHandler.handleTankStrategy();

      // Then
      verify(turret).autodetect();
      verify(tankDetector).autodetect();
      verify(tankEngine).moveForward();
   }
}
