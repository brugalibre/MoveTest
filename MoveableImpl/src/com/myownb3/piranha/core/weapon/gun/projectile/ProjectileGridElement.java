package com.myownb3.piranha.core.weapon.gun.projectile;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.UUID;

import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveable;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;

public class ProjectileGridElement extends AbstractMoveable implements Projectile {

   private Projectile projectile;

   private ProjectileGridElement(Grid grid, Position position, Shape shape, double damage,
         double health) {
      super(grid, position, shape);
      this.projectile = ProjectileBuilder.builder()
            .withDamage(damage)
            .withHealth(health)
            .withVelocity(getVelocity())
            .withOnDestroyedCallbackHandler(() -> grid.remove(this))
            .build();
      setName(UUID.randomUUID().toString());
   }

   private ProjectileGridElement(Projectile projectile, Grid grid, Position position, Shape shape, double damage,
         double health) {
      super(grid, position, shape);
      this.projectile = projectile;
      setName(UUID.randomUUID().toString());
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      projectile.onCollision(gridElements);
   }

   @Override
   public boolean isDestroyed() {
      return projectile.isDestroyed();
   }

   @Override
   public Damage getDamage() {
      return projectile.getDamage();
   }

   @Override
   public boolean isAimable() {
      return false;
   }

   @Override
   public boolean isAvoidable() {
      return true;
   }

   public static class ProjectileGridElementBuilder {
      private Position position;
      private Grid grid;
      private Shape shape;
      private double health;
      private double damage;
      private Projectile projectile;

      private ProjectileGridElementBuilder() {
         this.health = 4;
         this.damage = 10;
      }

      public static ProjectileGridElementBuilder builder() {
         return new ProjectileGridElementBuilder();
      }

      public ProjectileGridElementBuilder withPosition(Position position) {
         this.position = position;
         return this;
      }

      public ProjectileGridElementBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public ProjectileGridElementBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public ProjectileGridElementBuilder withProjectile(Projectile projectile) {
         this.projectile = projectile;
         return this;
      }

      public ProjectileGridElementBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public ProjectileGridElementBuilder withDamage(double damage) {
         this.damage = damage;
         return this;
      }

      public ProjectileGridElement build() {
         if (nonNull(projectile)) {
            return new ProjectileGridElement(projectile, grid, position, shape, damage, health);
         }
         return new ProjectileGridElement(grid, position, shape, damage, health);
      }
   }
}

