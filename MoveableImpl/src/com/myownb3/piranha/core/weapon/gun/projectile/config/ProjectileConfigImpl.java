package com.myownb3.piranha.core.weapon.gun.projectile.config;

import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;

public class ProjectileConfigImpl implements ProjectileConfig {

   private Dimension projectileDimension;
   private int velocity;

   private ProjectileConfigImpl(Dimension projectileDimension, int velocity) {
      this.projectileDimension = projectileDimension;
      this.velocity = verifVelocity(velocity);
   }

   @Override
   public Dimension getProjectileDimension() {
      return projectileDimension;
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
      private int velocity;

      private ProjectileConfigBuilder() {
         // private
      }

      public ProjectileConfigBuilder withDimension(Dimension projectileDimension) {
         this.projectileDimension = projectileDimension;
         return this;
      }

      public ProjectileConfigBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileConfig build() {
         return new ProjectileConfigImpl(projectileDimension, velocity);
      }

      public static ProjectileConfigBuilder builder() {
         return new ProjectileConfigBuilder();
      }
   }
}
