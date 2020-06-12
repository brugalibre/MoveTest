package com.myownb3.piranha.core.weapon.tank.strategy.handler.factory;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;

class TankStrategyHandlercFactoryTest {

   @Test
   void testCreateTankStrategyHandler() {

      // Given
      TankStrategy tankStrategy = TankStrategy.NONE;

      // When
      Executable ex = () -> {
         TankStrategyHandlercFactory.INSTANCE.createTankStrategyHandler(tankStrategy, null);
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

}
