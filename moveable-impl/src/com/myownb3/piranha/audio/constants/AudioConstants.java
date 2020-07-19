package com.myownb3.piranha.audio.constants;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;

public class AudioConstants {

   private AudioConstants() {
      // private 
   }

   /** Path to the sound file for an explosion */
   public static final String EXPLOSION_SOUND = "audio/explosion.wav";

   /** Path to the sound file for a the rattle of a {@link Turret} when it's about to start rotating */
   public static final String TURRET_START_ROTATE = "audio/turret/turret_rotate_start.wav";

   /** Path to the sound file for a the rattle of a {@link Turret} when it's rotating */
   public static final String TURRET_ROTATE = "audio/turret/turret_rotate.wav";

   /** Path to the sound file for a the rattle of a {@link Turret} when it's end rotating */
   public static final String TURRET_END_ROTATE = "audio/turret/turret_rotate_stop.wav";

   /** Path to the sound file for an engine which is idle */
   public static final String ENGINE_IDLE = "audio/engine/engine_idle.wav";

   /** Path to the sound file for an engine which is accelerating */
   public static final String ENGINE_ACCELERATING = "audio/engine/engine_accelerating.wav";

   /** Path to the sound file for an engine which is idle */
   public static final String ENGINE_SLOWING_DOWN = "audio/engine/engine_slowingdown.wav";

   /** Path to the sound file for an engine which is moving */
   public static final String ENGINE_MOVING = "audio/engine/engine_moving.wav";

   /** Path to the sound file for a the rattle of a {@link Tank}s track */
   public static final String TANK_TRACK_RATTLE = "audio/tank/tank_track_rattle.wav";

   /** Path to the sound file for a the rattle of a {@link Tank}s track */
   public static final String TANK_TRACK_RATTLE_VAR2 = "audio/tank/tank_track_rattle_2.wav";

   /** Path to the sound file when a {@link Turret} shots a projectile of the type {@link ProjectileTypes#LASER_BEAM} */
   public static final String LASER_BEAM_BLAST_SOUND = "audio/projectile/laser_beam_blast.wav";

   /** Path to the sound file when a {@link Turret} shots a projectile of the type {@link ProjectileTypes#BULLET} */
   public static final String BULLET_SHOT_SOUND = "audio/projectile/bullet_shot.wav";

   /** Path to the sound file when {@link Projectile} of the {@link ProjectileTypes#BULLET} hits an any obstacle */
   public static final String BULLET_INPACT_SOUND = "audio/projectile/bullet_hit_ground.wav";

   /** Path to the sound file when a {@link Turret} shots a projectile of the type {@link ProjectileTypes#MISSILE} */
   public static final String MISSILE_SHOT_SOUND = "audio/projectile/missile_shot.wav";

   /** Path to the sound file when {@link Projectile} of the {@link ProjectileTypes#MISSILE} hits an any obstacle */
   public static final String MISSILE_INPACT_SOUND = "audio/projectile/bullet_hit_ground.wav";

}
