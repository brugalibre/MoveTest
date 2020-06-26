package com.myownb3.piranha.core.weapon.turret;

import static java.util.Objects.nonNull;

import java.util.Optional;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.position.PositionTransformator;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl.TurretShapeBuilder;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TurretScanner;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TurretScanner.TurretScannerBuilder;

public class TurretImpl implements Turret {

   protected TurretState state;
   protected TurretShape shape;
   protected double parkingAngle;
   private GunCarriage gunCarriage;
   private TurretScanner turretScanner;
   private BelligerentParty belligerentParty;

   protected TurretImpl(GunCarriage gunCarriage, TurretShape turretShape, BelligerentParty belligerentParty) {
      init(gunCarriage.getShape().getCenter(), gunCarriage, turretShape, belligerentParty);
   }

   protected TurretImpl(TurretScanner turretScanner, GunCarriage gunCarriage, TurretShape turretShape, BelligerentParty belligerentParty) {
      this.turretScanner = turretScanner;
      init(gunCarriage.getShape().getCenter(), gunCarriage, turretShape, belligerentParty);
   }

   private void init(Position position, GunCarriage gunCarriage, TurretShape turretShape, BelligerentParty belligerentParty) {
      this.gunCarriage = gunCarriage;
      this.shape = turretShape;
      this.state = TurretState.SCANNING;
      this.belligerentParty = belligerentParty;
      this.parkingAngle = position.getDirection().getAngle();
   }

   @Override
   public void autodetect() {
      switch (state) {
         case SCANNING:// fall through
         case TARGET_DETECTED:
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
   }

   private void handleReturning() {
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

   @Override
   public boolean isShooting() {
      return getTurretStatus() == TurretState.SHOOTING;
   }

   @Override
   public boolean isAcquiring() {
      return getTurretStatus() == TurretState.ACQUIRING;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return belligerentParty.isEnemyParty(otherBelligerent.getBelligerentParty());
   }

   private TurretState getTurretStatus() {
      return state;
   }

   @Override
   public TurretShape getShape() {
      return shape;
   }

   public static abstract class GenericTurretBuilder<T> {

      protected BelligerentParty belligerentParty;
      protected TurretShape turretShape;
      protected GunCarriage gunCarriage;
      private IDetector detector;
      protected TurretScanner turretScanner;
      private TargetPositionLeadEvaluator targetPositionLeadEvaluator;
      private GridElementEvaluator gridElementsEvaluator;
      private PositionTransformator positionTransformator;

      protected GenericTurretBuilder() {
         belligerentParty = BelligerentPartyConst.REBEL_ALLIANCE;
         positionTransformator = pos -> pos;
      }

      public T withGridElementEvaluator(GridElementEvaluator gridElementsEvaluator) {
         this.gridElementsEvaluator = gridElementsEvaluator;
         return getThis();
      }

      public T withGunCarriage(GunCarriage gunCarriage) {
         this.gunCarriage = gunCarriage;
         return getThis();
      }

      public T withDetector(IDetector detector) {
         this.detector = detector;
         return getThis();
      }

      public T withTurretScanner(TurretScanner turretScanner) {
         this.turretScanner = turretScanner;
         return getThis();
      }

      public T withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return getThis();
      }

      public T withTargetPositionLeadEvaluator(TargetPositionLeadEvaluator targetPositionLeadEvaluator) {
         this.targetPositionLeadEvaluator = targetPositionLeadEvaluator;
         return getThis();
      }

      public T withPositionTransformator(PositionTransformator positionTransformator) {
         this.positionTransformator = positionTransformator;
         return getThis();
      }

      protected abstract T getThis();

      protected void buildTurretShape() {
         turretShape = TurretShapeBuilder.builder()
               .wighGunCarriage(gunCarriage)
               .wighPositionTransformator(positionTransformator)
               .build();
      }

      protected void setTurretScanner(TurretImpl turretImpl) {
         TargetPositionLeadEvaluator leadEvaluator = targetPositionLeadEvaluator != null ? targetPositionLeadEvaluator
               : new TargetPositionLeadEvaluatorImpl(getProjectilConfig(turretImpl));
         turretImpl.turretScanner = TurretScannerBuilder.builder()
               .withTurret(turretImpl)
               .withGridElementEvaluator(gridElementsEvaluator)
               .withDetector(detector)
               .withTargetPositionLeadEvaluator(leadEvaluator)
               .build();
      }

      public TurretImpl build() {
         buildTurretShape();
         if (nonNull(turretScanner)) {
            return new TurretImpl(turretScanner, gunCarriage, turretShape, belligerentParty);
         }
         TurretImpl turretImpl = new TurretImpl(gunCarriage, turretShape, belligerentParty);
         setTurretScanner(turretImpl);
         return turretImpl;
      }

      private static ProjectileConfig getProjectilConfig(TurretImpl turretImpl) {
         return turretImpl.gunCarriage
               .getGun()
               .getGunConfig()
               .getProjectileConfig();
      }

      public static class TurretBuilder extends GenericTurretBuilder<TurretBuilder> {

         protected TurretBuilder() {
            // protected
         }

         public static TurretBuilder builder() {
            return new TurretBuilder();
         }

         @Override
         protected TurretBuilder getThis() {
            return this;
         }
      }
   }
}
