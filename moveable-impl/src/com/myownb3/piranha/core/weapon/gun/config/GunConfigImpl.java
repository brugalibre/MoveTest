package com.myownb3.piranha.core.weapon.gun.config;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;

public class GunConfigImpl implements GunConfig {

   private int salveSize;
   private int roundsPerMinute;
   private ProjectileConfig projectileConfig;

   private GunConfigImpl(int roundsPerMinute, ProjectileConfig projectileConfig, int salveSize) {
      verifyInputs(roundsPerMinute, salveSize);
      this.roundsPerMinute = roundsPerMinute;
      this.projectileConfig = requireNonNull(projectileConfig);
      this.salveSize = salveSize;
   }

   @Override
   public int getRoundsPerMinute() {
      return roundsPerMinute;
   }

   @Override
   public int getSalveSize() {
      return salveSize;
   }

   @Override
   public ProjectileConfig getProjectileConfig() {
      return projectileConfig;
   }

   private static void verifyInputs(int roundsPerMinute, int salveSize) {
      if (salveSize < 1 || roundsPerMinute < 1) {
         throw new IllegalArgumentException("Rounds-per-Minute, the size of the salve must be greater or equal than one!");
      }
   }

   public static class GunConfigBuilder {
      private int salveSize;
      private int roundsPerMinute;
      private ProjectileConfig projectileConfig;

      private GunConfigBuilder() {
         // private
      }

      public GunConfigBuilder withRoundsPerMinute(int roundsPerMinute) {
         this.roundsPerMinute = roundsPerMinute;
         return this;
      }

      public GunConfigBuilder withProjectileConfig(ProjectileConfig projectileConfig) {
         this.projectileConfig = projectileConfig;
         return this;
      }

      public GunConfigBuilder withSalveSize(int salveSize) {
         this.salveSize = salveSize;
         return this;
      }

      public GunConfig build() {
         return new GunConfigImpl(roundsPerMinute, projectileConfig, salveSize);
      }

      public static GunConfigBuilder builder() {
         return new GunConfigBuilder();
      }
   }
}
