package com.myownb3.piranha.core.weapon.tank.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;

class TankDetectorImplTest {

   @Test
   void testIsUnderFire_BecauseEnemyAndFriendDetected() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankGridElement()
            .addDetectedProjectile(true)
            .addDetectedProjectile(false)
            .withGridElementDetector()
            .build();

      // When
      boolean actualIsUnderFire = tcb.tankDetector.isUnderFire();

      // Then
      assertThat(actualIsUnderFire, is(true));
   }

   @Test
   void testIsUnderFire_BecauseEnemyDetected() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankGridElement()
            .addDetectedProjectile(true)
            .withGridElementDetector()
            .build();

      // When
      boolean actualIsUnderFire = tcb.tankDetector.isUnderFire();

      // Then
      assertThat(actualIsUnderFire, is(true));
   }

   @Test
   void testIsNotUnderFire_BecauseNothingDetected() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withTankGridElement()
            .withGridElementDetector()
            .build();

      // When
      boolean actualIsUnderFire = tcb.tankDetector.isUnderFire();

      // Then
      assertThat(actualIsUnderFire, is(false));
   }

   private static class TestCaseBuilder {
      private GridElementDetector gridElementDetector;
      private TankGridElement tankGridElement;
      private TankDetectorImpl tankDetector;
      private List<GridElement> detectedGridElements;

      private TestCaseBuilder() {
         detectedGridElements = new ArrayList<>();
      }

      public TestCaseBuilder addDetectedProjectile(boolean isEnemy) {
         ProjectileGridElement projectile = mockProjectileGridElement(isEnemy);
         this.detectedGridElements.add(projectile);
         return this;
      }

      private TestCaseBuilder withTankGridElement() {
         tankGridElement = mock(TankGridElement.class);
         return this;
      }

      private TestCaseBuilder withGridElementDetector() {
         gridElementDetector = mock(GridElementDetector.class);
         when(gridElementDetector.getDetectedGridElement(eq(tankGridElement))).thenReturn(detectedGridElements);
         return this;
      }

      private TestCaseBuilder build() {
         tankDetector = TankDetectorBuilder.builder()
               .withTankGridElement(() -> tankGridElement)
               .withGridElementDetector(gridElementDetector)
               .build();
         return this;
      }

      private ProjectileGridElement mockProjectileGridElement(boolean isEnemy) {
         ProjectileGridElement projectileGridElement = mock(ProjectileGridElement.class);
         when(projectileGridElement.isEnemy(eq(tankGridElement))).thenReturn(isEnemy);
         return projectileGridElement;
      }
   }
}
