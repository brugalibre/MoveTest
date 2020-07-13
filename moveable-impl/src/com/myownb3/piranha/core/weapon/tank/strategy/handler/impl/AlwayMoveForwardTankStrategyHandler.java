package com.myownb3.piranha.core.weapon.tank.strategy.handler.impl;

import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class AlwayMoveForwardTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private TankEngine tankEngine;

   public AlwayMoveForwardTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.turret = tankStrategyHandleInput.getTurret();
      this.tankEngine = tankStrategyHandleInput.getTankEngine();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      tankEngine.moveForward();
   }
}
