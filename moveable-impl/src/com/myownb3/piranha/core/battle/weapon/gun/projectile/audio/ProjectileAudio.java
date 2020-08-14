package com.myownb3.piranha.core.battle.weapon.gun.projectile.audio;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;

/**
 * The {@link ProjectileAudio} represents the audio logic for a {@link ProjectileAudio}
 * 
 * @author DStalder
 *
 */
public interface ProjectileAudio {

   /**
    * Plays the sound depending on the {@link Projectile} and whether it's destroyed or not
    */
   void playOnCollisionAudio();
}
