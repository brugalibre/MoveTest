package com.myownb3.piranha.core.battle.weapon.turret.states;

import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * Defines the different states a {@link Turret} can has
 * 
 * @author Dominic
 *
 */
public enum TurretState {

   /** In this state a {@link Turret} is scanning the environment and checking for any target */
   SCANNING,

   /** In this state a {@link Turret} has detected a Target and is evaluating the actual {@link Position} to acquire */
   TARGET_DETECTED,

   /**
    * In this state a {@link Turret} has evaluated a {@link Position} to acquire and is trying to acquiring this very {@link Position} in
    * order to shoot
    */
   ACQUIRING,

   /** A {@link Turret} has successfully acquired a target and is now shooting */
   SHOOTING,

   /** A {@link Turret} returns to it's initial {@link Position} after a target was destroyed or lost */
   RETURNING,

   /** No actual state - only for testing purpose */
   NONE,
}
