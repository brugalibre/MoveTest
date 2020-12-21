
package com.myownb3.piranha.core.weapon.tank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl.DefaultGunCarriageBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.MoveResult;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.moveables.engine.MoveableEngineImpl;
import com.myownb3.piranha.core.moveables.engine.MoveableEngineImpl.MoveableEngineBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;

class TankImplIntegTest {

   @Test

   void testBuildAndMoveTank() {

      // Given
      Position tankStartPos = Positions.of(5, 5);
      Position expectedTanEndPos = Positions.of(5, 5.5);
      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(50)
                  .withMinX(50)
                  .withDefaultCollisionDetectionHandler()
                  .build())
            .withTank(mockTank(10.0, TankShapeBuilder.builder()
                  .withHull(RectangleBuilder.builder()
                        .withCenter(tankStartPos)
                        .withHeight(5)
                        .withWidth(5)
                        .build())
                  .withTurretShape(CircleBuilder.builder()
                        .withCenter(tankStartPos)
                        .withRadius(5)
                        .withAmountOfPoints(5)
                        .build())
                  .build()))
            .withEngineVelocity(5)
            .build();

      // When
      tankGridElement.moveForward();

      // Then
      assertThat(tankGridElement.getPosition(), is(expectedTanEndPos));
   }

   private Tank mockTank(double dimensionRadius, TankShape shape) {
      Tank tank = mock(Tank.class);
      when(tank.getShape()).thenReturn(shape);
      MoveableEngineImpl moveableEngine = mock(MoveableEngineImpl.class);
      when(tank.getMoveableEngine()).thenReturn(moveableEngine);
      return tank;
   }

   @Test
   void testBuildDefaultMoveableEngine() {

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
            .build());
      Tank tank = TankBuilder.builder()
            .withMoveableEngine(MoveableEngineBuilder.builder()
                  .withVelocity(10)
                  .withDefaultEngineStateHandler()
                  .withEngineAudio(EngineAudioBuilder.builder()
                        .withDefaultAudio()
                        .build())
                  .withMoveableController(MoveableControllerBuilder.builder()
                        .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                        .withEndPositions(Collections.singletonList(EndPositions.of(50, 50.1)))
                        .withLazyMoveable(() -> tankHolder.getTankGridElement())
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
                  .withGunCarriage(DefaultGunCarriageBuilder.builder()
                        .withRotationSpeed(4)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
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
                                    .withMuzzleBrake(RectangleBuilder.builder()
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
      verify(tank.getMoveableEngine().getMoveable()).moveForward2EndPos();
      verify(tankDetector).autodetect();
   }
}
