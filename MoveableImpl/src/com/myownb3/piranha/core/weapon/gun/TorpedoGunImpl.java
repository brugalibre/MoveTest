package com.myownb3.piranha.core.weapon.gun;

import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;

public class TorpedoGunImpl extends AbstractGun {

   private TorpedoGunImpl(GunShape gunShape, GunConfig gunConfig) {
      super(gunShape, gunConfig);
   }

   @Override
   protected ProjectileTypes getType() {
      return ProjectileTypes.TORPEDO;
   }

   public static class TropedoGunBuilder extends AbstractGunBuilder<TorpedoGunImpl> {

      private TropedoGunBuilder() {
         super();
      }

      @Override
      public TorpedoGunImpl build() {
         return new TorpedoGunImpl(gunShape, gunConfig);
      }

      public static TropedoGunBuilder builder() {
         return new TropedoGunBuilder();
      }
   }
}
