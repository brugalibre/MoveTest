package com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl;

import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.moveables.engine.MoveableEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

public class WaitWhileShootingTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private MoveableEngine moveableEngine;
   private TankDetector tankDetector;

   public WaitWhileShootingTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.tankDetector = tankStrategyHandleInput.getTankDetector();
      this.turret = tankStrategyHandleInput.getTurret();
      this.moveableEngine = tankStrategyHandleInput.getMoveableEngine();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      tankDetector.autodetect();
      if (has2MoveForward()) {
         moveableEngine.moveForward();
      } else {
         moveableEngine.stopMoveForward();
      }
   }

   private boolean has2MoveForward() {
      return !turret.isShooting() || tankDetector.isUnderFire();
   }
}
