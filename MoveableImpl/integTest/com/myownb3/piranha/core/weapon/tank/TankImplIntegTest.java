
package com.myownb3.piranha.core.weapon.tank;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.weapon.tank.turret.TankTurretBuilder;

class TankImplIntegTest {

   @Test
   void testBuildDefaultTankEngine() {

      // Given
      Position tankPos = Positions.of(10, 10);
      int tankWidth = 10;
      int tankHeight = 30;
      Grid grid = mock(Grid.class);

      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(350)
            .withDetectorAngle(180)
            .build();

      TankHolder tankHolder = new TankHolder();
      TankDetectorImpl tankDetector = spy(TankDetectorBuilder.builder()
            .withTankGridElement(() -> tankHolder.getTankGridElement())
            .withGridElementDetector(GridElementDetectorBuilder.builder()
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
                  .withDetectingGridElementFilter(FilterGridElementsMovingAway.of(() -> tankHolder.getTankGridElement()))
                  .build())
            .build());
      Tank tank = TankBuilder.builder()
            .withTankEngine(TankEngineBuilder.builder()
                  .withMoveableController(MoveableControllerBuilder.builder()
                        .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                        .withEndPositions(Collections.singletonList(EndPositions.of(50, 50.1)))
                        .withLazyMoveable(() -> tankHolder.getTankGridElement())
                        .withPostMoveForwardHandler(res -> {
                        })
                        .build())
                  .build())
            .withTankDetector(tankDetector)
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withTurret(TankTurretBuilder.builder()
                  .withParkingAngleEvaluator(() -> tankHolder.getPosition().getDirection().getAngle())
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(detectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(detectorConfig.getDetectorAngle())
                        .withDetectorReach(detectorConfig.getDetectorReach())
                        .withEvasionAngle(detectorConfig.getDetectorAngle())
                        .withEvasionDistance(detectorConfig.getEvasionDistance())
                        .build())
                  .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(4)
                        .withGun(BulletGunBuilder.builder()
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(2)
                                    .withRoundsPerMinute(250)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(3))
                                          .withVelocity(3)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(tankPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .withMuzzleBreak(RectangleBuilder.builder()
                                          .withHeight(gunWidth * 1.5)
                                          .withWidth(gunWidth * 1.5)
                                          .withCenter(tankPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(tankPos)
                              .build())
                        .build())
                  .build())
            .withHull(RectangleBuilder.builder()
                  .withCenter(tankPos)
                  .withHeight(tankHeight)
                  .withWidth(tankWidth)
                  .withOrientation(Orientation.HORIZONTAL)
                  .build())
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
            .build();

      tankHolder.setAndReturnTank(tank);
      TankGridElement tankGridElement = mock(TankGridElement.class);
      when(tankGridElement.moveForward2EndPos()).thenReturn(mock(MoveResult.class));
      tankHolder.setTankGridElement(tankGridElement);

      // When
      tank.autodetect();

      // Then
      verify(tank.getTankEngine().getMoveable()).moveForward2EndPos();
      verify(tankDetector).autodetect();
   }
}
