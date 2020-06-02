package com.myownb3.piranha.core.weapon.gun;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.direction.Directions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public abstract class AbstractGun implements Gun {

   private static final int TIME_BETWEEN_SALVES = 110;
   private Position position;
   private Rectangle shape;
   private AtomicLong lastTimeStamp;

   protected GunConfig gunConfig;
   private int minTimeBetweenShooting;

   protected AbstractGun(Rectangle shape, GunConfig gunConfig) {
      this.shape = requireNonNull(shape);
      this.gunConfig = gunConfig;
      this.minTimeBetweenShooting = 60 * 3600 / gunConfig.getRoundsPerMinute();
      lastTimeStamp = new AtomicLong(System.currentTimeMillis() - minTimeBetweenShooting);
   }

   @Override
   public void fire() {
      long now = System.currentTimeMillis();
      if (now - lastTimeStamp.get() >= minTimeBetweenShooting) {
         Position projectileStartPos = createProjectilStartPos(shape.getForemostPosition(), gunConfig);
         fireSalve(projectileStartPos);
      }
   }

   private void fireSalve(final Position projectileStartPos) {
      Callable<List<Projectile>> fireSalveCallable = getFireCallable(projectileStartPos);
      WorkerThreadFactory.INSTANCE.executeAsync(fireSalveCallable);
   }

   @Visible4Testing
   Callable<List<Projectile>> getFireCallable(final Position projectileStartPos) {
      return () -> {
         List<Projectile> firedProjectiles = new ArrayList<>();
         for (int i = 0; i < gunConfig.getSalveSize(); i++) {
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

   private Projectile fireShot(final Position projectileStartPos) {
      ProjectileTypes projectileType = getType();
      return ProjectileFactory.INSTANCE.createProjectile(projectileType, projectileStartPos, gunConfig.getProjectileConfig());
   }

   private static Position createProjectilStartPos(Position foremostPosition, GunConfig gunConfig) {
      ProjectileConfig projectileConfig = gunConfig.getProjectileConfig();
      Position projectileStartWithinGun =
            Positions.movePositionForward4Distance(foremostPosition, projectileConfig.getProjectileDimension().getHeight());
      Direction direction = foremostPosition.getDirection();
      Direction startDirection = Directions.of(direction.getForwardX() * gunConfig.getVeloCity(), direction.getForwardY() * gunConfig.getVeloCity());
      return Positions.of(startDirection, projectileStartWithinGun.getX(), projectileStartWithinGun.getY());
   }

   @Override
   public void evalAndSetGunPosition(Position gunMountPosition) {
      this.position = Positions.movePositionForward4Distance(gunMountPosition, shape.getHeight() / 2);
      this.shape.transform(position);
   }

   @Override
   public Position getPosition() {
      return position;
   }

   @Override
   public Rectangle getShape() {
      return shape;
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
      protected Rectangle rectangle;
      protected GunConfig gunConfig;

      protected AbstractGunBuilder() {
         // private
      }

      public AbstractGunBuilder<T> withGunConfig(GunConfig gunConfig) {
         this.gunConfig = gunConfig;
         return this;
      }

      public AbstractGunBuilder<T> withRectangle(Rectangle rectangle) {
         this.rectangle = rectangle;
         return this;
      }

      public abstract T build();
   }

}
