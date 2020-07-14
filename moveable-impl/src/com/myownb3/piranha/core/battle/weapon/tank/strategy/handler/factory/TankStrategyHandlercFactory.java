package com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.factory;

import com.myownb3.piranha.core.battle.weapon.tank.human.HumanControlledTankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl.AlwayMoveForwardTankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl.WaitWhileShootingTankStrategyHandler;

/**
 * Is responsible for creating different {@link TankStrategyHandler}s
 * 
 * @author Dominic
 *
 */
public class TankStrategyHandlercFactory {

   public static final TankStrategyHandlercFactory INSTANCE = new TankStrategyHandlercFactory();

   private TankStrategyHandlercFactory() {
      // private
   }

   public TankStrategyHandler createTankStrategyHandler(TankStrategy tankStrategy, TankStrategyHandleInput tankStrategyHandleInput) {
      switch (tankStrategy) {
         case ALWAYS_MOVE_AND_SHOOT:
            return new AlwayMoveForwardTankStrategyHandler(tankStrategyHandleInput);
         case WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE:
            return new WaitWhileShootingTankStrategyHandler(tankStrategyHandleInput);
         case HUMAN_CONTROLLED:
            return new HumanControlledTankStrategyHandler(tankStrategyHandleInput);
         default:
            throw new IllegalStateException("Unknown TankStrategy '" + tankStrategy + "'");
      }
   }
}
