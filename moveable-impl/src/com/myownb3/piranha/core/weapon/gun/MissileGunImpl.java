package com.myownb3.piranha.core.weapon.gun;

import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;

public class MissileGunImpl extends AbstractGun {

   private MissileGunImpl(GunShape gunShape, GunConfig gunConfig) {
      super(gunShape, gunConfig);
   }

   @Override
   protected ProjectileTypes getType() {
      return ProjectileTypes.MISSILE;
   }

   public static class MissileGunBuilder extends AbstractGunBuilder<MissileGunImpl> {

      private MissileGunBuilder() {
         super();
      }

      @Override
      public MissileGunImpl build() {
         return new MissileGunImpl(gunShape, gunConfig);
      }

      public static MissileGunBuilder builder() {
         return new MissileGunBuilder();
      }
   }
}
