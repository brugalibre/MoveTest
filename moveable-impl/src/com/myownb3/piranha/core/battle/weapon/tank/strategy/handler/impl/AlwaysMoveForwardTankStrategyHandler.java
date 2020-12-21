package com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl;

import com.myownb3.piranha.core.moveables.engine.MoveableEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

public class AlwaysMoveForwardTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private MoveableEngine moveableEngine;

   public AlwaysMoveForwardTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.turret = tankStrategyHandleInput.getTurret();
      this.moveableEngine = tankStrategyHandleInput.getMoveableEngine();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      moveableEngine.moveForward();
   }
}
