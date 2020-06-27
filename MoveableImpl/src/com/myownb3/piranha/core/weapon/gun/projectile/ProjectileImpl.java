package com.myownb3.piranha.core.weapon.gun.projectile;

import java.util.List;

import com.myownb3.piranha.core.destruction.Damage;
import com.myownb3.piranha.core.destruction.DamageImpl;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.destruction.HealthImpl;
import com.myownb3.piranha.core.destruction.OnDestroyedCallbackHandler;
import com.myownb3.piranha.core.destruction.SelfDestructive;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.wall.Wall;
import com.myownb3.piranha.core.grid.position.Position;

public class ProjectileImpl implements Projectile {

   private DestructionHelper destructionHelper;
   private DescentHandler descentHandler;
   private Shape shape;

   protected ProjectileImpl(Shape shape, double damage, double health, double velocity,
         OnDestroyedCallbackHandler onDestroyCallbackHandler) {
      this.destructionHelper = getDestructionHelper(damage, health, velocity, onDestroyCallbackHandler);
      this.shape = shape;
      this.descentHandler = buildDecentHandler(shape.getCenter(), shape.getDimensionRadius());
   }

   private DescentHandler buildDecentHandler(Position position, double dimensionRadius) {
      return new DescentHandler(position, dimensionRadius * 10, 0);
   }

   private DestructionHelper getDestructionHelper(double damage, double health, double velocity,
         OnDestroyedCallbackHandler onDestroyCallbackHandler) {
      return DestructionHelperBuilder.builder()
            .withDamage(DamageImpl.of(damage))
            .withHealth(HealthImpl.of(health))
            .withSelfDestructiveDamage(new ProjectileSelfDestructive(health))
            .withOnDestroyedCallbackHandler(onDestroyCallbackHandler)
            .build();
   }

   @Override
   public Damage getDamage() {
      return destructionHelper.getDamage();
   }

   @Override
   public boolean isDestroyed() {
      return destructionHelper.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      destructionHelper.onCollision(gridElements);
   }

   @Override
   public void autodetect() {
      Position position = descentHandler.evlPositionForNewHeight(shape.getCenter());
      shape.transform(position);
   }

   @Override
   public Shape getShape() {
      return shape;
   }

   private static class ProjectileSelfDestructive implements SelfDestructive {
      private double initialHealth;

      private ProjectileSelfDestructive(double initialHealth) {
         this.initialHealth = initialHealth;
      }

      @Override
      public Damage getDamage(GridElement gridElement) {
         double damageValue = Integer.MAX_VALUE; // A Projectile never survives a collision
         if (isWall(gridElement)) {
            damageValue = initialHealth / 2;// except we collided with a wall
         }
         return DamageImpl.of(damageValue);
      }

      private static boolean isWall(GridElement gridElement) {
         return gridElement instanceof Wall;
      }
   }

   public static class ProjectileBuilder {

      private double damage;
      private double health;
      private double velocity;
      private Shape shape;
      private OnDestroyedCallbackHandler onDestroyedCallbackHandler;

      private ProjectileBuilder() {
         // private
      }

      public ProjectileBuilder withHealth(double health) {
         this.health = health;
         return this;
      }

      public ProjectileBuilder withDamage(double damage) {
         this.damage = damage;
         return this;
      }

      public ProjectileBuilder withVelocity(double velocity) {
         this.velocity = velocity;
         return this;
      }

      public ProjectileBuilder withShape(Shape shape) {
         this.shape = shape;
         return this;
      }

      public ProjectileBuilder withOnDestroyedCallbackHandler(OnDestroyedCallbackHandler onDestroyedCallbackHandler) {
         this.onDestroyedCallbackHandler = onDestroyedCallbackHandler;
         return this;
      }

      public ProjectileImpl build() {
         return new ProjectileImpl(shape, damage, health, velocity, onDestroyedCallbackHandler);
      }

      public static ProjectileBuilder builder() {
         return new ProjectileBuilder();
      }
   }
}
