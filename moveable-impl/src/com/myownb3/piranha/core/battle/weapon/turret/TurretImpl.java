package com.myownb3.piranha.core.battle.weapon.turret;

import static com.myownb3.piranha.util.ObjectUtils.firstNonNull;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.trajectory.TargetPositionLeadEvaluator;
import com.myownb3.piranha.core.battle.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShapeImpl.TurretShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.states.TurretState;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.TurretStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.AutoAimAndShootTurretStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.TurretStrategyHandleInput;
import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.TurretScanner;
import com.myownb3.piranha.core.battle.weapon.turret.turretscanner.TurretScanner.TurretScannerBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.position.PositionTransformator;

public class TurretImpl implements Turret {

   @Visible4Testing
   TurretStrategyHandler turretStrategyHandler;
   private TurretShape shape;
   private Optional<DestructionHelper> destructionHelperOpt;
   private BelligerentParty belligerentParty;

   protected TurretImpl(TurretShape turretShape, BelligerentParty belligerentParty, DestructionHelper destructionHelper) {
      init(turretShape, belligerentParty, destructionHelper);
   }

   private void init(TurretShape turretShape, BelligerentParty belligerentParty, DestructionHelper destructionHelper) {
      this.shape = turretShape;
      this.belligerentParty = belligerentParty;
      this.destructionHelperOpt = Optional.ofNullable(destructionHelper);
   }

   @Override
   public void autodetect() {
      turretStrategyHandler.handleTankStrategy();
   }

   @Override
   public boolean isShooting() {
      return turretStrategyHandler.getTurretStatus() == TurretState.SHOOTING;
   }

   @Override
   public boolean isAcquiring() {
      return turretStrategyHandler.getTurretStatus() == TurretState.ACQUIRING;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public TurretShape getShape() {
      return shape;
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelperOpt.ifPresent(destructionHelper -> destructionHelper.onCollision(gridElements));
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelperOpt.map(DestructionHelper::isDestroyed)
            .orElse(false);
   }

   public abstract static class GenericTurretBuilder<T> {

      protected BelligerentParty belligerentParty;
      protected GunCarriage gunCarriage;
      private IDetector detector;
      protected TurretScanner turretScanner;
      private TargetPositionLeadEvaluator targetPositionLeadEvaluator;
      private GridElementEvaluator gridElementsEvaluator;
      private PositionTransformator positionTransformator;
      private TurretStrategyHandler turretStrategyHandler;
      private DestructionHelper destructionHelper;

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

      public T withTurretStrategyHandler(TurretStrategyHandler turretStrategyHandler) {
         this.turretStrategyHandler = turretStrategyHandler;
         return getThis();
      }

      public T withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return getThis();
      }

      public T withDestructionHelper(DestructionHelper destructionHelper) {
         this.destructionHelper = destructionHelper;
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

      public TurretImpl build() {
         TurretShape turretShape = buildTurretShape();
         TurretImpl turretImpl = new TurretImpl(turretShape, belligerentParty, destructionHelper);
         if (isNull(turretStrategyHandler)) {
            this.turretScanner = getTurretScanner(turretImpl);
            this.turretStrategyHandler = buildTurretStrategyHandlerHandler(turretShape);
         }
         turretImpl.turretStrategyHandler = this.turretStrategyHandler;
         return turretImpl;
      }

      private TurretShape buildTurretShape() {
         return TurretShapeBuilder.builder()
               .wighGunCarriage(gunCarriage)
               .wighPositionTransformator(positionTransformator)
               .build();
      }

      protected TurretStrategyHandler buildTurretStrategyHandlerHandler(TurretShape turretShape) {
         double parkingAngle = gunCarriage.getShape().getCenter().getDirection().getAngle();
         return new AutoAimAndShootTurretStrategyHandler(TurretStrategyHandleInput.of(gunCarriage, turretScanner, turretShape, () -> parkingAngle));
      }

      private TurretScanner getTurretScanner(TurretImpl turretImpl) {
         if (nonNull(turretScanner)) {
            return turretScanner;
         }
         return buildTurretScanner(turretImpl, getProjectilConfig(gunCarriage));
      }

      private TurretScanner buildTurretScanner(TurretImpl turretImpl, ProjectileConfig projectileConfig) {
         TargetPositionLeadEvaluator leadEvaluator =
               firstNonNull(targetPositionLeadEvaluator, new TargetPositionLeadEvaluatorImpl(projectileConfig.getVelocity()));
         return TurretScannerBuilder.builder()
               .withTurret(turretImpl)
               .withGridElementEvaluator(gridElementsEvaluator)
               .withDetector(detector)
               .withTargetPositionLeadEvaluator(leadEvaluator)
               .build();
      }

      private static ProjectileConfig getProjectilConfig(GunCarriage gunCarriage) {
         return gunCarriage.getGun()
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
