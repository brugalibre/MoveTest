package com.myownb3.piranha.core.weapon.turret;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShapeImpl;

class TurretGridElementTest {
   @Test
   void testIsTurretAvoidable() {
      // Given
      Turret turret = mockTurret(Positions.of(5, 5));
      TurretGridElement turretTower = TurretGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTurret(turret)
            .build();

      // When
      boolean actualIsAvoidable = turretTower.isAvoidable();

      // Then
      assertThat(actualIsAvoidable, is(true));
   }

   @Test
   void testBuildTurretGridElement_WithDimensionInfo() {

      double heightFromBottom = 10.0;
      double distanceToGround = 50.0;
      Turret turret = mockTurret(Positions.of(5, 5, 50));
      TurretGridElement turretTower = TurretGridElementBuilder.builder()
            .withHeightFromBottom(heightFromBottom)
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTurret(turret)
            .build();

      // Then
      assertThat(turretTower.getPosition().getZ(), is(distanceToGround));
      assertThat(turretTower.getDimensionInfo().getHeightFromBottom(), is(heightFromBottom));
   }

   @Test
   void testOtherDelegateMethods() {
      // Given
      Turret turret = mockTurret(Positions.of(5, 5));
      TurretGridElement turretTower = TurretGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTurret(turret)
            .build();

      // When
      turretTower.isAcquiring();
      turretTower.getShape();
      Belligerent belligerent = mock(Belligerent.class);
      turretTower.isEnemy(belligerent);
      turretTower.getBelligerentParty();
      turretTower.isShooting();

      // Then
      verify(turret).isAcquiring();
      verify(turret).isEnemy(belligerent);
      verify(turret).getBelligerentParty();
      verify(turret, times(5)).getShape();
      verify(turret).isShooting();
   }

   @Test
   void testAutodetect() {
      // Given
      Turret turret = mockTurret(Positions.of(5, 5));
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

   private Turret mockTurret(Position position) {
      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(TurretShapeImpl.class));
      when(turret.getShape().getCenter()).thenReturn(position);
      return turret;
   }

}
