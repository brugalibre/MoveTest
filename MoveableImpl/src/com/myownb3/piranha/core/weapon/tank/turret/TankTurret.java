package com.myownb3.piranha.core.weapon.tank.turret;

import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.TurretImpl;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretScanner;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TankTurret extends TurretImpl {

   private ParkingAngleEvaluator parkingAngleEvaluator;

   protected TankTurret(TurretScanner turretScanner, GunCarriage gunCarriage, TurretShape turretShape, ParkingAngleEvaluator parkingAngleEvaluator) {
      super(turretScanner, gunCarriage, turretShape);
      this.parkingAngleEvaluator = parkingAngleEvaluator;
   }

   @Override
   public void autodetect() {
      initReturningToMovingDirection();
      super.autodetect();
   }

   private void initReturningToMovingDirection() {
      if (hasToAdjustGunGarriagePos()) {
         state = TurretState.RETURNING;
         this.parkingAngle = parkingAngleEvaluator.evaluateParkingAngle();
      }
   }

   private boolean hasToAdjustGunGarriagePos() {
      return state == TurretState.SCANNING && gunCarriagIsNotInMovingDirection();
   }

   private boolean gunCarriagIsNotInMovingDirection() {
      return parkingAngleEvaluator.evaluateParkingAngle() != shape.getCenter().getDirection().getAngle();
   }

   @FunctionalInterface
   public interface ParkingAngleEvaluator {
      double evaluateParkingAngle();
   }

   public static class TankTurretBuilder extends TurretBuilder {

      private ParkingAngleEvaluator parkingAngleEvaluator;

      protected TankTurretBuilder() {
         super();
      }

      public TankTurretBuilder withParkingAngleEvaluator(ParkingAngleEvaluator parkingAngleEvaluator) {
         this.parkingAngleEvaluator = parkingAngleEvaluator;
         return this;
      }

      public static TankTurretBuilder builder() {
         return new TankTurretBuilder();
      }

      @Override
      protected TurretBuilder getThis() {
         return this;
      }

      @Override
      public TankTurret build() {
         buildTurretShape();
         TankTurret tankTurret = new TankTurret(turretScanner, gunCarriage, turretShape, parkingAngleEvaluator);
         setTurretScanner(tankTurret);
         return tankTurret;
      }
   }
}
