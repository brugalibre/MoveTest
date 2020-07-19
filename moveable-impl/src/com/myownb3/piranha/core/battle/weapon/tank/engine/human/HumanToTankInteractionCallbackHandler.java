package com.myownb3.piranha.core.battle.weapon.tank.engine.human;

import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.human.HumanToTurretInteractionCallbackHandler;

/**
 * The {@link HumanToTurretInteractionCallbackHandler} handles the interaction of a human with a {@link Turret}
 * 
 * @author Dominic
 *
 */
public interface HumanToTankInteractionCallbackHandler {

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to move forward
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    * 
    */
   void onForwardPressed(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to move backward
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    */
   void onBackwardPressed(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to turn right
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    */
   void onTurnRightPressed(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to turn left
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    */
   void onTurnLeftPressed(boolean isPressed);
}
