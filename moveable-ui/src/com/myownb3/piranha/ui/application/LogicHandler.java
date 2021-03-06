package com.myownb3.piranha.ui.application;

import com.myownb3.piranha.core.battle.weapon.gun.OnGunFireListener;
import com.myownb3.piranha.core.grid.OnGridElementAddListener;

/**
 * Is responsible for executing the main game logic
 * 
 * @author Dominic
 *
 */
public interface LogicHandler extends OnGridElementAddListener, OnGunFireListener {

   /**
    * executes the game logic for the current cycle
    */
   void onCylce();

}
