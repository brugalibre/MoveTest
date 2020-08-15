package com.myownb3.piranha.core.battle.weapon.gun;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;

public class DefaultGunImpl extends AbstractGun {

   private static final long serialVersionUID = 4747279825575737624L;

   private DefaultGunImpl(GunShape gunShape, GunConfig gunConfig, ProjectileTypes projectileType, List<OnGunFireListener> onGunFireListeners) {
      super(gunShape, gunConfig, projectileType, onGunFireListeners);
   }

   public static class DefaultGunBuilder extends AbstractGunBuilder<DefaultGunImpl> {

      private DefaultGunBuilder() {
         super();
      }

      @Override
      public DefaultGunImpl build() {
         requireNonNull(gunShape);
         requireNonNull(gunConfig);
         requireNonNull(projectileType);
         return new DefaultGunImpl(gunShape, gunConfig, projectileType, onGunFireListeners);
      }

      public static DefaultGunBuilder builder() {
         return new DefaultGunBuilder();
      }
   }
}
