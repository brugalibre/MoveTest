package com.myownb3.piranha.core.weapon.gun.projectile.config;

import com.myownb3.piranha.core.grid.Dimension;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;

public class ProjectileConfigImpl implements ProjectileConfig {

   private Dimension projectileDimension;

   private ProjectileConfigImpl(Dimension projectileDimension) {
      this.projectileDimension = projectileDimension;
   }

   @Override
   public Dimension getProjectileDimension() {
      return projectileDimension;
   }

   public static class ProjectileConfigBuilder {
      private Dimension projectileDimension;

      private ProjectileConfigBuilder() {
         // private
      }

      public ProjectileConfigBuilder withDimension(Dimension projectileDimension) {
         this.projectileDimension = projectileDimension;
         return this;
      }


      public ProjectileConfig build() {
         return new ProjectileConfigImpl(projectileDimension);
      }

      public static ProjectileConfigBuilder builder() {
         return new ProjectileConfigBuilder();
      }
   }
}
