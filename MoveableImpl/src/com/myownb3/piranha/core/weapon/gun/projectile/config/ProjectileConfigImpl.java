package com.myownb3.piranha.core.weapon.gun.projectile.config;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;

public class ProjectileConfigImpl implements ProjectileConfig {

   private Dimension projectileDimension;
   private BelligerentParty belligerentParty;
   private int velocity;

   private ProjectileConfigImpl(Dimension projectileDimension, BelligerentParty belligerentParty, int velocity) {
      this.projectileDimension = projectileDimension;
      this.belligerentParty = belligerentParty;
      this.velocity = verifVelocity(velocity);
   }

   @Override
   public Dimension getProjectileDimension() {
      return projectileDimension;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public int getVelocity() {
      return velocity;
   }

   private static int verifVelocity(int velocity) {
      if (velocity < 1) {
         throw new IllegalArgumentException("The velocity must be greater or equal than one!");
      }
      return velocity;
   }

   public static class ProjectileConfigBuilder {
      private Dimension projectileDimension;
      private BelligerentParty belligerentParty;
      private int velocity;

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

      public ProjectileConfigBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileConfig build() {
         return new ProjectileConfigImpl(projectileDimension, belligerentParty, velocity);
      }

      public static ProjectileConfigBuilder builder() {
         return new ProjectileConfigBuilder();
      }
   }
}
