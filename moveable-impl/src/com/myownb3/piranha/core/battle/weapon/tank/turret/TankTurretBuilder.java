package com.myownb3.piranha.core.battle.weapon.tank.turret;

import com.myownb3.piranha.core.battle.weapon.turret.TurretImpl.GenericTurretBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.TurretStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.TankAutoAimAndShootTurretStrategyHandler;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.TurretStrategyHandleInput;

public class TankTurretBuilder extends GenericTurretBuilder<TankTurretBuilder> {
   @FunctionalInterface
   public interface ParkingAngleEvaluator {
      double evaluateParkingAngle();
   }

   private ParkingAngleEvaluator parkingAngleEvaluator;

   protected TankTurretBuilder() {
      super();
   }

   public TankTurretBuilder withParkingAngleEvaluator(ParkingAngleEvaluator parkingAngleEvaluator) {
      this.parkingAngleEvaluator = parkingAngleEvaluator;
      return this;
   }

   public static TankTurretBuilder builder() {
      return new TankTurretBuilder();
   }

   @Override
   protected TurretStrategyHandler buildTurretStrategyHandlerHandler(TurretShape turretShape) {
      return new TankAutoAimAndShootTurretStrategyHandler(
            TurretStrategyHandleInput.of(gunCarriage, turretScanner, turretShape, parkingAngleEvaluator));
   }

   @Override
   protected TankTurretBuilder getThis() {
      return this;
   }
}
