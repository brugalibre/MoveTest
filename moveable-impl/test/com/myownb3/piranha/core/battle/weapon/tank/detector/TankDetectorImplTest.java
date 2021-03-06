package com.myownb3.piranha.core.battle.weapon.tank.detector;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareDispenser.DecoyFlareDispenserBuilder;
import com.myownb3.piranha.core.battle.weapon.countermeasure.MissileCounterMeasureSystemImpl.MissileCounterMeasureSystemBuilder;
import com.myownb3.piranha.core.battle.weapon.countermeasure.config.DecoyFlareConfigImpl.DecoyFlareConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement.ProjectileGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileImpl.ProjectileBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.collision.detection.handler.CollisionDetectionResultImpl;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.GridElementDetector;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;

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

   @Test
   void testBuildWithDecoyFlareDispenser() {

      // Given
      Supplier<TankGridElement> tankGridElementSupplier = spy(new TestSupplier());
      TankDetector tankDetector = TankDetectorBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinY(5)
                  .build())
            .withTankGridElement(tankGridElementSupplier)
            .withDetector(mock(Detector.class))
            .withMissileCounterMeasureSystem(MissileCounterMeasureSystemBuilder.builder()
                  .withDetector(mock(Detector.class))
                  .withGrid(mock(Grid.class))
                  .withGridElementSupplier(tankGridElementSupplier)
                  .withDecoyFlareDispenser(DecoyFlareDispenserBuilder.builder()
                        .withDecoyFlareConfig(DecoyFlareConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
                              .withVelocity(5)
                              .build())
                        .withMinTimeBetweenDispensing(50)
                        .build())
                  .build())
            .build();
      // When
      tankDetector.autodetect();

      // Then
      verify(tankGridElementSupplier, times(4)).get();
   }

   @Test
   void testAndScan() {

      // Given
      Supplier<TankGridElement> tankGridElementSupplier = spy(new TestSupplier());
      List<GridElement> detectedGridElements = Collections.singletonList(mock(ProjectileGridElement.class));
      Grid grid = mock(Grid.class);
      when(grid.getAllAvoidableGridElementsWithinDistance(any(), anyInt())).thenReturn(detectedGridElements);
      TankDetector tankDetector = TankDetectorBuilder.builder()
            .withGrid(grid)
            .withTankGridElement(tankGridElementSupplier)
            .withDetector(mock(Detector.class))
            .withMissileCounterMeasureSystem(MissileCounterMeasureSystemBuilder.builder()
                  .withDetector(mock(Detector.class))
                  .withGrid(grid)
                  .withGridElementSupplier(tankGridElementSupplier)
                  .withDecoyFlareDispenser(DecoyFlareDispenserBuilder.builder()
                        .withDecoyFlareConfig(DecoyFlareConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(5))
                              .withVelocity(5)
                              .build())
                        .withMinTimeBetweenDispensing(50)
                        .build())
                  .build())
            .build();
      // When
      tankDetector.autodetect();

      // Then
      verify(tankGridElementSupplier, times(4)).get();
   }

   private static class TestSupplier implements Supplier<TankGridElement> {
      @Override
      public TankGridElement get() {
         return mock(TankGridElement.class);
      }
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
         DimensionInfo dimensionInfo = getDefaultDimensionInfo(1);
         when(tankGridElement.getDimensionInfo()).thenReturn(dimensionInfo);
         return this;
      }

      private TestCaseBuilder withGridElementDetector() {
         gridElementDetector = mock(GridElementDetector.class);
         when(gridElementDetector.getDetectedGridElements(eq(tankGridElement))).thenReturn(detectedGridElements);
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
               .withDetectingGridElementFilter(FilterGridElementsMovingAway.of(() -> tankGridElement))
               .build();
         return this;
      }

      private TestCaseBuilder build() {
         tankDetector = TankDetectorBuilder.builder()
               .withGrid(mock(Grid.class))
               .withTankGridElement(() -> tankGridElement)
               .withGridElementDetector(gridElementDetector)
               .build();
         return this;
      }

      private ProjectileGridElement mockProjectileGridElement(Position position) {
         return ProjectileGridElementBuilder.builder()
               .withGrid(grid)
               .withProjectile(ProjectileBuilder.builder()
                     .withHealth(10)
                     .withDamage(10)
                     .withShape(PositionShapeBuilder.builder()
                           .withPosition(position)
                           .build())
                     .withProjectileTypes(ProjectileTypes.BULLET)
                     .withProjectileConfig(mock(ProjectileConfig.class))
                     .build())
               .withVelocity(5)
               .withDimensionInfo(getDefaultDimensionInfo(1))
               .build();
      }
   }
}
