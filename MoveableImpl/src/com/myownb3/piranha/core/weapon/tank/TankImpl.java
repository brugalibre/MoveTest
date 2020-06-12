package com.myownb3.piranha.core.weapon.tank;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DefaultSelfDestructiveImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.factory.TankStrategyHandlercFactory;
import com.myownb3.piranha.core.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankImpl implements Tank {

   private Turret turret;
   private TankEngine tankEngine;
   private TankShape tankShape;
   private BelligerentParty belligerentParty;
   private DestructionHelper destructionHelper;
   private TankStrategyHandler tankStrategyHandler;

   private TankImpl(Turret turret, TankEngine tankEngine, TankShape tankShape, BelligerentParty belligerentParty, TankDetector tankDetector,
         DestructionHelper destructionHelper, TankStrategy tankStrategy) {
      this.turret = turret;
      this.tankEngine = tankEngine;
      this.tankShape = tankShape;
      this.belligerentParty = belligerentParty;
      this.destructionHelper = destructionHelper;
      this.tankStrategyHandler = TankStrategyHandlercFactory.INSTANCE.createTankStrategyHandler(tankStrategy,
            TankStrategyHandleInput.of(this.getTurret(), tankEngine, tankDetector));
   }

   @Override
   public void autodetect() {
      tankStrategyHandler.handleTankStrategy();
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
      private TankEngine tankEngine;
      private BelligerentParty belligerentParty;
      private TankDetector tankDetector;
      private DestructionHelper destructionHelper;
      private double health;
      private TankStrategy tankStrategy;

      private TankBuilder() {
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

      public TankBuilder withTankStrategy(TankStrategy tankStrategy) {
         this.tankStrategy = tankStrategy;
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

      public TankBuilder withTankDetector(TankDetector tankDetector) {
         this.tankDetector = tankDetector;
         return this;
      }

      public Tank build() {
         TankShape tankShape = buildTankShape();
         this.destructionHelper = buildDestructionHelper(health);
         requireNonNull(tankStrategy);
         return new TankImpl(turret, tankEngine, tankShape, belligerentParty, tankDetector, destructionHelper, tankStrategy);
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

      public static TankBuilder builder() {
         return new TankBuilder();
      }
   }
}
