package com.myownb3.piranha.core.battle.weapon.tank.shape;

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

import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.battle.weapon.turret.shape.TurretShapeImpl;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

class TankShapeImplTest {

   @Test
   void testDetectObject_DetectTurret() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankPosition(Positions.of(1, 1))
            .withTank(5, 5)
            .withCollisionDetectionHandler()
            .withGridElement(mock(GridElement.class))
            .withDetector()
            .withDetectorPos()
            .withTurretShapeDetection()
            .build();
      tcb.tankShape.setGridElement(tcb.gridElement);

      // When
      boolean actualDetection = tcb.tankShape.detectObject(tcb.detectorPos, tcb.detector);

      // Then
      assertThat(actualDetection, is(true));
   }

   @Test
   void testDetectObject_DetectHull() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankPosition(Positions.of(1, 1))
            .withTank(5, 5)
            .withCollisionDetectionHandler()
            .withGridElement(mock(GridElement.class))
            .withDetector()
            .withDetectorPos()
            .withHullDetection()
            .build();
      tcb.tankShape.setGridElement(tcb.gridElement);

      // When
      boolean actualDetection = tcb.tankShape.detectObject(tcb.detectorPos, tcb.detector);

      // Then
      assertThat(actualDetection, is(true));
   }

   @Test
   void testDetectObject_NoDetection() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankPosition(Positions.of(1, 1))
            .withTank(5, 5)
            .withCollisionDetectionHandler()
            .withGridElement(mock(GridElement.class))
            .withDetector()
            .withDetectorPos()
            .build();
      tcb.tankShape.setGridElement(tcb.gridElement);

      // When
      boolean actualDetection = tcb.tankShape.detectObject(tcb.detectorPos, tcb.detector);

      // Then
      assertThat(actualDetection, is(false));
   }

   @Test
   void testTestForemostAndRearmostPosition() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankPosition(Positions.of(1, 1))
            .withTank(5, 5)
            .withCollisionDetectionHandler()
            .withGridElement(mock(GridElement.class))
            .build();

      // When
      tcb.tankShape.getForemostPosition();
      tcb.tankShape.getRearmostPosition();

      // Then
      verify(tcb.hull).getForemostPosition(); // so far this is only delegating
      verify(tcb.hull).getRearmostPosition();
   }

   @Test
   void testTestSetGridElement() {
      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankPosition(Positions.of(1, 1))
            .withTank(5, 5)
            .withCollisionDetectionHandler()
            .withGridElement(mock(GridElement.class))
            .build();

      // When
      tcb.tankShape.setGridElement(tcb.gridElement);

      // Then
      verify(((AbstractShape) tcb.hull)).setGridElement(eq(tcb.gridElement));
      verify(((AbstractShape) tcb.turretShape)).setGridElement(eq(tcb.gridElement));
   }

   @Test
   void testTransformTankShape() {
      // Given
      Position newPos = Positions.of(5, 5);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankPosition(Positions.of(1, 1))
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
            .withTankPosition(Positions.of(1, 1))
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
            .withTankPosition(Positions.of(1, 1))
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
            .withTankPosition(Positions.of(1, 1))
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
            .withTankPosition(Positions.of(1, 1))
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
      private TankShapeImpl tankShape;
      private Shape hull;
      private CollisionDetectionHandler collisionDetectionHandler;
      private GridElement gridElement;
      private IDetector detector;
      private Position detectorPos;
      private Position currentPos;

      private TestCaseBuilder() {
         tankShape = mock(TankShapeImpl.class);
      }

      public TestCaseBuilder withTankPosition(Position currentPos) {
         this.currentPos = currentPos;
         return this;
      }

      public TestCaseBuilder withHullDetection() {
         when(hull.detectObject(eq(detectorPos), eq(detector))).thenReturn(true);
         return this;
      }

      public TestCaseBuilder withTurretShapeDetection() {
         when(turretShape.detectObject(eq(detectorPos), eq(detector))).thenReturn(true);
         return this;
      }

      public TestCaseBuilder withDetector() {
         this.detector = mock(IDetector.class);
         return this;
      }

      public TestCaseBuilder withDetectorPos() {
         this.detectorPos = mock(Position.class);
         return this;
      }

      public TestCaseBuilder withGridElement(GridElement gridElement) {
         this.gridElement = gridElement;
         return this;
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
         hull = mock(RectangleImpl.class);
         when(hull.getCenter()).thenReturn(currentPos);
         when(hull.getDimensionRadius()).thenReturn(hullDimensionRadius);
         when(tankShape.getHull()).thenReturn(hull);
      }

      private void mockTurret(double turretDimensionRadius) {
         this.turretShape = mock(TurretShapeImpl.class);
         when(turretShape.getCenter()).thenReturn(currentPos);
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
