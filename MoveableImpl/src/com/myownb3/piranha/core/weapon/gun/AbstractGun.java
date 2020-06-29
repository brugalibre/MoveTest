package com.myownb3.piranha.core.weapon.gun;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.weapon.gun.shape.GunShape;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public abstract class AbstractGun implements Gun {

   private static final int PROJECTILE_START_POS_OFFSET = 10;// an additionally offset, so the firing a salve the projectiles don't collide with each other
   private static final int TIME_BETWEEN_SALVES = 150;
   private AtomicLong lastTimeStamp;
   private int minTimeBetweenShooting;
   private GunShape gunShape;

   protected GunConfig gunConfig;

   protected AbstractGun(GunShape gunShape, GunConfig gunConfig) {
      this.gunShape = requireNonNull(gunShape);
      this.gunConfig = gunConfig;
      this.minTimeBetweenShooting = 60 * 3600 / gunConfig.getRoundsPerMinute();
      lastTimeStamp = new AtomicLong(System.currentTimeMillis() - minTimeBetweenShooting);
   }

   @Override
   public void fire() {
      long now = System.currentTimeMillis();
      if (now - lastTimeStamp.get() >= minTimeBetweenShooting) {
         fireSalve();
      }
   }

   private void fireSalve() {
      Callable<List<Projectile>> fireSalveCallable = getFireCallable();
      WorkerThreadFactory.INSTANCE.executeAsync(fireSalveCallable);
   }

   @Visible4Testing
   Callable<List<Projectile>> getFireCallable() {
      return () -> {
         List<Projectile> firedProjectiles = new ArrayList<>();
         for (int i = 0; i < gunConfig.getSalveSize(); i++) {
            Position projectileStartPos = createProjectileStartPos(gunShape.getForemostPosition(), gunConfig.getProjectileConfig());
            firedProjectiles.add(fireShot(projectileStartPos));
            setTimeStamp();
            delayNextShot();
         }
         return firedProjectiles;
      };
   }

   @Visible4Testing
   void setTimeStamp() {
      lastTimeStamp.set(System.currentTimeMillis());
   }

   private Projectile fireShot(Position projectileStartPos) {
      ProjectileTypes projectileType = getType();
      return ProjectileFactory.INSTANCE.createProjectile(projectileType, projectileStartPos, gunConfig.getProjectileConfig());
   }

   private static Position createProjectileStartPos(Position foremostPosition, ProjectileConfig projectileConfig) {
      DimensionInfo projctileDimensionInfo = projectileConfig.getDimensionInfo();
      Position projectileStartWithinGun =
            foremostPosition.movePositionForward4Distance(PROJECTILE_START_POS_OFFSET + projctileDimensionInfo.getDimensionRadius());
      Direction direction = foremostPosition.getDirection();
      double distance2Ground = projectileStartWithinGun.getZ() + projctileDimensionInfo.getHeightFromBottom();
      return Positions.of(direction, projectileStartWithinGun.getX(), projectileStartWithinGun.getY(), distance2Ground);
   }

   @Override
   public void evalAndSetGunPosition(Position gunMountPosition) {
      Position gunPos = gunMountPosition.movePositionForward4Distance(gunShape.getLength() / 2);
      this.gunShape.transform(gunPos);
   }

   @Override
   public GunShape getShape() {
      return gunShape;
   }

   @Override
   public GunConfig getGunConfig() {
      return gunConfig;
   }

   /**
    * @return the type of the projectile
    */
   protected abstract ProjectileTypes getType();

   private static void delayNextShot() throws InterruptedException {
      Thread.sleep(TIME_BETWEEN_SALVES);
   }

   public static abstract class AbstractGunBuilder<T extends AbstractGun> {
      protected GunShape gunShape;
      protected GunConfig gunConfig;

      protected AbstractGunBuilder() {
         // private
      }

      public AbstractGunBuilder<T> withGunConfig(GunConfig gunConfig) {
         this.gunConfig = gunConfig;
         return this;
      }

      public AbstractGunBuilder<T> withGunShape(GunShape gunShape) {
         this.gunShape = gunShape;
         return this;
      }

      public abstract T build();
   }

}
