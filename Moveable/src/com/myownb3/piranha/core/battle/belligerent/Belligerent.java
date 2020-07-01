package com.myownb3.piranha.core.battle.belligerent;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * The {@link Belligerent} has an active part in a combat. It defines a person, a state or even a {@link Turret} or {@link Tank} which
 * participate in a battle or war
 * 
 * @author Dominic
 *
 */
public interface Belligerent {

   /**
    * @param otherBelligerent
    *        the other {@link Belligerent}
    * @return <code>true</code> if the other {@link Belligerent} is an enemy or <code>false</code> if it's an allied
    * 
    */
   default boolean isEnemy(Belligerent otherBelligerent) {
      return getBelligerentParty().isEnemyParty(otherBelligerent.getBelligerentParty());
   }

   /**
    * 
    * @return the {@link BelligerentParty} of this {@link Belligerent}
    */
   BelligerentParty getBelligerentParty();
}
