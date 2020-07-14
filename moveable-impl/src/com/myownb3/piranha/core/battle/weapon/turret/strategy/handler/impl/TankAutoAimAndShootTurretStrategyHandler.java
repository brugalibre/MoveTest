package com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl;

import static java.lang.Math.abs;

import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;

public class TankAutoAimAndShootTurretStrategyHandler extends AutoAimAndShootTurretStrategyHandler {
   private static final double ANGLE_DIFF_MARGIN = 0.1;
   private TurretShape turretShape;

   public TankAutoAimAndShootTurretStrategyHandler(TurretStrategyHandleInput turretStrategyHandleInput) {
      super(turretStrategyHandleInput);
      this.turretShape = turretStrategyHandleInput.getTurretShape();
   }

   @Override
   public void handleTankStrategy() {
      initReturningToMovingDirection();
      super.handleTankStrategy();
   }

   private void initReturningToMovingDirection() {
      if (hasToAdjustGunGarriagePos()) {
         state = TurretState.RETURNING;
      }
   }

   private boolean hasToAdjustGunGarriagePos() {
      return state == TurretState.SCANNING && gunCarriagIsNotInMovingDirection();
   }

   private boolean gunCarriagIsNotInMovingDirection() {
      double angleDiff = parkingAngleEvaluator.evaluateParkingAngle() - turretShape.getCenter().getDirection().getAngle();
      return abs(angleDiff) >= ANGLE_DIFF_MARGIN;
   }
}
