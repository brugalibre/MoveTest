package com.myownb3.piranha.core.battle.weapon.gun;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.factory.ProjectileFactory;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.collision.detection.handler.DefaultCollisionDetectionHandlerImpl;
import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.worker.WorkerThreadFactory;

class MissileGunImplTest {
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
   void testGetType() throws InterruptedException {

      // Given
      DefaultGunImpl missileGunImpl = DefaultGunBuilder.builder()
            .withGunProjectileType(ProjectileTypes.MISSILE)
            .withGunConfig(GunConfigBuilder.builder()
                  .withSalveSize(1)
                  .withRoundsPerMinute(1)
                  .withProjectileConfig(ProjectileConfigBuilder.builder()
                        .withDimensionInfo(DimensionInfoBuilder.builder()
                              .withDimensionRadius(2)
                              .withHeightFromBottom(1)
                              .build())
                        .withVelocity(1)
                        .build())
                  .build())
            .withGunShape(GunShapeBuilder.builder()
                  .withBarrel(RectangleBuilder.builder()
                        .withCenter(Positions.of(5, 5))
                        .withHeight(1)
                        .withWidth(1)
                        .build())
                  .build())
            .build();

      // When
      missileGunImpl.fire();
      Thread.sleep(200);// wait until the WorkerThreadFactory has created the new GridElement

      // Then
      assertThat(grid.actualGridElement.getShape(), instanceOf(Rectangle.class));
   }

   private static class TestGrid extends DefaultGrid {

      private GridElement actualGridElement;

      public TestGrid(int maxY, int maxX, int minX, int minY) {
         super(maxX, maxY, minX, minY, mock(DefaultCollisionDetectionHandlerImpl.class));
      }

      @Override
      public void addElement(GridElement gridElement) {
         super.addElement(gridElement);
         actualGridElement = gridElement;
      }
   }
}
