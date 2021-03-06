package com.myownb3.piranha.core.battle.weapon.gun.projectile.config;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;

public class ProjectileConfigImpl implements ProjectileConfig {

   private TargetGridElementEvaluator targetGridElementEvaluator;
   private DimensionInfo dimensionInfo;
   private int velocity;
   private double projectileDamage;
   private double missileRotationSpeed;

   protected ProjectileConfigImpl(DimensionInfo dimensionInfo, double projectileDamage, TargetGridElementEvaluator targetGridElementEvaluator,
         int velocity, double missileRotationSpeed) {
      this.dimensionInfo = requireNonNull(dimensionInfo);
      this.velocity = verifVelocity(velocity);
      this.projectileDamage = projectileDamage;
      this.missileRotationSpeed = missileRotationSpeed;
      this.targetGridElementEvaluator = targetGridElementEvaluator;
   }

   @Override
   public DimensionInfo getDimensionInfo() {
      return dimensionInfo;
   }

   @Override
   public double getProjectileDamage() {
      return projectileDamage;
   }

   @Override
   public TargetGridElementEvaluator getTargetGridElementEvaluator() {
      return targetGridElementEvaluator;
   }

   @Override
   public int getVelocity() {
      return velocity;
   }

   public double getMissileRotationSpeed() {
      return missileRotationSpeed;
   }

   private static int verifVelocity(int velocity) {
      if (velocity < 1) {
         throw new IllegalArgumentException("The velocity must be greater or equal than one!");
      }
      return velocity;
   }

   public abstract static class AbstractProjectileConfigBuilder<V extends ProjectileConfig, T extends AbstractProjectileConfigBuilder<V, T>> {
      protected int velocity;
      protected DimensionInfo dimensionInfo;
      protected double projectileDamage;
      protected TargetGridElementEvaluator targetGridElementEvaluator;
      protected double missileRotationSpeed;

      protected AbstractProjectileConfigBuilder() {
         this.projectileDamage = 10;
         this.missileRotationSpeed = 7;
      }

      public AbstractProjectileConfigBuilder<V, T> withDimensionInfo(DimensionInfo dimensionInfo) {
         this.dimensionInfo = dimensionInfo;
         return getThis();
      }

      protected abstract T getThis();

      public T withVelocity(int velocity) {
         this.velocity = velocity;
         return getThis();
      }

      public T withProjectileDamage(double projectileDamage) {
         this.projectileDamage = projectileDamage;
         return getThis();
      }

      public T withMissileRotationSpeed(double missileRotationSpeed) {
         this.missileRotationSpeed = missileRotationSpeed;
         return getThis();
      }

      public T withTargetGridElementEvaluator(TargetGridElementEvaluator targetGridElementEvaluator) {
         this.targetGridElementEvaluator = targetGridElementEvaluator;
         return getThis();
      }

      public abstract V build();
   }

   public static class ProjectileConfigBuilder extends AbstractProjectileConfigBuilder<ProjectileConfig, ProjectileConfigBuilder> {

      public ProjectileConfig build() {
         return new ProjectileConfigImpl(dimensionInfo, projectileDamage, targetGridElementEvaluator, velocity, missileRotationSpeed);
      }

      public static ProjectileConfigBuilder builder() {
         return new ProjectileConfigBuilder();
      }

      @Override
      protected ProjectileConfigBuilder getThis() {
         return this;
      }
   }
}
