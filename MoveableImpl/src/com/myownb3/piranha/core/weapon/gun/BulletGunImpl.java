package com.myownb3.piranha.core.weapon.gun;

import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;

public class BulletGunImpl extends AbstractGun {

   private BulletGunImpl(Rectangle rectangle, GunConfig gunConfig) {
      super(rectangle, gunConfig);
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
         return new BulletGunImpl(rectangle, gunConfig);
      }

      public static BulletGunBuilder builder() {
         return new BulletGunBuilder();
      }
   }
}
