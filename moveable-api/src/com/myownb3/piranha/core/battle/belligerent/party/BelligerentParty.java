package com.myownb3.piranha.core.battle.belligerent.party;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;

/**
 * The {@link BelligerentParty} defines a belligerent party in a battle or war
 * A {@link BelligerentParty} consist of one or more {@link Belligerent}s
 * 
 * @author Dominic
 *
 */
public interface BelligerentParty {

   /**
    * @param belligerentParty
    *        the other {@link BelligerentParty}
    * @return <code>true</code> if the other {@link BelligerentParty} is hostile to this one or <code>false</code> if not
    */
   boolean isEnemyParty(BelligerentParty belligerentParty);

   /**
    * @return the type of this {@link BelligerentParty}
    */
   BelligerentPartyTypes getBelligerentPartyType();
}
