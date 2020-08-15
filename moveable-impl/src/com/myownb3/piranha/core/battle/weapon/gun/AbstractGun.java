package com.myownb3.piranha.core.battle.weapon.gun;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

import com.myownb3.piranha.annotation.Visible4Testing;
import com.myownb3.piranha.audio.AudioClip;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.grid.direction.Direction;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public abstract class AbstractGun implements Gun {

   private static final long serialVersionUID = -1289023736390052168L;
   public static final int PROJECTILE_START_POS_OFFSET = 5;// an additionally offset, so the firing a salve the projectiles don't collide with the gun
   private static final int TIME_BETWEEN_SALVES = 150;
   private AtomicLong lastTimeStamp;
   private int minTimeBetweenShooting;
   private GunShape gunShape;

   protected transient List<OnGunFireListener> onGunFireListeners;
   protected transient GunConfig gunConfig;
   private ProjectileTypes projectileType;

   protected AbstractGun(GunShape gunShape, GunConfig gunConfig, ProjectileTypes projectileType, List<OnGunFireListener> onGunFireListeners) {
      this.gunShape = gunShape;
      this.gunConfig = gunConfig;
      this.projectileType = projectileType;
      this.minTimeBetweenShooting = 60 * 3600 / gunConfig.getRoundsPerMinute();
      this.onGunFireListeners = onGunFireListeners;
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
      getAudioClipOpt().ifPresent(AudioClip::play);
      callOnGunFireListeners();
      return ProjectileFactory.INSTANCE.createProjectile(projectileType, projectileStartPos, gunConfig.getProjectileConfig());
   }

   private void callOnGunFireListeners() {
      onGunFireListeners.stream()
            .forEach(onGunFireListener -> onGunFireListener.onFire(gunShape.getForemostPosition()));
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

   private static void delayNextShot() throws InterruptedException {
      Thread.sleep(TIME_BETWEEN_SALVES);
   }

   private Optional<AudioClip> getAudioClipOpt() {
      return Optional.ofNullable(gunConfig.getAudioClip());
   }

   public abstract static class AbstractGunBuilder<T extends AbstractGun> {
      protected GunShape gunShape;
      protected GunConfig gunConfig;
      protected ProjectileTypes projectileType;
      protected List<OnGunFireListener> onGunFireListeners;

      protected AbstractGunBuilder() {
         this.onGunFireListeners = new ArrayList<>();
      }

      public AbstractGunBuilder<T> withGunConfig(GunConfig gunConfig) {
         this.gunConfig = gunConfig;
         return this;
      }

      public AbstractGunBuilder<T> withOnGunFireListeners(List<OnGunFireListener> onGunFireListeners) {
         this.onGunFireListeners.addAll(onGunFireListeners);
         return this;
      }

      public AbstractGunBuilder<T> withGunProjectileType(ProjectileTypes projectileType) {
         this.projectileType = projectileType;
         return this;
      }

      public AbstractGunBuilder<T> withGunShape(GunShape gunShape) {
         this.gunShape = gunShape;
         return this;
      }

      public abstract T build();
   }

}
