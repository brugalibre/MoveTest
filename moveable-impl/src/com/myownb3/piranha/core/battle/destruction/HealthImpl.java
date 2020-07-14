package com.myownb3.piranha.core.battle.destruction;

public class HealthImpl implements Health {

   private double health;

   private HealthImpl(double health) {
      this.health = health;
   }

   @Override
   public void causeDamage(Damage damage) {
      health = health - damage.getDamageValue();
   }

   @Override
   public boolean isHealthy() {
      return health > 0;
   }

   public static HealthImpl of(double health) {
      return new HealthImpl(health);
   }
}
