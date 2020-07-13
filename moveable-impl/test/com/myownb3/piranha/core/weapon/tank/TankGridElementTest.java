package com.myownb3.piranha.core.weapon.tank;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.IDetector;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.DefaultGrid;
import com.myownb3.piranha.core.grid.DefaultGrid.GridBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.SimpleGridElement.SimpleGridElementBuilder;
import com.myownb3.piranha.core.grid.gridelement.evaluator.GridElementEvaluator;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape;
import com.myownb3.piranha.core.grid.gridelement.shape.position.PositionShape.PositionShapeBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileConfig;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl;
import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.weapon.turret.turretscanner.TurretScanner;

class TankGridElementTest {

   @Test
   void testGetPathFromTurret_OtherDimensionInfoHigherThanTank() {

      //  Given
      double tankHeightFromGround = 10.0;
      double turretHeightFromGround = 5;
      int tankDistanceToGround = 0;

      PositionShape turretShape = PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .build();
      Rectangle tankShape = RectangleBuilder.builder()
            .withCenter(Positions.of(5, 5, tankDistanceToGround))
            .withHeight(5)
            .withWidth(5)
            .withOrientation(Orientation.HORIZONTAL)
            .build();

      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withEngineVelocity(10)
            .withTankheightFromBottom(tankHeightFromGround)
            .withTurretHeightFromBottom(turretHeightFromGround)
            .withEvasionStateMachine(mock(EvasionStateMachine.class))
            .withTank(TankBuilder.builder()
                  .withTankEngine(mock(TankEngine.class))
                  .withTankDetector(mock(TankDetector.class))
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTurret(TankTurretBuilder.builder()
                        .withDetector(mock(IDetector.class))
                        .withTurretScanner(mock(TurretScanner.class))
                        .withGridElementEvaluator(mock(GridElementEvaluator.class))
                        .withParkingAngleEvaluator(() -> 0)
                        .withGunCarriage(SimpleGunCarriageBuilder.builder()
                              .withGun(DefaultGunBuilder.builder()
                                    .withGunProjectileType(ProjectileTypes.BULLET)
                                    .withGunShape(mock(GunShapeImpl.class))
                                    .withGunConfig(GunConfigBuilder.builder()
                                          .withProjectileConfig(mock(ProjectileConfig.class))
                                          .withRoundsPerMinute(2)
                                          .withSalveSize(2)
                                          .build())
                                    .build())
                              .withShape(turretShape)
                              .build())
                        .build())
                  .withHull(tankShape)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();

      GridElement otherGridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4, tankHeightFromGround + 5))
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(5)
                  .build())
            .build();

      // When
      List<PathSegment> actualPath = tankGridElement.getPath(otherGridElement);

      // Then
      assertThat(actualPath, is(turretShape.getPath()));
   }

   @Test
   void testGetPathFromTank_OtherDimensionInfoLowerThanTurret() {

      //  Given
      double tankHeightFromGround = 10.0;
      double turretHeightFromGround = 5;
      int tankDistanceToGround = 0;
      PositionShape turretShape = PositionShapeBuilder.builder()
            .withPosition(Positions.of(5, 5))
            .build();
      Rectangle tankShape = RectangleBuilder.builder()
            .withCenter(Positions.of(0, 0, tankDistanceToGround))
            .withHeight(5)
            .withWidth(5)
            .withOrientation(Orientation.HORIZONTAL)
            .build();

      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withEngineVelocity(10)
            .withTankheightFromBottom(tankHeightFromGround)
            .withTurretHeightFromBottom(turretHeightFromGround)
            .withEvasionStateMachine(mock(EvasionStateMachine.class))
            .withTank(TankBuilder.builder()
                  .withTankEngine(mock(TankEngine.class))
                  .withTankDetector(mock(TankDetector.class))
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTurret(TankTurretBuilder.builder()
                        .withParkingAngleEvaluator(() -> 0)
                        .withDetector(mock(IDetector.class))
                        .withTurretScanner(mock(TurretScanner.class))
                        .withGridElementEvaluator(mock(GridElementEvaluator.class))
                        .withGunCarriage(SimpleGunCarriageBuilder.builder()
                              .withGun(DefaultGunBuilder.builder()
                                    .withGunProjectileType(ProjectileTypes.BULLET)
                                    .withGunShape(mock(GunShapeImpl.class))
                                    .withGunConfig(GunConfigBuilder.builder()
                                          .withProjectileConfig(mock(ProjectileConfig.class))
                                          .withRoundsPerMinute(2)
                                          .withSalveSize(2)
                                          .build())
                                    .build())
                              .withShape(turretShape)
                              .build())
                        .build())
                  .withHull(tankShape)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();

      GridElement otherGridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4, 0))
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(5)
                  .build())
            .build();

      // When
      List<PathSegment> actualPath = tankGridElement.getPath(otherGridElement);

      // Then
      assertThat(actualPath, is(tankShape.getPath()));
   }

   @Test
   void testGetEmptyPath_OtherDimensionInfoToHigh() {

      //  Given
      double tankHeightFromGround = 10.0;
      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withEngineVelocity(10)
            .withTankheightFromBottom(tankHeightFromGround)
            .withEvasionStateMachine(mock(EvasionStateMachine.class))
            .withTank(mockTank(Positions.of(5, 5, 50), 5.0))
            .build();


      GridElement otherGridElement = SimpleGridElementBuilder.builder()
            .withGrid(mock(DefaultGrid.class))
            .withShape(CircleBuilder.builder()
                  .withRadius(5)
                  .withAmountOfPoints(4)
                  .withCenter(Positions.of(4, 4, 5000))
                  .build())
            .withDimensionInfo(DimensionInfoBuilder.builder()
                  .withDimensionRadius(5)
                  .build())
            .build();

      // When
      List<PathSegment> actualPath = tankGridElement.getPath(otherGridElement);

      // Then
      assertThat(actualPath, is(Collections.emptyList()));
   }

   @Test
   void testBuildTankGridElement_WithDimensionInfo() {

      //  Given
      double tankHeightFromGround = 10.0;
      double dimensionRadius = 5.0;
      Tank tank = mockTank(Positions.of(5, 5, 50), dimensionRadius);

      // When
      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(mock(Grid.class))
            .withEngineVelocity(10)
            .withTankheightFromBottom(tankHeightFromGround)
            .withEvasionStateMachine(mock(EvasionStateMachine.class))
            .withTank(tank)
            .build();

      // Then
      assertThat(tankGridElement.getDimensionInfo().getDimensionRadius(), is(dimensionRadius));
      assertThat(tankGridElement.getDimensionInfo().getHeightFromBottom(), is(tankHeightFromGround));
   }

   @Test
   void testBuildTankGridElement() {

      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;
      Position tankPos = Positions.of(200, 100);
      List<EndPosition> rebelTankEndPositions = Collections.singletonList(EndPositions.of(Positions.of(200, 600), 10));

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withMaxX(700)
            .withMaxY(600)
            .build();

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(350)
            .withDetectorAngle(180)
            .build();

      TankHolder tankHolder = new TankHolder();
      TankGridElement tank = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(10)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(1)
                        .withReturningMinDistance(1)
                        .withReturningAngleMargin(1)
                        .withPassingDistance(25)
                        .withPostEvasionReturnAngle(4)
                        .withDetectorConfig(DetectorConfigBuilder.builder()
                              .build())
                        .build())
                  .build())
            .withTank(TankBuilder.builder()
                  .withTankEngine(TankEngineBuilder.builder()
                        .withMoveableController(MoveableControllerBuilder.builder()
                              .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                              .withEndPositions(rebelTankEndPositions)
                              .withLazyMoveable(() -> tankHolder.getTankGridElement())
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
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
                        .build())
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
                  .build())
            .build();

      tankHolder.setAndReturnTank(tank);
      TankEngine tankEngine = tank.getTankEngine();

      // Then
      assertThat(tank.getPosition(), is(tankPos));
      assertThat(tankEngine, is(notNullValue()));
   }

   @Test
   void testOtherDelegateMethods() {
      // Given
      Tank tank = mockTank(Positions.of(5, 5), 5.0);
      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(GridBuilder.builder()
                  .withMaxX(5)
                  .withMinX(5)
                  .build())
            .withTank(tank)
            .withEngineVelocity(5)
            .build();

      // When
      boolean isTankAvoidable = tankGridElement.isAvoidable();
      tankGridElement.getTurret();
      tankGridElement.getTankEngine();
      tankGridElement.getShape();
      Belligerent belligerent = mock(Belligerent.class);
      when(belligerent.getBelligerentParty()).thenReturn(BelligerentPartyConst.GALACTIC_EMPIRE);
      tankGridElement.isEnemy(belligerent);
      tankGridElement.onCollision(Collections.emptyList());
      tankGridElement.autodetect();
      tankGridElement.isDestroyed();
      tankGridElement.getBelligerentParty();

      // Then
      verify(tank, times(3)).getBelligerentParty();
      verify(tank).isDestroyed();
      verify(tank).autodetect();
      verify(tank).getTankEngine();
      verify(tank).getTurret();
      verify(tank).onCollision(Collections.emptyList());
      assertThat(isTankAvoidable, is(true));
   }

   private Tank mockTank(Position shapeCenter, double dimensionRadius) {
      Tank tank = mock(Tank.class);
      TankShapeImpl shape = mock(TankShapeImpl.class);
      when(tank.getShape()).thenReturn(shape);
      when(shape.getCenter()).thenReturn(shapeCenter);
      when(shape.getDimensionRadius()).thenReturn(dimensionRadius);
      TankEngineImpl tankEngine = mock(TankEngineImpl.class);
      when(tank.getTankEngine()).thenReturn(tankEngine);
      when(tank.getBelligerentParty()).thenReturn(BelligerentPartyConst.GALACTIC_EMPIRE);
      return tank;
   }
}
