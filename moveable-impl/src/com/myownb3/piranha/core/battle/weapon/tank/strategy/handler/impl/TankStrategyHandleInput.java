package com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

/**
 * Defines the necessary parameters for handling a {@link TankStrategy}
 * 
 * @author Dominic
 *
 */
public class TankStrategyHandleInput {

   private TankEngine tankEngine;
   private TankDetector tankDetector;
   private Turret turret;

   private TankStrategyHandleInput(Turret turret, TankEngine tankEngine, TankDetector tankDetector) {
      this.turret = requireNonNull(turret, "A TankStrategyHandlerInput always needs a Turret!");
      this.tankEngine = requireNonNull(tankEngine, "A TankStrategyHandlerInput always needs a TankEngine!");
      this.tankDetector = tankDetector;
   }

   /**
    * 
    * @return the {@link TankEngine} of this input
    */
   public TankEngine getTankEngine() {
      return tankEngine;
   }

   /**
    * 
    * @return the {@link TankDetector} of this input
    */
   public TankDetector getTankDetector() {
      return tankDetector;
   }

   /**
    * 
    * @return the {@link Turret} of this input
    */
   public Turret getTurret() {
      return turret;
   }

   public static TankStrategyHandleInput of(Turret turret, TankEngine tankEngine, TankDetector tankDetector) {
      return new TankStrategyHandleInput(turret, tankEngine, tankDetector);
   }
}
