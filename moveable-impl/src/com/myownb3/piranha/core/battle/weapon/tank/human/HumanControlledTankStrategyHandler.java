package com.myownb3.piranha.core.battle.weapon.tank.human;

import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.moveables.engine.MoveableEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

public class HumanControlledTankStrategyHandler implements TankStrategyHandler {

   private Turret turret;
   private MoveableEngine moveableEngine;
   private TankDetector tankDetector;

   public HumanControlledTankStrategyHandler(TankStrategyHandleInput tankStrategyHandleInput) {
      this.turret = tankStrategyHandleInput.getTurret();
      this.moveableEngine = tankStrategyHandleInput.getMoveableEngine();
      this.tankDetector = tankStrategyHandleInput.getTankDetector();
   }

   @Override
   public void handleTankStrategy() {
      turret.autodetect();
      tankDetector.autodetect();
      moveableEngine.moveForward();
   }
}
