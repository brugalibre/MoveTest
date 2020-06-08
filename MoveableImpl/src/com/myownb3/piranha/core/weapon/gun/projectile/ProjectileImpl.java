package com.myownb3.piranha.core.weapon.gun.projectile;

import java.util.List;

import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.destruction.SelfDestructive;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

public class ProjectileImpl implements Projectile {

   private DestructionHelper destructionHelper;

   protected ProjectileImpl(double damage, double health, double velocity, OnDestroyedCallbackHandler onDestroyCallbackHandler) {
      this.destructionHelper = getDestructionHelper(damage, health, velocity, onDestroyCallbackHandler);
   }

   private DestructionHelper getDestructionHelper(double damage, double health, double velocity,
         OnDestroyedCallbackHandler onDestroyCallbackHandler) {
      return DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(damage))
            .withHealth(HealthImpl.of(health))
            .withSelfDestructiveDamage(SelfDestructive.of(velocity))
            .withOnDestroyedCallbackHandler(onDestroyCallbackHandler)
            .build();
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
   }

   public static class ProjectileBuilder {

      private double damage;
      private double health;
      private double velocity;
      private OnDestroyedCallbackHandler onDestroyedCallbackHandler;

      private ProjectileBuilder() {
         // private
      }

      public ProjectileBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public ProjectileBuilder withDamage(double damage) {
         this.damage = damage;
         return this;
      }

      public ProjectileBuilder withVelocity(double velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileBuilder withOnDestroyedCallbackHandler(OnDestroyedCallbackHandler onDestroyedCallbackHandler) {
         this.onDestroyedCallbackHandler = onDestroyedCallbackHandler;
         return this;
      }

      public ProjectileImpl build() {
         return new ProjectileImpl(damage, health, velocity, onDestroyedCallbackHandler);
      }

      public static ProjectileBuilder builder() {
         return new ProjectileBuilder();
      }
   }
}