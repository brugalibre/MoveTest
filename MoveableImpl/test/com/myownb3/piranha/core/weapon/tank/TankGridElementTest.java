package com.myownb3.piranha.core.weapon.tank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl;

class TankGridElementTest {


   @Test
   void testIsAimable() {

      // Given
      Moveable actualMoveableMock = mock(Moveable.class);
      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTankMoveable(actualMoveableMock)
            .withTank(mockTank())
            .build();

      // When
      boolean isActualAimable = tankGridElement.isAimable();

      // Then
      assertThat(isActualAimable, is(false));
   }

   @Test
   void testOtherDelegateMethods() {
      // Given
      Tank tank = mockTank();
      Moveable actualMoveableMock = mock(Moveable.class);
      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTankMoveable(actualMoveableMock)
            .withTank(tank)
            .build();

      // When
      boolean isTankAvoidable = tankGridElement.isAvoidable();
      tankGridElement.getTurret();
      tankGridElement.getTankEngine();
      tankGridElement.getPosition();
      tankGridElement.getForemostPosition();
      tankGridElement.getRearmostPosition();
      tankGridElement.getGrid();
      tankGridElement.getDimensionRadius();
      tankGridElement.hasGridElementDetected(actualMoveableMock, mock(Detector.class));
      tankGridElement.isDetectedBy(mock(Position.class), mock(Detector.class));
      tankGridElement.check4Collision(mock(CollisionDetectionHandler.class), mock(Position.class), Collections.emptyList());
      tankGridElement.getShape();
      tankGridElement.onCollision(Collections.emptyList());

      // Then
      verify(tank, times(2)).getTankEngine();
      verify(tank).getPosition();
      verify(tank).getTurret();
      verify(tank, times(2)).getShape();
      verify(actualMoveableMock, never()).isAvoidable();
      verify(actualMoveableMock).getForemostPosition();
      verify(actualMoveableMock).getRearmostPosition();
      verify(actualMoveableMock).getGrid();
      verify(actualMoveableMock).getDimensionRadius();
      verify(actualMoveableMock).hasGridElementDetected(any(), any());
      verify(actualMoveableMock).isDetectedBy(any(), any());
      verify(actualMoveableMock).check4Collision(any(), any(), any());
      verify(actualMoveableMock).onCollision(Collections.emptyList());
      assertThat(isTankAvoidable, is(true));
   }

   @Test
   void testAutodetect() {
      // Given
      Tank turret = mockTank();
      TankGridElement turretTower = TankGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTank(turret)
            .build();

      // When
      turretTower.autodetect();

      // Then
      verify(turret).autodetect();
   }

   private Tank mockTank() {
      Tank tank = mock(Tank.class);
      when(tank.getShape()).thenReturn(mock(TankShapeImpl.class));
      when(tank.getShape().getCenter()).thenReturn(Positions.of(5, 5));
      when(tank.getTankEngine()).thenReturn(mock(TankEngineImpl.class));
      return tank;
   }
}