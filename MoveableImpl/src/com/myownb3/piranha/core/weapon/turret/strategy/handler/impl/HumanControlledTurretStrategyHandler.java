package com.myownb3.piranha.core.weapon.turret.strategy.handler.impl;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.human.HumanToTurretInteractionCallbackHandler;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.weapon.turret.strategy.handler.TurretStrategyHandler;

public class HumanControlledTurretStrategyHandler implements TurretStrategyHandler, HumanToTurretInteractionCallbackHandler {

   private GunCarriage gunCarriage;
   private Position target;
   private boolean has2Fire;

   public HumanControlledTurretStrategyHandler(GunCarriage gunCarriage) {
      this.gunCarriage = gunCarriage;
      this.target = gunCarriage.getShape().getCenter();
   }

   @Override
   public TurretState getTurretStatus() {
      return TurretState.NONE;
   }

   @Override
   public void handleTankStrategy() {
      gunCarriage.aimTargetPos(target);
      if (has2Fire) {
         gunCarriage.fire();
      }
   }

   @Override
   public void onFired(boolean startFire) {
      has2Fire = startFire;

   }

   @Override
   public void onTurretTurned(Position turretPos) {
      this.target = turretPos;
   }
}
