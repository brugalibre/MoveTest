package com.myownb3.piranha.core.weapon.turret.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.Gun;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl.TurretShapeBuilder;

class TurretShapeImplTest {

   @Test
   void testBuildTurretShape() {
      // Given
      double expectedDimensionRadius = 10;

      // When
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGunCarriage(5, 10)
            .withCollisionDetectionHandler()
            .build();

      // Then
      GunCarriage gunCarriage = tcb.turretTowerShape.getGunCarriage();
      assertThat(gunCarriage, is(tcb.gunCarriage));
      assertThat(tcb.turretTowerShape.getDimensionRadius(), is(expectedDimensionRadius));
      assertThat(tcb.turretTowerShape.buildPath4Detection(), is(Collections.emptyList()));
   }

   @Test
   void testCheck4Collision_NoCollisionAtAll() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGunCarriage(0, 0)
            .withCollisionDetectionHandler()
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.turretTowerShape.check4Collision(tcb.collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), any(), any());
      assertThat(collisionDetectionResult.getMovedPosition(), is(nullValue()));
   }

   @Test
   void testCheck4Collision_CollisionWithGun() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGunCarriage(0, 0)
            .withCollisionDetectionHandler()
            .withCollisionWithGun(Positions.of(8, 8))
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.turretTowerShape.check4Collision(tcb.collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), any(), any());
      assertThat(collisionDetectionResult.getMovedPosition(), is(Positions.of(8, 8)));
   }

   @Test
   void testCheck4Collision_CollisionWithGunCarriage() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGunCarriage(0, 0)
            .withCollisionDetectionHandler()
            .withCollisionWithGunCarriage(Positions.of(3, 21))
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.turretTowerShape.check4Collision(tcb.collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), any(), any());
      assertThat(collisionDetectionResult.getMovedPosition(), is(Positions.of(3, 21)));
   }

   private static final class TestCaseBuilder {
      private GunCarriage gunCarriage;
      private TurretShapeImpl turretTowerShape;
      private CollisionDetectionHandler collisionDetectionHandler;

      private TestCaseBuilder() {
         // priv
      }

      public TestCaseBuilder withCollisionDetectionHandler() {
         this.collisionDetectionHandler = mock(CollisionDetectionHandler.class);
         return this;
      }

      private TestCaseBuilder withGunCarriage(double gunCarriageDimensionRadius, double gunDimensionRadius) {
         mockGunCarriage(gunCarriageDimensionRadius);
         mockGun(gunDimensionRadius);
         mockDefaultCollisionsDetectionResults();
         return this;
      }

      private TestCaseBuilder withCollisionWithGunCarriage(Position movedPosition) {
         mockGunCarriageCollisionDetectionResult(true, movedPosition);
         return this;
      }

      private TestCaseBuilder withCollisionWithGun(Position movedPosition) {
         mockGunCollisionDetectionResult(true, movedPosition);
         return this;
      }

      private void mockDefaultCollisionsDetectionResults() {
         mockGunCarriageCollisionDetectionResult(false, null);
         mockGunCollisionDetectionResult(false, null);
      }

      private void mockGunCollisionDetectionResult(boolean isCollision, Position movedPosition) {
         CollisionDetectionResult collisionDetectionWithGunResult = new CollisionDetectionResultImpl(isCollision, movedPosition);
         when(gunCarriage.getGun().getShape().check4Collision(any(), any(), any())).thenReturn(collisionDetectionWithGunResult);
      }

      private void mockGunCarriageCollisionDetectionResult(boolean isCollision, Position movedPosition) {
         CollisionDetectionResult collisionDetectionWithGunCarriageResult = new CollisionDetectionResultImpl(isCollision, movedPosition);
         when(gunCarriage.getShape().check4Collision(any(), any(), any())).thenReturn(collisionDetectionWithGunCarriageResult);
      }

      private void mockGun(double gunDimensionRadius) {
         when(gunCarriage.getGun()).thenReturn(mock(Gun.class));
         when(gunCarriage.getGun().getShape()).thenReturn(mock(Rectangle.class));
         when(gunCarriage.getGun().getShape().getDimensionRadius()).thenReturn(gunDimensionRadius);
      }

      private void mockGunCarriage(double gunCarriageDimensionRadius) {
         this.gunCarriage = mock(GunCarriage.class);
         when(gunCarriage.getShape()).thenReturn(mock(Circle.class));
         when(gunCarriage.getShape().getDimensionRadius()).thenReturn(gunCarriageDimensionRadius);
      }

      private TestCaseBuilder build() {
         turretTowerShape = TurretShapeBuilder.builder()
               .wighGunCarriage(gunCarriage)
               .build();
         return this;
      }
   }

}
