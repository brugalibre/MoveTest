package com.myownb3.piranha.core.weapon.turret.strategy.handler.impl.human.firemode;

import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * Defines the different fire modes for a human {@link Turret}
 * 
 * @author Dominic
 *
 */
public enum ShootMode {

   /** Indicates not to shot */
   DONT_SHOOT,

   /** indicates to shot one time */
   ONE_SHOT,

   /** Indicate to start shooting until it has to stop */
   KEEP_SHOOTING;
}
