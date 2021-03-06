package com.myownb3.piranha.core.battle.weapon.tank;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.TankStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.factory.TankStrategyHandlercFactory;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl.TankStrategyHandleInput;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.engine.MoveableEngine;

public class TankImpl implements Tank {

   @Visible4Testing
   MoveableEngine moveableEngine;
   private Turret turret;
   private TankShape tankShape;
   private BelligerentParty belligerentParty;
   private DestructionHelper destructionHelper;
   private TankStrategyHandler tankStrategyHandler;

   private TankImpl(Turret turret, MoveableEngine moveableEngine, TankShape tankShape, BelligerentParty belligerentParty, TankDetector tankDetector,
         DestructionHelper destructionHelper, TankStrategy tankStrategy) {
      this.turret = turret;
      this.moveableEngine = moveableEngine;
      this.tankShape = tankShape;
      this.belligerentParty = belligerentParty;
      this.destructionHelper = destructionHelper;
      this.tankStrategyHandler = TankStrategyHandlercFactory.INSTANCE.createTankStrategyHandler(tankStrategy,
            TankStrategyHandleInput.of(this.getTurret(), moveableEngine, tankDetector));
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
   public Turret getTurret() {
      return turret;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
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
      private MoveableEngine moveableEngine;
      private BelligerentParty belligerentParty;
      private TankDetector tankDetector;
      private double health;
      private TankStrategy tankStrategy;
      private OnDestroyedCallbackHandler onDestroyedCallbackHandler;

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

      public TankBuilder withOnDestroyedCallbackHandler(OnDestroyedCallbackHandler onDestroyedCallbackHandler) {
         this.onDestroyedCallbackHandler = onDestroyedCallbackHandler;
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

      public TankBuilder withMoveableEngine(MoveableEngine moveableEngine) {
         this.moveableEngine = moveableEngine;
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
         DestructionHelper destructionHelper = buildDestructionHelper(health);
         requireNonNull(tankStrategy);
         return new TankImpl(turret, moveableEngine, tankShape, belligerentParty, tankDetector, destructionHelper, tankStrategy);
      }

      private DestructionHelper buildDestructionHelper(double health) {
         return DestructionHelperBuilder.builder()
               .withDamage(0)
               .withHealth(health)
               .withSelfDestructiveDamage(0)
               .withOnDestroyedCallbackHandler(onDestroyedCallbackHandler)
               .withDestroyedAudioClip(AudioClipBuilder.builder()
                     .withAudioResource(AudioConstants.EXPLOSION_SOUND)
                     .build())
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
