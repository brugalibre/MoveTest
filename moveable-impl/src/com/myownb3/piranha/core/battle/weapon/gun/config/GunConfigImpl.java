package com.myownb3.piranha.core.battle.weapon.gun.config;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;

public class GunConfigImpl implements GunConfig {

   private int salveSize;
   private int roundsPerMinute;
   private ProjectileConfig projectileConfig;
   private AudioClip audioClip;

   private GunConfigImpl(int roundsPerMinute, ProjectileConfig projectileConfig, int salveSize, AudioClip audioClip) {
      verifyInputs(roundsPerMinute, salveSize);
      this.roundsPerMinute = roundsPerMinute;
      this.projectileConfig = requireNonNull(projectileConfig);
      this.salveSize = salveSize;
      this.audioClip = audioClip;
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

   @Override
   public AudioClip getAudioClip() {
      return audioClip;
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
      private AudioClip audioClip;

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

      public GunConfigBuilder withAudioClip(AudioClip audioClip) {
         this.audioClip = audioClip;
         return this;
      }

      public GunConfigBuilder withSalveSize(int salveSize) {
         this.salveSize = salveSize;
         return this;
      }

      public GunConfig build() {
         return new GunConfigImpl(roundsPerMinute, projectileConfig, salveSize, audioClip);
      }

      public static GunConfigBuilder builder() {
         return new GunConfigBuilder();
      }
   }
}
