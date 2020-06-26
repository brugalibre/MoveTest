package com.myownb3.piranha.core.weapon.tank.strategy;

import com.myownb3.piranha.core.weapon.tank.Tank;

/**
 * The {@link TankStrategy} defines the strategy of a {@link Tank} during the battle
 * 
 * @author Dominic
 *
 */
public enum TankStrategy {

   /**
    * With this {@link TankStrategy} a {@link Tank} is under the controll of a human being
    */
   HUMAN_CONTROLLED,

   /**
    * With this {@link TankStrategy} a {@link Tank} whites while it is shooting except it's under fire itself
    */
   WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE,

   /**
    * With this {@link TankStrategy} the {@link Tank} always moves forward regardles if it's shooting or not
    */
   ALWAYS_MOVE_AND_SHOOT,

   /** For testing purpose only */
   NONE,
}
