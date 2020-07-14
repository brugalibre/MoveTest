package com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl;

import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder.ParkingAngleEvaluator;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.TurretStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.TurretScanner;
import com.myownb3.piranha.core.grid.position.Position;

public class AutoAimAndShootTurretStrategyHandler implements TurretStrategyHandler {
   @Visible4Testing
   TurretState state;
   protected ParkingAngleEvaluator parkingAngleEvaluator;
   private GunCarriage gunCarriage;
   private TurretScanner turretScanner;

   public AutoAimAndShootTurretStrategyHandler(TurretStrategyHandleInput turretStrategyHandleInput) {
      this.gunCarriage = turretStrategyHandleInput.getGunCarriage();
      this.turretScanner = turretStrategyHandleInput.getTurretScanner();
      this.parkingAngleEvaluator = turretStrategyHandleInput.getParkingAngleEvaluator();
      this.state = TurretState.SCANNING;
   }

   @Override
   public TurretState getTurretStatus() {
      return state;
   }

   @Override
   public void handleTankStrategy() {
      switch (state) {
         case SCANNING:// fall through
         case TARGET_DETECTED:
            handleScanningState();
            break;
         case ACQUIRING:
            turretScanner.getNearestDetectedTargetPos()
                  .ifPresent(this::handleAcquiringState);
            break;
         case SHOOTING:
            handleShooting();
            break;
         case RETURNING:
            handleReturning();
            break;
         default:
            break;
      }
   }

   private void handleReturning() {
      double parkingAngle = parkingAngleEvaluator.evaluateParkingAngle();
      gunCarriage.turn2ParkPosition(parkingAngle);
      if (gunCarriage.isInParkingPosition(parkingAngle)) {
         state = TurretState.SCANNING;
      } else {
         handleScanningState();// Scan, maybe we found something else
      }
   }

   private void handleShooting() {
      Optional<Position> nearestDetectedTargetPos = turretScanner.getNearestDetectedTargetPos();
      if (nearestDetectedTargetPos.isPresent()) {
         handleShootingState(nearestDetectedTargetPos.get());
      } else {
         // We lost or destroyed the target -> return
         state = TurretState.RETURNING;
      }
   }

   private void handleShootingState(Position acquiredTargetPos) {
      gunCarriage.aimTargetPos(acquiredTargetPos);
      gunCarriage.fire();
      turretScanner.scan(state);
   }

   private void handleAcquiringState(Position acquiredTargetPos) {
      gunCarriage.aimTargetPos(acquiredTargetPos);
      state = evalNextState(acquiredTargetPos);
   }

   private void handleScanningState() {
      state = turretScanner.scan(state);
   }

   private TurretState evalNextState(Position acquiredTargetPos) {
      return gunCarriage.hasTargetLocked(acquiredTargetPos) ? TurretState.SHOOTING : TurretState.ACQUIRING;
   }
}
