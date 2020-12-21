package com.myownb3.piranha.core.moveables.engine.human;

import com.myownb3.piranha.core.battle.weapon.turret.human.HumanToTurretInteractionCallbackHandler;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link HumanToTurretInteractionCallbackHandler} handles the interaction of a human with a {@link Moveable}
 * 
 * @author Dominic
 *
 */
public interface HumanToEngineInteractionCallbackHandler {

   /**
    * Is triggered when ever the humand indicated the {@link Moveable} to move forward
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanMoveableEngine} has to start <code>false</code> if it has to stop
    * 
    */
   void onForwardPressed(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Moveable} to move backward
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanMoveableEngine} has to start <code>false</code> if it has to stop
    */
   void onBackwardPressed(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Moveable} to turn right
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanMoveableEngine} has to start <code>false</code> if it has to stop
    */
   void onTurnRightPressed(boolean isPressed);

   /**
    * Is triggered when ever the humand indicated the {@link Moveable} to turn left
    * 
    * @param isPressed
    *        <code>true</code> when the {@link HumanMoveableEngine} has to start <code>false</code> if it has to stop
    */
   void onTurnLeftPressed(boolean isPressed);
}
