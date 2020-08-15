package com.myownb3.piranha.core.battle.weapon.gun.projectile;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.destruction.Damage;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveable;

public class ProjectileGridElement extends AbstractMoveable implements Projectile {
   private Projectile projectile;

   private ProjectileGridElement(Projectile projectile, Grid grid, DimensionInfo dimensionInfo, int velocity) {
      super(grid, projectile.getShape(), dimensionInfo, velocity);
      this.projectile = projectile;
      setName(UUID.randomUUID().toString());
   }

   @Override
   public void autodetect() {
      moveForward();
      projectile.autodetect();
   }

   @Override
   public Position getPosition() {
      return shape.getCenter();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      projectile.onCollision(gridElements);
   }

   @Override
   public boolean isDestroyed() {
      return isDestroyed(projectile);
   }

   @Visible4Testing
   static boolean isDestroyed(Projectile projectile) {
      return projectile == null || projectile.isDestroyed();
   }

   @Override
   public ProjectileTypes getProjectileType() {
      return projectile.getProjectileType();
   }

   @Override
   public Damage getDamage() {
      return projectile.getDamage();
   }

   @Override
   public double getSmallestStepWith() {
      return super.getSmallestStepWith() * 10;
   }

   public static class ProjectileGridElementBuilder {
      private Grid grid;
      private Integer velocity;
      private Projectile projectile;
      private DimensionInfo dimensionInfo;

      private ProjectileGridElementBuilder() {
         // privatos
      }

      public static ProjectileGridElementBuilder builder() {
         return new ProjectileGridElementBuilder();
      }

      public ProjectileGridElementBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public ProjectileGridElementBuilder withDimensionInfo(DimensionInfo dimensionInfo) {
         this.dimensionInfo = dimensionInfo;
         return this;
      }

      public ProjectileGridElementBuilder withProjectile(Projectile projectile) {
         this.projectile = projectile;
         return this;
      }

      public ProjectileGridElementBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileGridElement build() {
         requireNonNull(velocity, "A Projectile needs a velocity!");
         requireNonNull(dimensionInfo, "A Projectile needs a dimensionInfo!");
         requireNonNull(projectile, "A Projectile needs a projectile!");
         ProjectileGridElement projectileGridElement = new ProjectileGridElement(projectile, grid, dimensionInfo, velocity);
         grid.addElement(projectileGridElement);
         return projectileGridElement;
      }
   }
}

