package com.myownb3.piranha.core.battle.belligerent.party;

import java.util.Collections;

public class BelligerentPartyConst {

   private BelligerentPartyConst() {
      // non instantiable
   }

   /**
    * The {@link GalacticEmpire} which rules over the galaxy
    */
   public static final BelligerentParty GALACTIC_EMPIRE =
         BelligerentPartyImpl.of(Collections.singletonList(BelligerentPartyTypes.REBEL_ALLIANCE), BelligerentPartyTypes.GALACTIC_EMPIRE);
   /**
    * The {@link RebelAlliance} which rules over the galaxy
    */
   public static final BelligerentParty REBEL_ALLIANCE =
         BelligerentPartyImpl.of(Collections.singletonList(BelligerentPartyTypes.GALACTIC_EMPIRE), BelligerentPartyTypes.REBEL_ALLIANCE);
}
