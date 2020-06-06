package com.myownb3.piranha.core.weapon.tank.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
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
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;

class TankShapeImplTest {

   @Test
   void testTransformTankShape() {
      // Given
      Position newPos = Positions.of(5, 5);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTank(5, 5)
            .withCollisionDetectionHandler()
            .build();

      // When
      tcb.tankShape.transform(newPos);

      // Then
      verify(tcb.hull).transform(eq(newPos));
      verify(tcb.turretShape).transform(eq(newPos));
   }

   @Test
   void testBuildTankShape() {
      // Given
      double turretShapeDimensionRadius = 10.0;
      double expectedDimensionRadius = turretShapeDimensionRadius;
      double hullDimensionRadius = 5.0;

      // When
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTank(turretShapeDimensionRadius, hullDimensionRadius)
            .withCollisionDetectionHandler()
            .build();

      // Then

      assertThat(tcb.tankShape.getHull().getDimensionRadius(), is(hullDimensionRadius));
      assertThat(tcb.tankShape.getTurretShape().getDimensionRadius(), is(turretShapeDimensionRadius));
      assertThat(tcb.tankShape.getDimensionRadius(), is(expectedDimensionRadius));
   }

   @Test
   void testCheck4Collision_NoCollisionAtAll() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTank(0, 0)
            .withCollisionDetectionHandler()
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.tankShape.check4Collision(tcb.collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), any(), any());
      assertThat(collisionDetectionResult.getMovedPosition(), is(nullValue()));
   }

   @Test
   void testCheck4Collision_CollisionWithHull() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTank(0, 0)
            .withCollisionDetectionHandler()
            .withCollisionWithHull(Positions.of(8, 8))
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.tankShape.check4Collision(tcb.collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), any(), any());
      assertThat(collisionDetectionResult.getMovedPosition(), is(Positions.of(8, 8)));
   }

   @Test
   void testCheck4Collision_CollisionWithTurret() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTank(0, 0)
            .withCollisionDetectionHandler()
            .withCollisionWithTurret(Positions.of(3, 21))
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            tcb.tankShape.check4Collision(tcb.collisionDetectionHandler, Positions.of(5, 5), Collections.emptyList());

      // Then
      verify(tcb.collisionDetectionHandler, never()).handleCollision(any(), any(), any());
      assertThat(collisionDetectionResult.getMovedPosition(), is(Positions.of(3, 21)));
   }

   private static final class TestCaseBuilder {
      private TurretShape turretShape;
      private TankShape tankShape;
      private Shape hull;
      private CollisionDetectionHandler collisionDetectionHandler;

      private TestCaseBuilder() {
         tankShape = mock(TankShapeImpl.class);
      }

      public TestCaseBuilder withCollisionDetectionHandler() {
         this.collisionDetectionHandler = mock(CollisionDetectionHandler.class);
         return this;
      }

      private TestCaseBuilder withTank(double turretDimensionRadius, double hullDimensionRadius) {
         mockTurret(turretDimensionRadius);
         mockHull(hullDimensionRadius);
         mockDefaultCollisionsDetectionResults();
         return this;
      }

      private TestCaseBuilder withCollisionWithTurret(Position movedPosition) {
         mockTurretCollisionDetectionResult(true, movedPosition);
         return this;
      }

      private TestCaseBuilder withCollisionWithHull(Position movedPosition) {
         mockHullCollisionDetectionResult(true, movedPosition);
         return this;
      }

      private void mockDefaultCollisionsDetectionResults() {
         mockTurretCollisionDetectionResult(false, null);
         mockHullCollisionDetectionResult(false, null);
      }

      private void mockHullCollisionDetectionResult(boolean isCollision, Position movedPosition) {
         CollisionDetectionResult collisionDetectionWithHullResult = new CollisionDetectionResultImpl(isCollision, movedPosition);
         when(hull.check4Collision(any(), any(), any())).thenReturn(collisionDetectionWithHullResult);
      }

      private void mockTurretCollisionDetectionResult(boolean isCollision, Position movedPosition) {
         CollisionDetectionResult collisionDetectionWithTurretResult = new CollisionDetectionResultImpl(isCollision, movedPosition);
         when(turretShape.check4Collision(any(), any(), any())).thenReturn(collisionDetectionWithTurretResult);
      }

      private void mockHull(double hullDimensionRadius) {
         hull = mock(Rectangle.class);
         when(hull.getCenter()).thenReturn(mock(Position.class));
         when(hull.getDimensionRadius()).thenReturn(hullDimensionRadius);
         when(tankShape.getHull()).thenReturn(hull);
      }

      private void mockTurret(double turretDimensionRadius) {
         this.turretShape = mock(TurretShape.class);
         when(turretShape.getCenter()).thenReturn(mock(Position.class));
         when(turretShape.getDimensionRadius()).thenReturn(turretDimensionRadius);
         when(tankShape.getTurretShape()).thenReturn(turretShape);
      }

      private TestCaseBuilder build() {
         tankShape = TankShapeBuilder.builder()
               .withHull(hull)
               .withTurretShape(turretShape)
               .build();
         return this;
      }
   }

}
