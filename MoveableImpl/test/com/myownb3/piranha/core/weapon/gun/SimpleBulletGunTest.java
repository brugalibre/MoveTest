package com.myownb3.piranha.core.weapon.gun;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.worker.WorkerThreadFactory;

class SimpleBulletGunTest {

   @BeforeEach
   public void setUp() {
      ProjectileFactory.INSTANCE.registerGrid(mock(Grid.class));
      WorkerThreadFactory.INSTANCE.restart();
   }

   @AfterEach
   public void tearDown() {
      ProjectileFactory.INSTANCE.degisterGrid();
      WorkerThreadFactory.INSTANCE.shutdown();
   }

   @Test
   void testFireGun() {
      // Given
      int projectileRadius = 5;
      int velocityMulti = 2;
      int salve = 1;
      Position position = Positions.of(5, 5);
      BulletGunImpl simpleBulletGun = spy(BulletGunBuilder.builder()
            .withGunConfig(GunConfigBuilder.builder()
                  .withVelocity(velocityMulti)
                  .withSalveSize(salve)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withDimension(new DimensionImpl(0, 0, projectileRadius, projectileRadius))
                        .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                        .build())
                  .build())
            .withGunShape(GunShapeBuilder.builder()
                  .withBarrel(RectangleBuilder.builder()
                        .withCenter(position)
                        .withHeight(projectileRadius)
                        .withWidth(projectileRadius)
                        .build())
                  .build())
            .build());
      simpleBulletGun.evalAndSetGunPosition(position);
      Position expectedProjectilPosition = Positions.movePositionForward4Distance(simpleBulletGun.getShape().getForemostPosition(), projectileRadius);

      // When
      simpleBulletGun.fire();

      // Then
      verify(simpleBulletGun).getFireCallable(eq(expectedProjectilPosition));
   }

   @Test
   void testFireGunSalve() throws Exception {
      // Given
      int radius = 5;
      int salve = 2;
      Position position = Positions.of(radius, radius);
      BulletGunImpl simpleBulletGun = spy(BulletGunBuilder.builder()
            .withGunConfig(GunConfigBuilder.builder()
                  .withVelocity(2)
                  .withSalveSize(salve)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withDimension(new DimensionImpl(0, 0, radius, radius))
                        .build())
                  .build())
            .withGunShape(GunShapeBuilder.builder()
                  .withBarrel(RectangleBuilder.builder()
                        .withCenter(position)
                        .withHeight(5)
                        .withWidth(5)
                        .build())
                  .build())
            .build());
      simpleBulletGun.evalAndSetGunPosition(position);

      // When
      Callable<List<Projectile>> fireCallable = simpleBulletGun.getFireCallable(position);
      List<Projectile> projectiles = fireCallable.call();

      // Then
      assertThat(projectiles.size(), CoreMatchers.is(salve));
   }

   @Test
   void testFireGun2TimesInARow() throws InterruptedException {
      // Given
      WorkerThreadFactory.INSTANCE.restart();
      BulletGunImpl simpleBulletGun = spy(BulletGunBuilder.builder()
            .withGunConfig(GunConfigBuilder.builder()
                  .withVelocity(1)
                  .withSalveSize(1)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withDimension(new DimensionImpl(0, 0, 5, 5))
                        .build())
                  .build())
            .withGunShape(GunShapeBuilder.builder()
                  .withBarrel(RectangleBuilder.builder()
                        .withCenter(Positions.of(5, 5))
                        .withHeight(5)
                        .withWidth(5)
                        .build())
                  .build())
            .build());
      simpleBulletGun.evalAndSetGunPosition(Positions.of(5, 5));

      when(simpleBulletGun.getFireCallable(any())).thenReturn(() -> {
         simpleBulletGun.setTimeStamp();
         return Collections.emptyList();
      });

      // When
      simpleBulletGun.fire();
      Thread.sleep(300); // Wait for the WorkerThreadExecuter until it has executed our callable
      simpleBulletGun.fire();

      // Then
      verify(simpleBulletGun).getFireCallable(any());
   }
}
