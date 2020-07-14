package com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.human;

import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.turret.human.HumanToTurretInteractionCallbackHandler;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.TurretStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.human.firemode.ShootMode;
import com.myownb3.piranha.core.grid.position.Position;

public class HumanControlledTurretStrategyHandler implements TurretStrategyHandler, HumanToTurretInteractionCallbackHandler {

   private GunCarriage gunCarriage;
   private Position target;
   private ShootMode shootMode;

   public HumanControlledTurretStrategyHandler(GunCarriage gunCarriage) {
      this.gunCarriage = gunCarriage;
      this.target = gunCarriage.getShape().getCenter();
      this.shootMode = ShootMode.DONT_SHOOT;
   }

   @Override
   public TurretState getTurretStatus() {
      return TurretState.NONE;
   }

   @Override
   public void handleTankStrategy() {
      gunCarriage.aimTargetPos(target);
      handleShooting();
   }

   private void handleShooting() {
      switch (shootMode) {
         case ONE_SHOT:
            gunCarriage.fire();
            shootMode = ShootMode.DONT_SHOOT;
            break;

         case KEEP_SHOOTING:
            gunCarriage.fire();
            break;
         default:
            break;
      }
   }

   @Override
   public void onSingleShotFired() {
      shootMode = ShootMode.ONE_SHOT;
   }

   @Override
   public void onStartFire(boolean startOrStop) {
      if (startOrStop) {
         shootMode = ShootMode.KEEP_SHOOTING;
      } else {
         shootMode = ShootMode.DONT_SHOOT;
      }
   }

   @Override
   public void onTurretTurned(Position turretPos) {
      this.target = turretPos;
   }
}
