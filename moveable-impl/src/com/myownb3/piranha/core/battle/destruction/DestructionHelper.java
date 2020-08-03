package com.myownb3.piranha.core.battle.destruction;

import java.util.List;
import java.util.function.Function;

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
   private SelfDestructive selfDestructive;
   private OnDestroyedCallbackHandler callbackHandler;

   private DestructionHelper(Health health, Damage damage, SelfDestructive selfDestructive, OnDestroyedCallbackHandler callbackHandler) {
      this.health = health;
      this.damage = damage;
      this.selfDestructive = selfDestructive;
      this.callbackHandler = callbackHandler;// can be null for objects which are not destroyable
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
      gridElements.stream()
            .map(map2SelfDestructiveDamage())
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

   private Function<? super GridElement, ? extends Damage> map2SelfDestructiveDamage() {
      return gridElement -> this.selfDestructive.getDamage(gridElement);
   }

   /**
    * @param gridElement
    *        the {@link GridElement} to test
    * @return <code>true</code> if the given {@link GridElement} is alive or <code>false</code> if it's destroyed
    * 
    */
   public static boolean isNotDestroyed(GridElement gridElement) {
      return !(gridElement instanceof Destructible) || !((Destructible) gridElement).isDestroyed();
   }

   /**
    * @param gridElement
    *        the {@link GridElement} to test
    * @return <code>true</code> if the given {@link GridElement} is destroyed or <code>false</code> if it's still a live
    * 
    */
   public static boolean isDestroyed(GridElement gridElement) {
      return !isNotDestroyed(gridElement);
   }

   public static class DestructionHelperBuilder {
      private Health health;
      private Damage damage;
      private SelfDestructive selfDestructiveDamage;
      private OnDestroyedCallbackHandler callbackHandler;

      private DestructionHelperBuilder() {
         // private
      }

      public DestructionHelperBuilder withHealth(double healthValue) {
         this.health = HealthImpl.of(healthValue);
         return this;
      }

      public DestructionHelperBuilder withHealth(Health health) {
         this.health = health;
         return this;
      }

      public DestructionHelperBuilder withSelfDestructiveDamage(SelfDestructive selfDestructive) {
         this.selfDestructiveDamage = selfDestructive;
         return this;
      }

      public DestructionHelperBuilder withSelfDestructiveDamage(double velocity) {
         this.selfDestructiveDamage = DefaultSelfDestructiveImpl.of(velocity);
         return this;
      }

      public DestructionHelperBuilder withDamage(double damageValue) {
         this.damage = DamageImpl.of(damageValue);
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
