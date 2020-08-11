package com.myownb3.piranha.application.battle.impl.turret;

import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder.ParkingAngleEvaluator;

public class TankBattleApplicationTankTurretBuilder
      extends GenericTankBattleApplicationTurretBuilder<TankTurretBuilder, TankBattleApplicationTankTurretBuilder> {

   private ParkingAngleEvaluator parkingAngleEvaluator;

   public TankBattleApplicationTankTurretBuilder withParkingAngleEvaluator(ParkingAngleEvaluator parkingAngleEvaluator) {
      this.parkingAngleEvaluator = parkingAngleEvaluator;
      return this;
   }

   @Override
   protected TankTurretBuilder getTurretBuilder() {
      return TankTurretBuilder.builder()
            .withParkingAngleEvaluator(parkingAngleEvaluator);
   }

   private TankBattleApplicationTankTurretBuilder() {
      // private
   }

   @Override
   protected TankBattleApplicationTankTurretBuilder getThis() {
      return this;
   }

   public static TankBattleApplicationTankTurretBuilder builder() {
      return new TankBattleApplicationTankTurretBuilder();
   }
}
