package com.myownb3.piranha.core.weapon.turret.strategy.handler;

import com.myownb3.piranha.core.weapon.turret.states.TurretState;

/**
 * The {@link TurretStrategyHandler} defines the interface for implementing the different turret strategies
 * 
 * @author Dominic
 *
 */
public interface TurretStrategyHandler {

   /**
    * Handles a specific turret strategy
    */
   void handleTankStrategy();

   /**
    * 
    * @return the {@link TurretState} of this {@link TurretStrategyHandler}
    */
   TurretState getTurretStatus();
}
