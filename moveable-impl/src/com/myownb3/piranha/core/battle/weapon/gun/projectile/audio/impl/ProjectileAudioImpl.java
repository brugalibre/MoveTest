package com.myownb3.piranha.core.battle.weapon.gun.projectile.audio.impl;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.audio.ProjectileAudio;

public class ProjectileAudioImpl implements ProjectileAudio {

   private Optional<AudioClip> projectileDestructionAudioClipOpt;
   private AudioClip projectileImpactAudioClip;
   private Projectile projectile;

   private ProjectileAudioImpl(Projectile projectile, AudioClip projectileImpactAudioClip, AudioClip projectileDestructionAudioClip) {
      this.projectileDestructionAudioClipOpt = Optional.ofNullable(projectileDestructionAudioClip);
      this.projectileImpactAudioClip = projectileImpactAudioClip;
      this.projectile = projectile;
   }

   @Override
   public void playOnCollisionAudio() {
      if (projectile.isDestroyed()) {
         playDestructionAudio();
      } else {
         playImpactAudio();
      }
   }

   @Visible4Testing
   void playImpactAudio() {
      projectileImpactAudioClip.play();
   }

   @Visible4Testing
   void playDestructionAudio() {
      projectileDestructionAudioClipOpt.ifPresent(AudioClip::play);
   }

   public static class ProjectileAudioBuilder {
      private AudioClip projectileDestructionAudioClip;
      private AudioClip projectileImpactAudioClip;
      private Projectile projectile;

      private ProjectileAudioBuilder() {
         // private 
      }

      public ProjectileAudioBuilder withProjectile(Projectile projectile) {
         this.projectile = projectile;
         return this;
      }

      public ProjectileAudioBuilder withProjectileImpactAudioClip() {
         String audioResource = getImpactAudioResource4Type();
         this.projectileImpactAudioClip = AudioClipBuilder.builder()
               .withAudioResource(audioResource)
               .withRestartRunningAudio(false)
               .build();
         return this;
      }

      public ProjectileAudioBuilder withProjectileDestructionAudioClip() {
         String audioResource = getExplosionAudioResource4Type();
         this.projectileDestructionAudioClip = AudioClipBuilder.builder()
               .withAudioResource(audioResource)
               .withRestartRunningAudio(false)
               .build();
         return this;
      }

      public ProjectileAudioImpl build() {
         requireNonNull(projectileImpactAudioClip);
         return new ProjectileAudioImpl(projectile, projectileImpactAudioClip, projectileDestructionAudioClip);
      }

      public static ProjectileAudioBuilder builder() {
         return new ProjectileAudioBuilder();
      }

      private String getExplosionAudioResource4Type() {
         requireNonNull(projectile, "Add first a projectile!");
         switch (projectile.getProjectileType()) {
            case LASER_BEAM: // fall through
            case BULLET:
               return AudioConstants.BULLET_INPACT_SOUND;
            case MISSILE:
               return AudioConstants.EXPLOSION_SOUND;
            default:
               throw new IllegalStateException("Unsupported ProjectileType '" + projectile.getProjectileType() + "'!");
         }
      }

      private String getImpactAudioResource4Type() {
         requireNonNull(projectile, "Add first a projectile!");
         switch (projectile.getProjectileType()) {
            case LASER_BEAM: // fall through
            case BULLET:
               return AudioConstants.BULLET_INPACT_SOUND;
            case MISSILE:
               return AudioConstants.MISSILE_INPACT_SOUND;
            default:
               throw new IllegalStateException("Unsupported ProjectileType '" + projectile.getProjectileType() + "'!");
         }
      }
   }
}
