package com.myownb3.piranha.application.battle.impl.turret;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl.DefaultGunCarriageBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.TurretImpl.GenericTurretBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.TurretStrategyHandler;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.position.PositionTransformator;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;

public abstract class GenericTankBattleApplicationTurretBuilder<V extends GenericTurretBuilder<V>, T extends GenericTankBattleApplicationTurretBuilder<V, T>> {

   private static final double MUZZLE_BRAKE_FACTOR = 1.5;
   protected PositionTransformator positionTransformator;
   protected BelligerentParty belligerentParty;
   protected DestructionHelper destructionHelper;
   protected TurretStrategyHandler turretStrategyHandler;
   private Grid grid;
   private DetectorConfig detectorConfig;
   private Position tankTurretPos;
   private GunConfig gunConfig;
   private double turretRotationSpeed;
   private double gunHeight;
   private double gunWidth;
   private ProjectileTypes projectileType;
   private Shape gunCarriageShape;
   private boolean withMuzzleBrake;
   private Orientation gunOrientation;

   protected GenericTankBattleApplicationTurretBuilder() {
      this.positionTransformator = pos -> pos;
      this.withMuzzleBrake = false;
      this.gunOrientation = Orientation.HORIZONTAL;
   }

   public T withGrid(Grid grid) {
      this.grid = grid;
      return getThis();
   }

   public T withDetectorConfig(DetectorConfig detectorConfig) {
      this.detectorConfig = detectorConfig;
      return getThis();
   }

   public T withTurretPosition(Position turretPos) {
      this.tankTurretPos = turretPos;
      return getThis();
   }

   public T withGunConfig(GunConfig gunConfig) {
      this.gunConfig = gunConfig;
      return getThis();
   }

   public T withTurretRotationSpeed(double turretRotationSpeed) {
      this.turretRotationSpeed = turretRotationSpeed;
      return getThis();
   }

   public T withGunHeight(double gunHeight) {
      this.gunHeight = gunHeight;
      return getThis();
   }

   public T withGunWidth(double gunWidth) {
      this.gunWidth = gunWidth;
      return getThis();
   }

   public T withGunCarriageShape(Shape gunCarriageShape) {
      this.gunCarriageShape = gunCarriageShape;
      return getThis();
   }

   public T withProjectileType(ProjectileTypes projectileType) {
      this.projectileType = projectileType;
      return getThis();
   }

   public T withBelligerentParty(BelligerentParty belligerentParty) {
      this.belligerentParty = belligerentParty;
      return getThis();
   }

   public T withMuzzleBrake() {
      this.withMuzzleBrake = true;
      return getThis();
   }

   public T withPositionTransformator(PositionTransformator positionTransformator) {
      this.positionTransformator = positionTransformator;
      return getThis();
   }

   public T withTurretStrategyHandler(TurretStrategyHandler turretStrategyHandler) {
      this.turretStrategyHandler = turretStrategyHandler;
      return getThis();
   }

   public T withDestructionHelper(DestructionHelper destructionHelper) {
      this.destructionHelper = destructionHelper;
      return getThis();
   }

   public T withGunOrientation(Orientation orientation) {
      this.gunOrientation = orientation;
      return getThis();
   }

   protected abstract V getTurretBuilder();

   protected abstract T getThis();

   public Turret build() {
      requireAllNonNull();
      Rectangle muzzleBrake = buildMuzzleBrake();
      return getTurretBuilder()
            .withBelligerentParty(belligerentParty)
            .withDetector(DetectorBuilder.builder()
                  .withAngleInc(detectorConfig.getEvasionAngleInc())
                  .withDetectorAngle(detectorConfig.getDetectorAngle())
                  .withDetectorReach(detectorConfig.getDetectorReach())
                  .withEvasionAngle(detectorConfig.getDetectorAngle())
                  .withEvasionDistance(detectorConfig.getEvasionDistance())
                  .build())
            .withPositionTransformator(positionTransformator)
            .withDestructionHelper(destructionHelper)
            .withTurretStrategyHandler(turretStrategyHandler)
            .withGridElementEvaluator(getGridElementEvaluator())
            .withGunCarriage(DefaultGunCarriageBuilder.builder()
                  .withRotationSpeed(turretRotationSpeed)
                  .withGun(DefaultGunBuilder.builder()
                        .withGunProjectileType(projectileType)
                        .withGunConfig(gunConfig)
                        .withGunShape(GunShapeBuilder.builder()
                              .withBarrel(RectangleBuilder.builder()
                                    .withHeight(gunHeight)
                                    .withWidth(gunWidth)
                                    .withCenter(tankTurretPos)
                                    .withOrientation(gunOrientation)
                                    .build())
                              .withMuzzleBrake(muzzleBrake)
                              .build())
                        .build())
                  .withShape(gunCarriageShape)
                  .build())
            .build();
   }

   protected GridElementEvaluator getGridElementEvaluator() {
      return (position, distance) -> grid.getAllGridElementsWithinDistance(position, distance);
   }

   private Rectangle buildMuzzleBrake() {
      if (withMuzzleBrake) {
         return RectangleBuilder.builder()
               .withHeight(gunWidth * MUZZLE_BRAKE_FACTOR)
               .withWidth(gunWidth * MUZZLE_BRAKE_FACTOR)
               .withCenter(tankTurretPos)
               .withOrientation(gunOrientation)
               .build();
      }
      return null;
   }

   private void requireAllNonNull() {
      requireNonNull(projectileType);
      requireNonNull(grid);
      requireNonNull(gunConfig);
      requireNonNull(tankTurretPos);
      requireNonNull(detectorConfig);
   }
}
