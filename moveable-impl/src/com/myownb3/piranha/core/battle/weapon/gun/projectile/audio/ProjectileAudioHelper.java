package com.myownb3.piranha.core.battle.weapon.gun.projectile.audio;

import java.util.List;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link ProjectileAudioHelper} is responsible for choosing the right {@link AudioClip} when a {@link Projectile}
 * has a collision or is destroyed
 * 
 * @author Dominic
 *
 */
public class ProjectileAudioHelper {

   /**
    * Depending on the given {@link GridElement} with which the {@link Projectile} was collided, a audio is played
    * 
    * @param gridElementsCollidedWith
    */
   public void playOnCollisionAudio(Projectile projectile, List<GridElement> gridElementsCollidedWith) {
      switch (projectile.getProjectileType()) {
         case BULLET:
            playBulletHitsObstacleAudio(gridElementsCollidedWith);
            break;
         case LASER_BEAM:
            playBulletHitsObstacleAudio(gridElementsCollidedWith);
            break;
         case MISSILE:
            playMissileHitsObstacleAudio(projectile);
            break;
         default:
            throw new IllegalStateException("Unknown ProjectileTypes '" + projectile.getProjectileType() + "'!");
      }
   }

   private void playBulletHitsObstacleAudio(List<GridElement> gridElementsCollidedWith) {
      long amountOfGridElements = gridElementsCollidedWith.stream()
            .count();
      for (int i = 0; i < amountOfGridElements; i++) {
         buildAndPlayAudio(AudioConstants.BULLET_INPACT_SOUND);
      }
   }

   private void playMissileHitsObstacleAudio(Projectile projectile) {
      if (projectile.isDestroyed()) {
         playDefaultExplosion();
      } else {
         buildAndPlayAudio(AudioConstants.MISSILE_INPACT_SOUND);
      }
   }

   @Visible4Testing
   void playDefaultExplosion() {
      AudioClipBuilder.builder()
            .withAudioResource(AudioConstants.EXPLOSION_SOUND)
            .build()
            .play();
   }

   @Visible4Testing
   void buildAndPlayAudio(String resource) {
      AudioClipBuilder.builder()
            .withAudioResource(resource)
            .build()
            .play();
   }
}
