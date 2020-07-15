package com.myownb3.piranha.core.battle.weapon.guncarriage.state;

import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

/**
 * The {@link GunCarriageStates} defines the different states of a {@link GunCarriage}
 * 
 * @author Dominic
 *
 */
public enum GunCarriageStates {

   /** No state at all - only for testing purpose */
   NONE,

   /** The Turret is idle and not turning in any direction */
   IDLE,

   /** The {@link Turret} is about to start turning */
   START_TURNING,

   /** The {@link Turret} is turning */
   TURNING,

   /** The {@link Turret} has turned and is now about to end turning */
   END_TURNING,
}
