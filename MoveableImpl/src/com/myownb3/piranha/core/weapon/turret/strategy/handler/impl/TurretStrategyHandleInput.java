package com.myownb3.piranha.core.weapon.turret.strategy.handler.impl;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.tank.turret.TankTurretBuilder.ParkingAngleEvaluator;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TurretScanner;

/**
 * Defines the necessary parameters for handling a {@link Turret}
 * 
 * @author Dominic
 *
 */
public class TurretStrategyHandleInput {

   private GunCarriage gunCarriage;
   private TurretScanner turretScanner;
   private TurretShape turretShape;
   private ParkingAngleEvaluator parkingAngleEvaluator;

   private TurretStrategyHandleInput(GunCarriage gunCarriage, TurretScanner turretScanner, TurretShape turretShape,
         ParkingAngleEvaluator parkingAngleEvaluator) {
      this.gunCarriage = requireNonNull(gunCarriage, "A TurretStrategyHandleInput always needs a GunCarriage!");
      this.turretScanner = requireNonNull(turretScanner, "A TurretStrategyHandleInput always needs a TurretScanner!");
      this.turretShape = requireNonNull(turretShape, "A TurretStrategyHandleInput always needs a TurretShape!");
      this.parkingAngleEvaluator = requireNonNull(parkingAngleEvaluator, "A TurretStrategyHandleInput always needs a ParkingAngleEvaluator!");
   }

   public GunCarriage getGunCarriage() {
      return gunCarriage;
   }

   public TurretScanner getTurretScanner() {
      return turretScanner;
   }

   public ParkingAngleEvaluator getParkingAngleEvaluator() {
      return parkingAngleEvaluator;
   }

   public static TurretStrategyHandleInput of(GunCarriage gunCarriage, TurretScanner turretScanner, TurretShape turretShape,
         ParkingAngleEvaluator parkingAngleEvaluator) {
      return new TurretStrategyHandleInput(gunCarriage, turretScanner, turretShape, parkingAngleEvaluator);
   }

   public TurretShape getTurretShape() {
      return turretShape;
   }
}
