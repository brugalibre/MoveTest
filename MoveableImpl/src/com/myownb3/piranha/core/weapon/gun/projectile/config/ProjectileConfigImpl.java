package com.myownb3.piranha.core.weapon.gun.projectile.config;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;

public class ProjectileConfigImpl implements ProjectileConfig {

   private Dimension projectileDimension;
   private BelligerentParty belligerentParty;

   private ProjectileConfigImpl(Dimension projectileDimension, BelligerentParty belligerentParty) {
      this.projectileDimension = projectileDimension;
      this.belligerentParty = belligerentParty;
   }

   @Override
   public Dimension getProjectileDimension() {
      return projectileDimension;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   public static class ProjectileConfigBuilder {
      private Dimension projectileDimension;
      private BelligerentParty belligerentParty;

      private ProjectileConfigBuilder() {
         belligerentParty = BelligerentPartyConst.REBEL_ALLIANCE;
      }

      public ProjectileConfigBuilder withDimension(Dimension projectileDimension) {
         this.projectileDimension = projectileDimension;
         return this;
      }

      public ProjectileConfigBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public ProjectileConfig build() {
         return new ProjectileConfigImpl(projectileDimension, belligerentParty);
      }

      public static ProjectileConfigBuilder builder() {
         return new ProjectileConfigBuilder();
      }
   }
}
