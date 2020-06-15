package com.myownb3.piranha.core.weapon.gun.projectile;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.UUID;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.AbstractMoveable;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;

public class ProjectileGridElement extends AbstractMoveable implements Projectile, AutoDetectable {
   private Projectile projectile;

   private ProjectileGridElement(Grid grid, Position position, Shape shape, BelligerentParty belligerentParty, double damage,
         double health, int velocity) {
      super(grid, position, shape, velocity);
      this.projectile = ProjectileBuilder.builder()
            .withDamage(damage)
            .withHealth(health)
            .withVelocity(getVelocity())
            .withBelligerentParty(belligerentParty)
            .withOnDestroyedCallbackHandler(() -> grid.remove(this))
            .build();
      setName(UUID.randomUUID().toString());
   }

   private ProjectileGridElement(Projectile projectile, Grid grid, Position position, Shape shape, double damage,
         double health, int velocity) {
      super(grid, position, shape, velocity);
      this.projectile = projectile;
      setName(UUID.randomUUID().toString());
   }

   @Override
   public void autodetect() {
      moveForward(getVelocity());
   }

   public BelligerentParty getBelligerentParty() {
      return projectile.getBelligerentParty();
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return projectile.isEnemy(otherBelligerent);
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
      return projectile == null ? true : projectile.isDestroyed();
   }

   @Override
   public Damage getDamage() {
      return projectile.getDamage();
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
      private Integer velocity;
      private Projectile projectile;
      private BelligerentParty belligerentParty;

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

      public ProjectileGridElementBuilder withVelocity(int velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileGridElementBuilder withProjectile(Projectile projectile) {
         this.projectile = projectile;
         return this;
      }

      public ProjectileGridElementBuilder withBelligerentParty(BelligerentParty belligerentParty) {
         this.belligerentParty = belligerentParty;
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
         requireNonNull(velocity, "A Projectile needs a velocity!");
         if (nonNull(projectile)) {
            return new ProjectileGridElement(projectile, grid, position, shape, damage, health, velocity);
         }
         return new ProjectileGridElement(grid, position, shape, belligerentParty, damage, health, velocity);
      }
   }
}

