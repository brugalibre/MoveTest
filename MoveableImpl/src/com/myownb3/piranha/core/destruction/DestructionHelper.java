package com.myownb3.piranha.core.destruction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link DestructionHelper} is responsible for causing {@link Damage} but is also the one whose {@link Health} is taken
 * {@link Damage}
 * Poor guy actually.. Anyway, as soon as this helper as got more {@link Damage} than it can take it's destroyed.
 * If this happens, a {@link OnDestroyedCallbackHandler} is called for further actions
 * 
 * @author Dominic
 *
 */
public class DestructionHelper implements Destructible, Destructive, CollisionSensitive {

   private Health health;
   private Damage damage;
   private Destructive selfDestructiveDamage;
   private OnDestroyedCallbackHandler callbackHandler;

   private DestructionHelper(Health health, Damage damage, Destructive selfDestructiveDamage, OnDestroyedCallbackHandler callbackHandler) {
      this.health = health;
      this.damage = damage;
      this.selfDestructiveDamage = selfDestructiveDamage;
      this.callbackHandler = callbackHandler;
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {

      applyDamageFromDestructives(gridElements);
      applySelfCausedDamage(gridElements);

      if (isDestroyed()) {
         callbackHandler.onDestroy();
      }
   }

   private void applySelfCausedDamage(List<GridElement> gridElements) {
      Predicate<? super GridElement> isDestructive = Destructive.class::isInstance;
      gridElements.stream()
            .filter(isDestructive.negate())
            .map(map2SelfDestructiveDamage())
            .map(Destructive::getDamage)
            .forEach(health::causeDamage);
   }

   private void applyDamageFromDestructives(List<GridElement> gridElements) {
      gridElements.stream()
            .filter(Destructive.class::isInstance)
            .map(Destructive.class::cast)
            .map(Destructive::getDamage)
            .forEach(health::causeDamage);
   }

   @Override
   public Damage getDamage() {
      return damage;
   }

   @Override
   public boolean isDestroyed() {
      return !health.isHealthy();
   }

   private Function<? super GridElement, ? extends Destructive> map2SelfDestructiveDamage() {
      return gridElement -> this.selfDestructiveDamage;
   }

   public static class DestructionHelperBuilder {
      private Health health;
      private Damage damage;
      private Destructive selfDestructiveDamage;
      private OnDestroyedCallbackHandler callbackHandler;

      private DestructionHelperBuilder() {
         // private
      }

      public DestructionHelperBuilder withHealth(Health health) {
         this.health = health;
         return this;
      }

      public DestructionHelperBuilder withSelfDestructiveDamage(Destructive selfDestructiveDamage) {
         this.selfDestructiveDamage = selfDestructiveDamage;
         return this;
      }

      public DestructionHelperBuilder withDamage(Damage damage) {
         this.damage = damage;
         return this;
      }

      public DestructionHelperBuilder withOnDestroyedCallbackHandler(OnDestroyedCallbackHandler destroyedCallbackHandler) {
         this.callbackHandler = destroyedCallbackHandler;
         return this;
      }

      public DestructionHelper build() {
         return new DestructionHelper(health, damage, selfDestructiveDamage, callbackHandler);
      }

      public static DestructionHelperBuilder builder() {
         return new DestructionHelperBuilder();
      }
   }
}