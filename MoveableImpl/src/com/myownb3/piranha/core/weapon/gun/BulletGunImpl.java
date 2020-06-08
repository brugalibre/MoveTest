package com.myownb3.piranha.core.weapon.gun;

import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;

public class BulletGunImpl extends AbstractGun {

   private BulletGunImpl(GunShape gunShape, GunConfig gunConfig) {
      super(gunShape, gunConfig);
   }

   @Override
   protected ProjectileTypes getType() {
      return ProjectileTypes.BULLET;
   }

   public static class BulletGunBuilder extends AbstractGunBuilder<BulletGunImpl> {

      private BulletGunBuilder() {
         super();
      }

      @Override
      public BulletGunImpl build() {
         return new BulletGunImpl(gunShape, gunConfig);
      }

      public static BulletGunBuilder builder() {
         return new BulletGunBuilder();
      }
   }
}
