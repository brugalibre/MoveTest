package com.myownb3.piranha.core.weapon.tank.strategy.handler.impl;

import com.myownb3.piranha.core.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class WaitWhileShootingTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private TankEngine tankEngine;
   private TankDetector tankDetector;

   public WaitWhileShootingTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.tankDetector = tankStrategyHandleInput.getTankDetector();
      this.turret = tankStrategyHandleInput.getTurret();
      this.tankEngine = tankStrategyHandleInput.getTankEngine();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      tankDetector.autodetect();
      if (has2MoveForward()) {
         tankEngine.moveForward();
      }
   }

   private boolean has2MoveForward() {
      return !turret.isShooting() || tankDetector.isUnderFire();
   }
}