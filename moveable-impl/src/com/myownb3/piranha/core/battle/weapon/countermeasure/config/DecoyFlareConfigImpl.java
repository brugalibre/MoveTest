package com.myownb3.piranha.core.battle.weapon.countermeasure.config;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.util.MathUtil;

public class DecoyFlareConfigImpl extends ProjectileConfigImpl implements DecoyFlareConfig {

   private double decoyFlareSpreadAngle;
   private int amountDecoyFlares;
   private BelligerentParty belligerentParty;
   private int decoyFlareTimeToLife;

   public DecoyFlareConfigImpl(DimensionInfo dimensionInfo, double projectileDamage, TargetGridElementEvaluator targetGridElementEvaluator,
         int velocity, double missileRotationSpeed) {
      super(dimensionInfo, projectileDamage, targetGridElementEvaluator, velocity, missileRotationSpeed);
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return belligerentParty;
   }

   @Override
   public int getAmountDecoyFlares() {
      return amountDecoyFlares;
   }

   @Override
   public double getDecoyFlareSpreadAngle() {
      return decoyFlareSpreadAngle;
   }

   @Override
   public int getDecoyFlareTimeToLife(int offset) {
      return (int) (MathUtil.getRandom(offset) + decoyFlareTimeToLife);
   }

   @Override
   public int getDecoyFlareTimeToLife() {
      return decoyFlareTimeToLife;
   }

   public static class DecoyFlareConfigBuilder extends AbstractProjectileConfigBuilder<DecoyFlareConfigImpl, DecoyFlareConfigBuilder> {

      private double decoyFlareSpreadAngle;
      private int amountDecoyFlares;
      private BelligerentParty belligerentParty;
      private int decoyFlareTimeToLife;

      public DecoyFlareConfigBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
         return this;
      }

      public DecoyFlareConfigBuilder withAmountDecoyFlares(int amountDecoyFlares) {
         this.amountDecoyFlares = amountDecoyFlares;
         return this;
      }

      public DecoyFlareConfigBuilder withDecoyFlareTimeToLife(int decoyFlareTimeToLife) {
         this.decoyFlareTimeToLife = decoyFlareTimeToLife;
         return this;
      }

      public DecoyFlareConfigBuilder withDecoyFlareSpreadAngle(double decoyFlareSpreadAngle) {
         this.decoyFlareSpreadAngle = decoyFlareSpreadAngle;
         return this;
      }

      public static DecoyFlareConfigBuilder builder() {
         return new DecoyFlareConfigBuilder();
      }

      public DecoyFlareConfigImpl build() {
         DecoyFlareConfigImpl decoyFlareConfigImpl = new DecoyFlareConfigImpl(dimensionInfo, projectileDamage,
               targetGridElementEvaluator, velocity, missileRotationSpeed);
         decoyFlareConfigImpl.decoyFlareSpreadAngle = decoyFlareSpreadAngle;
         decoyFlareConfigImpl.amountDecoyFlares = amountDecoyFlares;
         decoyFlareConfigImpl.belligerentParty = belligerentParty;
         decoyFlareConfigImpl.decoyFlareTimeToLife = decoyFlareTimeToLife;
         return decoyFlareConfigImpl;
      }

      @Override
      protected DecoyFlareConfigBuilder getThis() {
         return this;
      }
   }
}
