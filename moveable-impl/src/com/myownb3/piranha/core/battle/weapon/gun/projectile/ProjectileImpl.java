package com.myownb3.piranha.core.battle.weapon.gun.projectile;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.destruction.Damage;
import com.myownb3.piranha.core.battle.destruction.DamageImpl;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.battle.destruction.SelfDestructive;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.audio.ProjectileAudioHelper;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.ProjectileStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy.factory.ProjectileStrategyHandlerFactory;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;

public class ProjectileImpl implements Projectile {

   private DestructionHelper destructionHelper;
   private Shape shape;
   private ProjectileStrategyHandler projectileStrategyHandler;
   private ProjectileTypes projectileType;
   private ProjectileAudioHelper projectileAudioHelper;

   protected ProjectileImpl(Shape shape, ProjectileTypes projectileType, ProjectileConfig projectileConfig, double damage, double health,
         OnDestroyedCallbackHandler onDestroyCallbackHandler) {
      this.destructionHelper = getDestructionHelper(damage, health, onDestroyCallbackHandler);
      this.shape = shape;
      this.projectileType = projectileType;
      this.projectileStrategyHandler =
            ProjectileStrategyHandlerFactory.INSTANCE.getProjectileStrategyHandler(projectileType, projectileConfig, shape);
      this.projectileAudioHelper = new ProjectileAudioHelper();
   }

   private DestructionHelper getDestructionHelper(double damage, double health, OnDestroyedCallbackHandler onDestroyCallbackHandler) {
      return DestructionHelperBuilder.builder()
            .withDamage(damage)
            .withHealth(health)
            .withSelfDestructiveDamage(new ProjectileSelfDestructive(health))
            .withOnDestroyedCallbackHandler(onDestroyCallbackHandler)
            .build();
   }

   @Override
   public ProjectileTypes getProjectileType() {
      return projectileType;
   }

   @Override
   public Damage getDamage() {
      return destructionHelper.getDamage();
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
      projectileAudioHelper.playOnCollisionAudio(this, gridElements);
   }

   @Override
   public void autodetect() {
      projectileStrategyHandler.handleProjectileStrategy();
   }

   @Override
   public Shape getShape() {
      return shape;
   }

   private static class ProjectileSelfDestructive implements SelfDestructive {
      private double initialHealth;

      private ProjectileSelfDestructive(double initialHealth) {
         this.initialHealth = initialHealth;
      }

      @Override
      public Damage getDamage(GridElement gridElement) {
         double damageValue = Integer.MAX_VALUE; // A Projectile never survives a collision
         if (isWall(gridElement)) {
            damageValue = initialHealth / 2;// except we collided with a wall
         }
         return DamageImpl.of(damageValue);
      }

      private static boolean isWall(GridElement gridElement) {
         return gridElement instanceof Wall;
      }
   }

   public static class ProjectileBuilder {

      private ProjectileTypes projectileType;
      private double damage;
      private double health;
      private Shape shape;
      private OnDestroyedCallbackHandler onDestroyedCallbackHandler;
      private ProjectileConfig projectileConfig;

      private ProjectileBuilder() {
         this.health = 4;
      }

      public ProjectileBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public ProjectileBuilder withDamage(double damage) {
         this.damage = damage;
         return this;
      }

      public ProjectileBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public ProjectileBuilder withProjectileTypes(ProjectileTypes projectileType) {
         this.projectileType = projectileType;
         return this;
      }

      public ProjectileBuilder withProjectileConfig(ProjectileConfig projectileConfig) {
         this.projectileConfig = projectileConfig;
         return this;
      }

      public ProjectileBuilder withOnDestroyedCallbackHandler(OnDestroyedCallbackHandler onDestroyedCallbackHandler) {
         this.onDestroyedCallbackHandler = onDestroyedCallbackHandler;
         return this;
      }

      public ProjectileImpl build() {
         requireNonNull(projectileType);
         requireNonNull(projectileConfig);
         return new ProjectileImpl(shape, projectileType, projectileConfig, damage, health, onDestroyedCallbackHandler);
      }

      public static ProjectileBuilder builder() {
         return new ProjectileBuilder();
      }

   }
}
