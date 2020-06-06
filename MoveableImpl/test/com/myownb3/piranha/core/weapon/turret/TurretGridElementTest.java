package com.myownb3.piranha.core.weapon.turret;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl;

class TurretGridElementTest {

   @Test
   void testOtherDelegateMethods() {
      // Given
      Turret turret = mockTurret();
      GridElement actualGridElementMock = mock(GridElement.class);
      TurretGridElement turretTower = TurretGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTurretGridElement(actualGridElementMock)
            .withTurret(turret)
            .build();

      // When
      turretTower.getPosition();
      turretTower.getForemostPosition();
      turretTower.getRearmostPosition();
      turretTower.getGrid();
      turretTower.getDimensionRadius();
      turretTower.hasGridElementDetected(actualGridElementMock, mock(Detector.class));
      turretTower.isDetectedBy(mock(Position.class), mock(Detector.class));
      turretTower.check4Collision(mock(CollisionDetectionHandler.class), mock(Position.class), Collections.emptyList());
      turretTower.getTurretStatus();
      turretTower.getShape();
      turretTower.onCollision(Collections.emptyList());

      // Then
      verify(actualGridElementMock).getPosition();
      verify(actualGridElementMock).getForemostPosition();
      verify(actualGridElementMock).getRearmostPosition();
      verify(actualGridElementMock).getGrid();
      verify(actualGridElementMock).getDimensionRadius();
      verify(actualGridElementMock).hasGridElementDetected(any(), any());
      verify(actualGridElementMock).isDetectedBy(any(), any());
      verify(actualGridElementMock).check4Collision(any(), any(), any());
      verify(turret).getTurretStatus();
      verify(turret, times(4)).getShape();
      verify(actualGridElementMock).onCollision(Collections.emptyList());

   }

   @Test
   void testAutodetect() {
      // Given
      Turret turret = mockTurret();
      TurretGridElement turretTower = TurretGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTurret(turret)
            .build();

      // When
      turretTower.autodetect();

      // Then
      verify(turret).autodetect();
   }

   @Test
   void testGetGunCarriage() {
      // Given
      Turret turret = mockTurret();
      TurretGridElement turretTower = TurretGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTurret(turret)
            .build();

      // When
      turretTower.getGunCarriage();

      // Then
      verify(turret).getGunCarriage();
   }

   private Turret mockTurret() {
      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(Positions.of(5, 5));
      return turret;
   }

}
