package com.myownb3.piranha.core.weapon.turret.cluster.shape;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.turret.cluster.shape.TurretClusterShapeImpl.TurretClusterShapeBuilder;

class TurretClusterShapeImplTest {

   @Test
   void testTransform() {

      // Given
      Position position = Positions.of(55, 5);
      Position newPosition = Positions.of(55, 55);
      List<Shape> turretShapes = Collections.singletonList(PositionShapeBuilder.builder()
            .withPosition(position)
            .build());
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(position)
            .withTurretShapes(turretShapes)
            .build();

      // When
      turretClusterShape.transform(newPosition);

      // Then
      assertThat(turretShapes.get(0).getCenter(), is(newPosition));
   }

   @Test
   void testGetShapes() {

      // Given
      Position position = Positions.of(55, 5);
      List<Shape> turretShapes = Collections.singletonList(PositionShapeBuilder.builder()
            .withPosition(position)
            .build());
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(position)
            .withTurretShapes(turretShapes)
            .build();

      // When
      List<Shape> actualTurretShapes = turretClusterShape.getTurretShapes();

      // Then
      assertThat(actualTurretShapes, is(turretShapes));
   }

   @Test
   void testSetGridElement() {

      // Given
      GridElement gridElement = mock(GridElement.class);
      Detector detector = mock(Detector.class);
      PositionShape turretShape = PositionShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .build();
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(Collections.singletonList(turretShape))
            .build();

      // When
      ((AbstractShape) turretClusterShape).setGridElement(gridElement);
      turretClusterShape.detectObject(Positions.of(1, 1), detector);

      // Then
      verify(detector).detectObjectAlongPath(eq(gridElement), any(), eq(Positions.of(1, 1)));
      verify(detector).hasObjectDetected(eq(gridElement));
   }

   @Test
   void testCheck4Collision_CollisionWithFirst_DontCheckSecond() {

      // Given
      CollisionDetectionHandler collisionDetectionHandler = mock(CollisionDetectionHandler.class);

      List<Shape> turretShapes = new LinkedList<>();
      Shape turretShape1 = mockCollisionShape(true);
      Shape turretShape2 = mockCollisionShape(false);
      turretShapes.add(turretShape1);
      turretShapes.add(turretShape2);
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(turretShapes)
            .build();

      // When
      turretClusterShape.check4Collision(collisionDetectionHandler, Positions.of(55, 5), Collections.emptyList());

      // Then
      verify(turretShape2, never()).check4Collision(any(), any(), any());
   }

   @Test
   void testCheck4Collision_CollisionWithSecond_DontAbbortAfterFirst() {

      // Given
      CollisionDetectionHandler collisionDetectionHandler = mock(CollisionDetectionHandler.class);

      List<Shape> turretShapes = new LinkedList<>();
      Shape turretShape1 = mockCollisionShape(false);
      Shape turretShape2 = mockCollisionShape(true);
      turretShapes.add(turretShape1);
      turretShapes.add(turretShape2);
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(turretShapes)
            .build();

      // When
      turretClusterShape.check4Collision(collisionDetectionHandler, Positions.of(55, 5), Collections.emptyList());

      // Then
      verify(turretShape1).check4Collision(any(), any(), any());
      verify(turretShape2).check4Collision(any(), any(), any());
   }

   @Test
   void testCheck4Collision_NoCollisionAtAll() {

      // Given
      CollisionDetectionHandler collisionDetectionHandler = mock(CollisionDetectionHandler.class);

      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(Collections.emptyList())
            .build();

      // When
      CollisionDetectionResult collisionDetectionResult =
            turretClusterShape.check4Collision(collisionDetectionHandler, Positions.of(55, 5), Collections.emptyList());

      // Then
      assertThat(collisionDetectionResult.isCollision(), is(false));
   }

   private Shape mockCollisionShape(boolean isCollision) {
      Shape turretShape = mock(Shape.class);
      CollisionDetectionResult collisionDetectionResult = new CollisionDetectionResultImpl(isCollision, Positions.of(1, 1));
      when(turretShape.check4Collision(any(), any(), any())).thenReturn(collisionDetectionResult);
      return turretShape;
   }

   @Test
   void testGetForemostPosition() {
      // Given
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(Collections.singletonList(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(55, 5))
                  .build()))
            .build();

      // When
      Executable ex = () -> {
         turretClusterShape.getForemostPosition();
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   void testGetRearmostPosition() {
      // Given
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(Collections.singletonList(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(55, 5))
                  .build()))
            .build();

      // When
      Executable ex = () -> {
         turretClusterShape.getRearmostPosition();
      };

      // Then
      assertThrows(IllegalStateException.class, ex);
   }

   @Test
   void testGetDimensionRadius() {

      // Given
      double expectedDimensionRadius = 1.0;
      TurretClusterShape turretClusterShape = TurretClusterShapeBuilder.builder()
            .withPosition(Positions.of(55, 5))
            .withTurretShapes(Collections.singletonList(PositionShapeBuilder.builder()
                  .withPosition(Positions.of(55, 5))
                  .build()))
            .build();

      // Then
      double actualDimensionRadius = turretClusterShape.getDimensionRadius();

      // When
      assertThat(actualDimensionRadius, is(expectedDimensionRadius));
   }
}
