package com.myownb3.piranha.core.weapon.tank.strategy.handler.factory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.weapon.tank.human.HumanControlledTankStrategyHandler;
import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;

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

   @Test
   void testCreateHumanTankStrategyHandler() {

      // Given
      TankStrategy tankStrategy = TankStrategy.HUMAN_CONTROLLED;

      // When
      TankStrategyHandler tankStrategyHandler =
            TankStrategyHandlercFactory.INSTANCE.createTankStrategyHandler(tankStrategy, mock(TankStrategyHandleInput.class));

      // Then
      assertThat(tankStrategyHandler instanceof HumanControlledTankStrategyHandler, is(true));
   }

}
