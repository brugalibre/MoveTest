package com.myownb3.piranha.core.battle.belligerent;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;

public class StroomTrooper implements Belligerent {

   public StroomTrooper() {
      //
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return BelligerentPartyConst.GALACTIC_EMPIRE;
   }
}
