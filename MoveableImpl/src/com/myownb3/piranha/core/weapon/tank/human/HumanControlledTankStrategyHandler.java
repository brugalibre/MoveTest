package com.myownb3.piranha.core.weapon.tank.human;

import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class HumanControlledTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private TankEngine tankEngine;

   public HumanControlledTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.turret = tankStrategyHandleInput.getTurret();
      this.tankEngine = tankStrategyHandleInput.getTankEngine();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      tankEngine.moveForward();
   }
}
