package com.myownb3.piranha.core.battle.weapon.gun;

import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link OnGunFireListener} listens and act as soon as a {@link Gun} fires
 * 
 * @author Dominic
 *
 */
public interface OnGunFireListener {

   /**
    * is invoked as soon as the {@link Gun} has fired
    * 
    * @param furthermostGunPos
    *        the furthermost Position from which the gun has fired
    */
   void onFire(Position furthermostGunPos);

}
