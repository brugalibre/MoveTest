package com.myownb3.piranha.core.battle.weapon.gun.projectile.descent;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.myownb3.piranha.core.battle.weapon.AutoDetectable;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;

class DescentAutoDetectableTest {

   @Test
   void testGetDescentAutoDetectable_DoNotTransformSinceSamePosition() {

      // Given
      double targetHeight = 0.0;
      double distanceBevorDescent = 0.0;
      Position pos = Positions.of(5, 5, 0);
      PositionShape shape = Mockito.spy(PositionShapeBuilder.builder()
            .withPosition(pos)
            .build());
      AutoDetectable descentAutoDetectable = DescentAutoDetectable.getDescentAutoDetectable(shape, distanceBevorDescent, targetHeight);

      // When
      descentAutoDetectable.autodetect();

      // Then
      verify(shape, never()).transform(any());
   }

   @Test
   void testGetDescentAutoDetectable_DoTransformSinceSamePosition() {

      // Given
      double targetHeight = 0.0;
      double distanceBevorDescent = 0.0;
      Position initPos = Positions.of(5, 5, 1);
      Position newPos = Positions.of(2, 2, 1);
      Position expectedDescentTransformPos = Positions.of(2, 2, 0);
      PositionShape shape = Mockito.spy(PositionShapeBuilder.builder()
            .withPosition(initPos)
            .build());
      AutoDetectable descentAutoDetectable = DescentAutoDetectable.getDescentAutoDetectable(shape, distanceBevorDescent, targetHeight);

      // When
      shape.transform(newPos);
      descentAutoDetectable.autodetect();

      // Then
      verify(shape).transform(eq(newPos));
      verify(shape).transform(eq(expectedDescentTransformPos));
   }
}
