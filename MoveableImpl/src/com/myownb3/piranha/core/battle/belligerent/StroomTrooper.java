package com.myownb3.piranha.core.battle.belligerent;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyTypes;

public class StroomTrooper implements Belligerent {

   public StroomTrooper() {
      //
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return otherBelligerent.getBelligerentParty().getBelligerentPartyType() == BelligerentPartyTypes.REBEL_ALLIANCE;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return BelligerentPartyConst.GALACTIC_EMPIRE;
   }
}
