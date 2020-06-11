package com.myownb3.piranha.core.weapon.tank;

import static java.util.Objects.nonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankImpl implements Tank {

   private Turret turret;
   private TankEngine tankEngine;
   private TankShape tankShape;
   private BelligerentParty belligerentParty;
   private TankDetector tankDetector;
   private DestructionHelper destructionHelper;

   private TankImpl(Turret turret, TankEngine tankEngine, TankShape tankShape, BelligerentParty belligerentParty, TankDetector tankDetector,
         DestructionHelper destructionHelper) {
      this.turret = turret;
      this.tankEngine = tankEngine;
      this.tankShape = tankShape;
      this.belligerentParty = belligerentParty;
      this.tankDetector = tankDetector;
      this.destructionHelper = destructionHelper;
   }

   @Override
   public void autodetect() {
      turret.autodetect();
      tankDetector.autodetect();
      if (has2MoveForward()) {
         tankEngine.moveForward();
      }
   }

   private boolean has2MoveForward() {
      return !turret.isShooting() || tankDetector.isUnderFire();
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return belligerentParty.isEnemyParty(otherBelligerent.getBelligerentParty());
   }

   @Override
   public Turret getTurret() {
      return turret;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public TankEngine getTankEngine() {
      return tankEngine;
   }

   @Override
   public TankShape getShape() {
      return tankShape;
   }

   @Override
   public Position getPosition() {
      return tankShape.getCenter();
   }

   public static final class TankBuilder {

      private Turret turret;
      private Shape tankHull;
      private Grid grid;
      private int movingIncrement;
      private List<EndPosition> endPositions;
      private TankEngine tankEngine;
      private BelligerentParty belligerentParty;
      private TankDetector tankDetector;
      private DestructionHelper destructionHelper;
      private double health;

      private TankBuilder() {
         movingIncrement = 1;
         belligerentParty = BelligerentPartyConst.REBEL_ALLIANCE;
         health = Integer.MAX_VALUE;
      }

      public TankBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public TankBuilder withTurret(Turret turret) {
         this.turret = turret;
         return this;
      }

      public TankBuilder withHull(Shape tankHull) {
         this.tankHull = tankHull;
         return this;
      }

      public TankBuilder withTankEngine(TankEngine tankEngine) {
         this.tankEngine = tankEngine;
         return this;
      }

      public TankBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public TankBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankBuilder withEngineVelocity(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public TankBuilder withEndPositions(List<EndPosition> endPositions) {
         this.endPositions = endPositions;
         return this;
      }

      public TankBuilder withTankDetector(TankDetector tankDetector) {
         this.tankDetector = tankDetector;
         return this;
      }

      public Tank build() {
         TankShape tankShape = buildTankShape();
         TankHolder tankHolder = new TankHolder();
         TankEngine tankEngine = buildNewOrGetExistingEngine(tankShape, this.tankEngine);
         this.destructionHelper = buildDestructionHelper(health);
         TankImpl tankImpl = new TankImpl(turret, tankEngine, tankShape, belligerentParty, tankDetector, destructionHelper);
         return tankHolder
               .setAndReturnTank(tankImpl);
      }

      private DestructionHelper buildDestructionHelper(double health) {
         return DestructionHelperBuilder.builder()
               .withDamage(DamageImpl.of(0))
               .withHealth(HealthImpl.of(health))
               .withSelfDestructiveDamage(DefaultSelfDestructiveImpl.of(0))
               .withOnDestroyedCallbackHandler(() -> {
               })
               .build();
      }

      private TankShape buildTankShape() {
         return TankShapeBuilder.builder()
               .withHull(tankHull)
               .withTurretShape(turret.getShape())
               .build();
      }

      private TankEngine buildNewOrGetExistingEngine(TankShape tankShape, TankEngine tankEngine) {
         if (nonNull(tankEngine)) {
            return tankEngine;
         }
         return TankEngineBuilder.builder()
               .withMoveableController(MoveableControllerBuilder.builder()
                     .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                     .withEndPositions(endPositions)
                     .withEndPointMoveable()
                     .withBelligerentParty(belligerentParty)
                     .withGrid(grid)
                     .withStartPosition(tankShape.getCenter())
                     .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                           .withGrid(grid)
                           .withDetector(DetectorBuilder.builder()
                                 .build())
                           .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                                 .withReturningAngleIncMultiplier(1)
                                 .withOrientationAngle(1)
                                 .withReturningMinDistance(1)
                                 .withReturningAngleMargin(1)
                                 .withPassingDistance(25)
                                 .withPostEvasionReturnAngle(4)
                                 .withDetectorConfig(DetectorConfigBuilder.builder()
                                       .build())
                                 .build())
                           .build())
                     .withShape(tankShape)
                     .withMovingIncrement(movingIncrement)
                     .buildAndReturnParentBuilder()
                     .withPostMoveForwardHandler((m) -> {
                     })
                     .build())
               .build();
      }

      public static TankBuilder builder() {
         return new TankBuilder();
      }
   }
}
