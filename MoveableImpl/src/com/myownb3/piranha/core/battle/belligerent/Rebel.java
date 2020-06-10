package com.myownb3.piranha.core.battle.belligerent;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyTypes;

public class Rebel implements Belligerent {

   public Rebel() {
      //
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return otherBelligerent.getBelligerentParty().getBelligerentPartyType() == BelligerentPartyTypes.GALACTIC_EMPIRE;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return BelligerentPartyConst.REBEL_ALLIANCE;
   }
}
