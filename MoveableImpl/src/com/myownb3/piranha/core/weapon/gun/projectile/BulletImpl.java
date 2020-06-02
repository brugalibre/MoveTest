package com.myownb3.piranha.core.weapon.gun.projectile;

import java.util.UUID;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class BulletImpl extends AbstracProjectile {

   private static final int MAX_COLLISIONS = 2;

   private BulletImpl(Grid grid, Position position, Shape shape) {
      super(grid, position, shape, MAX_COLLISIONS);
      setName(UUID.randomUUID().toString());
   }

   @Override
   public ProjectileTypes getProjectileTypes() {
      return ProjectileTypes.BULLET;
   }

   public static class BulletBuilder extends AbstracProjectileBuilder<BulletImpl> {

      private BulletBuilder() {
         // private
      }

      public static BulletBuilder builder() {
         return new BulletBuilder();
      }

      @Override
      public BulletImpl build() {
         return new BulletImpl(grid, position, shape);
      }
   }
}

