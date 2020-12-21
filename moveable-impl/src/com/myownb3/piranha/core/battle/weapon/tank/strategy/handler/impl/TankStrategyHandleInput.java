package com.myownb3.piranha.core.battle.weapon.tank.strategy.handler.impl;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.moveables.engine.MoveableEngine;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

/**
 * Defines the necessary parameters for handling a {@link TankStrategy}
 * 
 * @author Dominic
 *
 */
public class TankStrategyHandleInput {

   private MoveableEngine moveableEngine;
   private TankDetector tankDetector;
   private Turret turret;

   private TankStrategyHandleInput(Turret turret, MoveableEngine moveableEngine, TankDetector tankDetector) {
      this.turret = requireNonNull(turret, "A TankStrategyHandlerInput always needs a Turret!");
      this.moveableEngine = requireNonNull(moveableEngine, "A TankStrategyHandlerInput always needs a MoveableEngine!");
      this.tankDetector = tankDetector;
   }

   /**
    * 
    * @return the {@link MoveableEngine} of this input
    */
   public MoveableEngine getMoveableEngine() {
      return moveableEngine;
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

   public static TankStrategyHandleInput of(Turret turret, MoveableEngine moveableEngine, TankDetector tankDetector) {
      return new TankStrategyHandleInput(turret, moveableEngine, tankDetector);
   }
}
