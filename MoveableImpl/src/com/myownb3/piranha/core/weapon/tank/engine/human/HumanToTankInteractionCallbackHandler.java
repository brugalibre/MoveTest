package com.myownb3.piranha.core.weapon.tank.engine.human;

import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.human.HumanToTurretInteractionCallbackHandler;

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
   void onForward(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to move backward
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    */
   void onBackward(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to turn right
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    */
   void onTurnRight(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Tank} to turn left
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanTankEngine} has to start <code>false</code> if it has to stop
    */
   void onTurnLeft(boolean isPressed);
}
