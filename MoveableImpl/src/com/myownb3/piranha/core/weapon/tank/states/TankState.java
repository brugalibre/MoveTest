package com.myownb3.piranha.core.weapon.tank.states;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.weapon.tank.Tank;

/**
 * Defines the different states a {@link Tank} can be in
 * 
 * @author Dominic
 *
 */
public enum TankState {

   /** The {@link Tank} is being shot by other {@link Belligerent}s */
   UNDER_FIRE,
}
