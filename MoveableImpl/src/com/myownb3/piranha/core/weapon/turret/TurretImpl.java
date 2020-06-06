package com.myownb3.piranha.core.weapon.turret;

import static java.util.Objects.nonNull;

import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.weapon.turret.TurretScanner.TurretScannerBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl.TurretShapeBuilder;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TurretImpl implements Turret {

   private TurretState state;
   private GunCarriage gunCarriage;
   private TurretScanner turretScanner;
   private TurretShape shape;

   private TurretImpl(GunCarriage gunCarriage, TurretShape turretShape) {
      init(gunCarriage.getShape().getCenter(), gunCarriage, turretShape);
   }

   private TurretImpl(TurretScanner turretScanner, GunCarriage gunCarriage, TurretShape turretShape) {
      this.turretScanner = turretScanner;
      init(gunCarriage.getShape().getCenter(), gunCarriage, turretShape);
   }

   private void init(Position position, GunCarriage gunCarriage, TurretShape turretShape) {
      this.gunCarriage = gunCarriage;
      this.shape = turretShape;
      this.state = TurretState.SCANNING;
   }

   @Override
   public void autodetect() {
      switch (state) {
         case SCANNING:
            handleScanningState();
            break;
         case ACQUIRING:
            turretScanner.getNearestDetectedTargetPos()
                  .ifPresent(acquiredTargetPos -> handleAcquiringState(acquiredTargetPos));
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
      // The Position of the GunCarriage is master 
      shape.transform(gunCarriage.getShape().getCenter());
   }

   private void handleReturning() {
      gunCarriage.turn2ParkPosition();
      if (gunCarriage.isInParkingPosition()) {
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

   @Visible4Testing
   TurretState getTurretStatus() {
      return state;
   }

   @Override
   public GunCarriage getGunCarriage() {
      return gunCarriage;
   }

   @Override
   public TurretShape getShape() {
      return shape;
   }

   public static class TurretBuilder {

      private TurretShape turretShape;
      private GunCarriage gunCarriage;
      private IDetector detector;
      private TurretScanner turretScanner;
      private TargetPositionLeadEvaluator targetPositionLeadEvaluator;
      private GridElementEvaluator gridElementsEvaluator;

      private TurretBuilder() {
         // private
      }

      public TurretBuilder withGridElementEvaluator(GridElementEvaluator gridElementsEvaluator) {
         this.gridElementsEvaluator = gridElementsEvaluator;
         return this;
      }

      public TurretBuilder withGunCarriage(GunCarriage gunCarriage) {
         this.gunCarriage = gunCarriage;
         return this;
      }

      public TurretBuilder withDetector(IDetector detector) {
         this.detector = detector;
         return this;
      }

      public TurretBuilder withTurretScanner(TurretScanner turretScanner) {
         this.turretScanner = turretScanner;
         return this;
      }

      public TurretBuilder withTargetPositionLeadEvaluator(TargetPositionLeadEvaluator targetPositionLeadEvaluator) {
         this.targetPositionLeadEvaluator = targetPositionLeadEvaluator;
         return this;
      }

      private void setTurretScanner(TurretImpl abstractTurret) {
         TargetPositionLeadEvaluator leadEvaluator = targetPositionLeadEvaluator != null ? targetPositionLeadEvaluator
               : new TargetPositionLeadEvaluatorImpl(getProjectilVelocity(abstractTurret));
         abstractTurret.turretScanner = TurretScannerBuilder.builder()
               .withTurret(abstractTurret)
               .withGridElementEvaluator(gridElementsEvaluator)
               .withDetector(detector)
               .withTargetPositionLeadEvaluator(leadEvaluator)
               .build();
      }

      public TurretImpl build() {
         turretShape = TurretShapeBuilder.builder()
               .wighGunCarriage(gunCarriage)
               .build();
         if (nonNull(turretScanner)) {
            return new TurretImpl(turretScanner, gunCarriage, turretShape);
         }
         TurretImpl turretImpl = new TurretImpl(gunCarriage, turretShape);
         setTurretScanner(turretImpl);
         return turretImpl;
      }

      private static double getProjectilVelocity(Turret turret) {
         return turret.getGunCarriage()
               .getGun()
               .getGunConfig()
               .getVeloCity();
      }

      public static TurretBuilder builder() {
         return new TurretBuilder();
      }
   }
}
