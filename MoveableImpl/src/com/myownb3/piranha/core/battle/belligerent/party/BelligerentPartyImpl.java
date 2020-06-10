package com.myownb3.piranha.core.battle.belligerent.party;

import java.util.List;

public class BelligerentPartyImpl implements BelligerentParty {

   private List<BelligerentPartyTypes> hostileParties;
   private BelligerentPartyTypes belligerentPartyType;

   private BelligerentPartyImpl(List<BelligerentPartyTypes> hostileParties, BelligerentPartyTypes belligerentPartyType) {
      this.hostileParties = hostileParties;
      this.belligerentPartyType = belligerentPartyType;
   }

   @Override
   public boolean isEnemyParty(BelligerentParty belligerentParty) {
      return hostileParties.contains(belligerentParty.getBelligerentPartyType());
   }

   @Override
   public BelligerentPartyTypes getBelligerentPartyType() {
      return belligerentPartyType;
   }

   public static BelligerentParty of(List<BelligerentPartyTypes> hostileParties, BelligerentPartyTypes belligerentPartyType) {
      return new BelligerentPartyImpl(hostileParties, belligerentPartyType);
   }
}
