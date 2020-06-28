package com.myownb3.piranha.core.weapon.turret.human;

import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * The {@link HumanToTurretInteractionCallbackHandler} handles the interaction of a human with a {@link Turret}
 * 
 * @author Dominic
 *
 */
public interface HumanToTurretInteractionCallbackHandler {

   /**
    * Is triggered when ever the humand indicated the {@link Turret} hit the trigger one time
    */
   void onSingleShotFired();

   /**
    * Is triggered when ever the humand indicated the {@link Turret} start fire
    * 
    * @param startOrStop
    *        <code>true</code> whenn the user starts fire and <code>false</code> if he want to stop
    */
   void onStartFire(boolean startOrStop);

   /**
    * Is called when ever the humand indicated the {@link Turret} to change the direction of the {@link Gun}
    * 
    * @param turretPos
    */
   void onTurretTurned(Position turretPos);

}
