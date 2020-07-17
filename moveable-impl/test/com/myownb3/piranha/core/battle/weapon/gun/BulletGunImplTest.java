package com.myownb3.piranha.core.battle.weapon.gun;

import static com.myownb3.piranha.core.battle.weapon.gun.AbstractGun.PROJECTILE_START_POS_OFFSET;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.collision.detection.handler.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.worker.WorkerThreadFactory;

class BulletGunImplTest {

   private TestGrid grid = new TestGrid(100, 100, -100, -100);

   @BeforeEach
   public void setUp() {
      ProjectileFactory.INSTANCE.registerGrid(grid);
      WorkerThreadFactory.INSTANCE.restart();
   }

   @AfterEach
   public void tearDown() {
      ProjectileFactory.INSTANCE.deregisterGrid();
      WorkerThreadFactory.INSTANCE.shutdown();
   }

   @Test
   void testFireGun() throws InterruptedException {
      // Given
      double heightFromBottom = 15;
      int distanceToBottom = 2;
      double projectileRadius = 5.0;
      int velocityMulti = 2;
      int salve = 1;
      Position position = Positions.of(5, 5, distanceToBottom);
      DefaultGunImpl simpleBulletGun = DefaultGunBuilder.builder()
            .withGunProjectileType(ProjectileTypes.BULLET)
            .withGunConfig(GunConfigBuilder.builder()
                  .withSalveSize(salve)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withDimensionInfo(DimensionInfoBuilder.builder()
                              .withDimensionRadius(projectileRadius)
                              .withHeightFromBottom(heightFromBottom)
                              .build())
                        .withVelocity(velocityMulti)
                        .build())
                  .build())
            .withGunShape(GunShapeBuilder.builder()
                  .withBarrel(RectangleBuilder.builder()
                        .withCenter(position)
                        .withHeight(projectileRadius)
                        .withWidth(projectileRadius)
                        .build())
                  .build())
            .build();
      simpleBulletGun.evalAndSetGunPosition(position);


      Position foremostGunPosition = simpleBulletGun.getShape().getForemostPosition();
      foremostGunPosition = Positions.of(foremostGunPosition.getX(), foremostGunPosition.getY(), heightFromBottom + distanceToBottom);
      Position expectedProjectilPosition = foremostGunPosition.movePositionForward4Distance(PROJECTILE_START_POS_OFFSET + projectileRadius);

      // When
      simpleBulletGun.fire();
      Thread.sleep(200);// wait until the WorkerThreadFactory has created the new GridElement

      // Then
      assertThat(grid.addedGridElemPos, is(expectedProjectilPosition));
   }

   @Test
   void testFireGunSalve() throws Exception {
      // Given
      int radius = 5;
      int salve = 2;
      Position position = Positions.of(radius, radius);
      DefaultGunImpl simpleBulletGun = spy(DefaultGunBuilder.builder()
            .withGunProjectileType(ProjectileTypes.BULLET)
            .withGunConfig(GunConfigBuilder.builder()
                  .withSalveSize(salve)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withVelocity(2)
                        .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(radius))
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
      Callable<List<Projectile>> fireCallable = simpleBulletGun.getFireCallable();
      List<Projectile> projectiles = fireCallable.call();

      // Then
      assertThat(projectiles.size(), is(salve));
   }

   @Test
   void testFireGun2TimesInARow() throws InterruptedException {
      // Given
      WorkerThreadFactory.INSTANCE.restart();
      DefaultGunImpl simpleBulletGun = spy(DefaultGunBuilder.builder()
            .withGunProjectileType(ProjectileTypes.BULLET)
            .withGunConfig(GunConfigBuilder.builder()
                  .withSalveSize(1)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
                        .withVelocity(1)
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

      when(simpleBulletGun.getFireCallable()).thenReturn(() -> {
         simpleBulletGun.setTimeStamp();
         return Collections.emptyList();
      });

      // When
      simpleBulletGun.fire();
      Thread.sleep(300); // Wait for the WorkerThreadExecuter until it has executed our callable
      simpleBulletGun.fire();

      // Then
      verify(simpleBulletGun).getFireCallable();
   }

   private static class TestGrid extends DefaultGrid {

      private Position addedGridElemPos;

      public TestGrid(int maxY, int maxX, int minX, int minY) {
         super(maxX, maxY, minX, minY, mock(DefaultCollisionDetectionHandlerImpl.class));
      }

      @Override
      public void addElement(GridElement gridElement) {
         super.addElement(gridElement);
         addedGridElemPos = Positions.of(gridElement.getPosition());
      }
   }
}
