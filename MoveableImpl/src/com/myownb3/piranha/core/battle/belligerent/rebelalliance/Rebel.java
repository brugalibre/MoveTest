package com.myownb3.piranha.core.battle.belligerent.rebelalliance;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;

public class Rebel implements Belligerent {

   public Rebel() {
      //
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return BelligerentPartyConst.REBEL_ALLIANCE;
   }
}
