package com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.destruction.Destructive;
import com.myownb3.piranha.core.weapon.AutoDetectable;

/**
 * Represents a T-Fighter of the Galactic empire
 * 
 * @author Dominic
 *
 */
public interface TFighter extends Belligerent, Destructible, Destructive, AutoDetectable {

   /**
    * @return the shape of this {@link TFighter}
    */
   TIEFighterShape getShape();
}
