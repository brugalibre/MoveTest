package com.myownb3.piranha.core.weapon.tank.detector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;

class TankDetectorImplTest {

   @Test
   void testIsUnderFire_BecauseOneProjectileDoesNotMoveAway() {

      // Given
      Position tankPosition = Positions.of(0, 0);
      Position obstaclePos1 = Positions.of(0, 1);
      Position obstaclePos2 = Positions.of(0, 3);
      Position obstaclePos3 = Positions.of(0, 5).rotate(180);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(100)
                  .withMaxY(100)
                  .withMinX(-5)
                  .withMinY(-5)
                  .withCollisionDetectionHandler((a, b, c) -> new CollisionDetectionResultImpl(c))
                  .build())
            .withTankGridElement(tankPosition)
            .addDetectedProjectile(obstaclePos1)
            .addDetectedProjectile(obstaclePos2)
            .addDetectedProjectile(obstaclePos3)
            .withTrippleDetectorCluster()
            .build();

      // When
      tcb.tankDetector.autodetect();
      boolean actualIsUnderFire = tcb.tankDetector.isUnderFire();

      // Then
      assertThat(actualIsUnderFire, is(true));
   }

   @Test
   void testIsNotUnderFire_BecauseProjectileMovesAway() {

      // Given
      Position posNot2Detect = Positions.of(0, 1);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withTankGridElement(Positions.of(0, 0))
            .addDetectedProjectile(posNot2Detect)
            .withTrippleDetectorCluster()
            .build();

      // When
      tcb.tankDetector.autodetect();
      boolean actualIsUnderFire = tcb.tankDetector.isUnderFire();

      // Then
      assertThat(actualIsUnderFire, is(false));
   }

   @Test
   void testIsUnderFire_BecauseProjectileMovesNotAway() {

      // Given
      Position pos2Detect = Positions.of(0, 1).rotate(180);
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGrid(GridBuilder.builder()
                  .build())
            .withTankGridElement(Positions.of(0, 0))
            .addDetectedProjectile(pos2Detect)
            .withTrippleDetectorCluster()
            .build();

      // When
      tcb.tankDetector.autodetect();
      boolean actualIsUnderFire = tcb.tankDetector.isUnderFire();

      // Then
      assertThat(actualIsUnderFire, is(true));
   }

   @Test
   void testIsUnderFire_BecauseEnemyAndFriendDetected() {

      // Given
      TestCaseBuilder tcb = new TestCaseBuilder()
            .withGrid(mock(Grid.class))
            .withTankGridElement(Positions.of(0, 0))
            .addDetectedProjectile(Positions.of(0, 0))
            .addDetectedProjectile(Positions.of(0, 0))
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
            .withGrid(mock(Grid.class))
            .withTankGridElement(Positions.of(0, 0))
            .addDetectedProjectile(Positions.of(0, 0))
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
            .withTankGridElement(Positions.of(0, 0))
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
      private Grid grid;
      private List<GridElement> detectedGridElements;

      private TestCaseBuilder() {
         detectedGridElements = new ArrayList<>();
      }

      public TestCaseBuilder addDetectedProjectile(Position position) {
         ProjectileGridElement projectile = mockProjectileGridElement(position);
         this.detectedGridElements.add(projectile);
         return this;
      }

      private TestCaseBuilder withTankGridElement(Position position) {
         tankGridElement = mock(TankGridElement.class);
         when(tankGridElement.getBelligerentParty()).thenReturn(BelligerentPartyConst.REBEL_ALLIANCE);
         when(tankGridElement.getPosition()).thenReturn(position);
         when(tankGridElement.getForemostPosition()).thenReturn(position);
         return this;
      }

      private TestCaseBuilder withGridElementDetector() {
         gridElementDetector = mock(GridElementDetector.class);
         when(gridElementDetector.getDetectedGridElement(eq(tankGridElement))).thenReturn(detectedGridElements);
         return this;
      }

      private TestCaseBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      private TestCaseBuilder withTrippleDetectorCluster() {
         gridElementDetector = GridElementDetectorBuilder.builder()
               .withGrid(grid)
               .withDetector(TrippleDetectorClusterBuilder.builder()
                     .withCenterDetector(DetectorBuilder.builder()
                           .withAngleInc(1)
                           .withDetectorAngle(90)
                           .withDetectorReach(400)
                           .withEvasionAngle(90)
                           .withEvasionDistance(400)
                           .build())
                     .withLeftSideDetector(DetectorBuilder.builder()
                           .withAngleInc(1)
                           .withDetectorAngle(90)
                           .withDetectorReach(400)
                           .withEvasionAngle(90)
                           .withEvasionDistance(400)
                           .build(), 90)
                     .withRightSideDetector(DetectorBuilder.builder()
                           .withAngleInc(1)
                           .withDetectorAngle(90)
                           .withDetectorReach(400)
                           .withEvasionAngle(90)
                           .withEvasionDistance(400)
                           .build(), 90)
                     .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS_WITH_DETECTION)
                     .withAutoDetectionStrategyHandler()
                     .build())
               .withDetectingGridElementFilter(FilterGridElementsMovingAway.of(tankGridElement))
               .build();
         return this;
      }

      private TestCaseBuilder build() {
         tankDetector = TankDetectorBuilder.builder()
               .withTankGridElement(() -> tankGridElement)
               .withGridElementDetector(gridElementDetector)
               .build();
         return this;
      }

      private ProjectileGridElement mockProjectileGridElement(Position position) {
         ProjectileGridElement projectileGridElement = ProjectileGridElementBuilder.builder()
               .withGrid(grid)
               .withHealth(10)
               .withDamage(10)
               .withPosition(position)
               .withShape(PositionShapeBuilder.builder()
                     .withPosition(position)
                     .build())
               .withVelocity(5)
               .build();
         return projectileGridElement;
      }
   }
}
