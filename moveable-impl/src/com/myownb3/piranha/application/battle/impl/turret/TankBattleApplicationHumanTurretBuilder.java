package com.myownb3.piranha.application.battle.impl.turret;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

public class TankBattleApplicationHumanTurretBuilder
      extends GenericTankBattleApplicationTurretBuilder<TankTurretBuilder, TankBattleApplicationHumanTurretBuilder> {

   private GunCarriage gunCarriage;

   private TankBattleApplicationHumanTurretBuilder() {
      // private
   }

   public TankBattleApplicationHumanTurretBuilder withGunCarriage(GunCarriage gunCarriage) {
      this.gunCarriage = gunCarriage;
      return getThis();
   }

   @Override
   public Turret build() {
      requireNonNull(gunCarriage);
      return getTurretBuilder()
            .withBelligerentParty(belligerentParty)
            .withPositionTransformator(positionTransformator)
            .withDestructionHelper(destructionHelper)
            .withTurretStrategyHandler(turretStrategyHandler)
            .withGridElementEvaluator(getGridElementEvaluator())
            .withGunCarriage(gunCarriage)
            .build();
   }

   @Override
   protected TankTurretBuilder getTurretBuilder() {
      return TankTurretBuilder.builder();
   }

   @Override
   protected TankBattleApplicationHumanTurretBuilder getThis() {
      return this;
   }

   public static TankBattleApplicationHumanTurretBuilder builder() {
      return new TankBattleApplicationHumanTurretBuilder();
   }
}
