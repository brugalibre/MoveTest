package com.myownb3.piranha.core.weapon.gun.projectile.config;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;

public class ProjectileConfigImpl implements ProjectileConfig {

   private DimensionInfo dimensionInfo;
   private int velocity;
   private double projectileDamage;

   private ProjectileConfigImpl(DimensionInfo dimensionInfo, double projectileDamage, int velocity) {
      this.dimensionInfo = requireNonNull(dimensionInfo);
      this.velocity = verifVelocity(velocity);
      this.projectileDamage = projectileDamage;
   }

   @Override
   public DimensionInfo getDimensionInfo() {
      return dimensionInfo;
   }

   @Override
   public double getProjectileDamage() {
      return projectileDamage;
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
      private int velocity;
      private DimensionInfo dimensionInfo;
      private double projectileDamage;

      private ProjectileConfigBuilder() {
         this.projectileDamage = 10;
      }

      public ProjectileConfigBuilder withDimensionInfo(DimensionInfo dimensionInfo) {
         this.dimensionInfo = dimensionInfo;
         return this;
      }

      public ProjectileConfigBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileConfigBuilder withProjectileDamate(double projectileDamage) {
         this.projectileDamage = projectileDamage;
         return this;
      }

      public ProjectileConfig build() {
         return new ProjectileConfigImpl(dimensionInfo, projectileDamage, velocity);
      }

      public static ProjectileConfigBuilder builder() {
         return new ProjectileConfigBuilder();
      }

   }
}
