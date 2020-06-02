package com.myownb3.piranha.core.weapon.gun.projectile;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveable;

public abstract class AbstracProjectile extends AbstractMoveable implements Projectile {

   private boolean isDestroyed;
   private int maxCollisionsBeforeDestroy;
   private int collisionCounter;

   protected AbstracProjectile(Grid grid, Position position, Shape shape, int maxCollisionsBeforeDestroy) {
      super(grid, position, shape);
      this.maxCollisionsBeforeDestroy = maxCollisionsBeforeDestroy;
   }

   @Override
   public void onCollision() {
      collisionCounter++;
      isDestroyed = collisionCounter >= maxCollisionsBeforeDestroy;
      if (isDestroyed) {
         grid.remove(this);
      }
   }

   @Override
   public boolean isDestroyed() {
      return isDestroyed;
   }

   public abstract static class AbstracProjectileBuilder<T extends AbstracProjectile> {

      protected Position position;
      protected Grid grid;
      protected Shape shape;

      protected AbstracProjectileBuilder() {
         // private
      }

      public AbstracProjectileBuilder<T> withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public AbstracProjectileBuilder<T> withPosition(Position position) {
         this.position = position;
         return this;
      }

      public AbstracProjectileBuilder<T> withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public abstract T build();
   }
}
