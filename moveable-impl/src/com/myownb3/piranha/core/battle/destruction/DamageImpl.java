package com.myownb3.piranha.core.battle.destruction;

public class DamageImpl implements Damage {

   private double damage;

   private DamageImpl(double damage) {
      this.damage = damage;
   }

   @Override
   public double getDamageValue() {
      return damage;
   }

   public static DamageImpl of(double damage) {
      return new DamageImpl(damage);
   }
}
