package com.myownb3.piranha.application.battle.impl.turret;

import com.myownb3.piranha.core.battle.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;

public class TankBattleApplicationTurretBuilder
      extends GenericTankBattleApplicationTurretBuilder<TurretBuilder, TankBattleApplicationTurretBuilder> {

   private TankBattleApplicationTurretBuilder() {
      // private
   }

   @Override
   protected TankBattleApplicationTurretBuilder getThis() {
      return this;
   }

   @Override
   protected TurretBuilder getTurretBuilder() {
      return TurretBuilder.builder();
   }

   public static TankBattleApplicationTurretBuilder builder() {
      return new TankBattleApplicationTurretBuilder();
   }
}
