package com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl;

import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

public class AlwaysMoveForwardTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private TankEngine tankEngine;

   public AlwaysMoveForwardTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.turret = tankStrategyHandleInput.getTurret();
      this.tankEngine = tankStrategyHandleInput.getTankEngine();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      tankEngine.moveForward();
   }
}
