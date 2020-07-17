package com.myownb3.piranha.core.battle.weapon.turret.cluster;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.TurretClusterImpl.TurretClusterBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Positions;

class TurretClusterImplTest {

   @Test
   void testBuildAndVerifyTurretCluster() {

      // Given
      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(Shape.class));
      TurretCluster turretCluster = TurretClusterBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .withTurret(turret)
            .build();

      // When
      Shape actualTurretClusterShape = turretCluster.getShape();
      int amountOfTurrets = turretCluster.getTurrets().size();

      // Then
      assertThat(actualTurretClusterShape.getCenter(), is(Positions.of(5, 5)));
      assertThat(amountOfTurrets, is(1));
   }

   @Test
   void testDelegatingMethods() {

      // Given
      Turret turret = mock(Turret.class);
      when(turret.getShape()).thenReturn(mock(Shape.class));
      TurretCluster turretCluster = TurretClusterBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .withTurret(turret)
            .build();

      // When
      turretCluster.isAcquiring();
      turretCluster.isShooting();
      turretCluster.autodetect();

      // Then
      verify(turret).isAcquiring();
      verify(turret).isShooting();
      verify(turret).autodetect();
   }

   @Test
   void testBelligerent() {

      // Given
      Turret turret = mock(Turret.class);
      Turret otherTurret = mock(Turret.class);
      when(turret.getBelligerentParty()).thenReturn(BelligerentPartyConst.REBEL_ALLIANCE);
      when(otherTurret.getBelligerentParty()).thenReturn(BelligerentPartyConst.GALACTIC_EMPIRE);
      when(turret.getShape()).thenReturn(mock(Shape.class));
      TurretCluster turretCluster = TurretClusterBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .withTurret(turret)
            .build();

      // When
      turretCluster.getBelligerentParty();
      turretCluster.isEnemy(otherTurret);

      // Then
      verify(turret, times(2)).getBelligerentParty();
   }

}
