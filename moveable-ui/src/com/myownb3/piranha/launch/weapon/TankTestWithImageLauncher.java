package com.myownb3.piranha.launch.weapon;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.application.battle.util.MoveableAdder.MoveableAdderBuilder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl.DefaultGunCarriageBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.TurretClusterImpl.TurretClusterBuilder;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.UIRefresher;
import com.myownb3.piranha.ui.constants.ImageConstants;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.weapon.tank.TankGridElementImagePainter;
import com.myownb3.piranha.ui.render.impl.weapon.turret.TurretGridElementImagePainter;

public class TankTestWithImageLauncher {
   private static final int MAX_Y = 820;
   private static final int MAX_X = 850;
   private static final int padding = 0;

   public static void main(String[] args) {
      TankTestWithImageLauncher launcher = new TankTestWithImageLauncher();
      launcher.launch();
   }

   private void launch() {

      int width = 30;
      Position turretNorthPos = Positions.of(70, 70).rotate(-60);
      Position southNorthPos = Positions.of(700, 450).rotate(120);

      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      Position tankPos = Positions.of(550, 100).rotate(180);
      Position tankTurretPos = Positions.of(410, 100).rotate(180);

      List<EndPosition> endPositions = new ArrayList<>();
      endPositions.add(EndPositions.of(Positions.of(440, 100), 10));
      endPositions.add(EndPositions.of(Positions.of(440, 300), 10));
      endPositions.add(EndPositions.of(Positions.of(550, 300), 10));
      endPositions.add(EndPositions.of(tankPos, 10));

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withCollisionDetectionHandler(BouncingCollisionDetectionHandlerBuilder.builder()
                  .withBouncedPositionEvaluator(new BouncedPositionEvaluatorImpl())
                  .build())
            .withMaxX(MAX_X)
            .withMaxY(MAX_Y)
            .withMinX(padding)
            .withMinY(padding)
            .build();

      double turretRotationSpeed = 6;
      int tankWidth = 40;
      int tankHeight = 90;
      int tankWithImageWidth = 70;
      int gunCarriageRadius = 10;
      int gunCarriageWithImageRadius = gunCarriageRadius * 3;
      double gunHeight = 25;
      double gunWithImageHeight = gunHeight * 2d;
      double gunWidth = 7;
      double gunWithImageWidth = gunWidth * 2d;
      int battleTankGunCarriageRadius = 8;
      int battleTankWithImageGunCarriageRadius = battleTankGunCarriageRadius * 3;
      double battleTankWithImageGunHeight = gunWithImageHeight;
      double battleTankWithImageGunWidth = gunWithImageWidth;
      int projectileVelocity = 50;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
            .withDetectorAngle(360)
            .build();

      DetectorConfig missileDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(200)
            .withDetectorAngle(120)
            .build();

      TankHolder tankHolder = new TankHolder();

      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(12)
            .withTankheightFromBottom(tankTurretHeight)
            .withTurretHeightFromBottom(tankTurretHeight)
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
                        .withVelocity(12)
                        .withDefaultEngineStateHandler()
                        .withEngineAudio(EngineAudioBuilder.builder()
                              .withDefaultAudio()
                              .withEngineMoveAudio(AudioClipBuilder.builder()
                                    .withRestartRunningAudio(false)
                                    .withAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                                    .build())
                              .build())
                        .withMoveableController(MoveableControllerBuilder.builder()
                              .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                              .withEndPositions(endPositions)
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
                              .withRotationSpeed(turretRotationSpeed)
                              .withGun(DefaultGunBuilder.builder()
                                    .withGunProjectileType(ProjectileTypes.MISSILE)
                                    .withGunConfig(GunConfigBuilder.builder()
                                          .withAudioClip(AudioClipBuilder.builder()
                                                .withAudioResource(AudioConstants.MISSILE_SHOT_SOUND)
                                                .build())
                                          .withSalveSize(1)
                                          .withRoundsPerMinute(100)
                                          .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                .withDimensionInfo(DimensionInfoBuilder.builder()
                                                      .withDimensionRadius(3)
                                                      .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                                      .build())
                                                .withVelocity(projectileVelocity)
                                                .withTargetGridElementEvaluator(TargetGridElementEvaluatorBuilder.builder()
                                                      .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                                                      .withDetector(DetectorBuilder.builder()
                                                            .withDetectorAngle(missileDetectorConfig.getDetectorAngle())
                                                            .withDetectorReach(missileDetectorConfig.getDetectorReach())
                                                            .build())
                                                      .withGridElementEvaluator(
                                                            (position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                                                      .build())
                                                .withProjectileDamage(300)
                                                .build())
                                          .build())
                                    .withGunShape(GunShapeBuilder.builder()
                                          .withBarrel(RectangleBuilder.builder()
                                                .withHeight(gunWithImageHeight)
                                                .withWidth(gunWithImageWidth)
                                                .withCenter(tankTurretPos)
                                                .withOrientation(Orientation.HORIZONTAL)
                                                .build())
                                          .build())
                                    .build())
                              .withShape(RectangleBuilder.builder()
                                    .withWidth(gunCarriageWithImageRadius)
                                    .withHeight(gunCarriageWithImageRadius)
                                    .withCenter(tankTurretPos)
                                    .build())
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(tankPos)
                        .withHeight(tankHeight)
                        .withWidth(tankWithImageWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();


      int battleShipParkingAngle = -90;
      double battleTankHeight = tankHeight * 2d;
      double battleTankWidth = 2 * tankWidth / 3d;
      double battleTankWithImageHeight = battleTankHeight;
      double battleTankWithImageWidth = 2 * battleTankWidth;
      TankHolder battleShipHolder = new TankHolder();
      Position battleShipPos = Positions.of(150, 600, tankHeightFromGround).rotate(90);
      Position tankTurret1Pos = Positions.of(140, 100).rotate(180);
      Position tankTurret2Pos = Positions.of(30, 100).rotate(180);

      List<EndPosition> battleShipEndPositions = new ArrayList<>();
      battleShipEndPositions.add(EndPositions.of(Positions.of(750, 600), 10));
      battleShipEndPositions.add(EndPositions.of(battleShipPos, 10));

      TankGridElement battleShipGridElement = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(10)
            .withTankheightFromBottom(tankTurretHeight)
            .withTurretHeightFromBottom(tankTurretHeight)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(180)
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
                        .withVelocity(10)
                        .withDefaultEngineStateHandler()
                        .withEngineAudio(EngineAudioBuilder.builder()
                              .withDefaultAudio()
                              .withEngineMoveAudio(AudioClipBuilder.builder()
                                    .withRestartRunningAudio(false)
                                    .withAudioResource(AudioConstants.TANK_TRACK_RATTLE_VAR2)
                                    .build())
                              .build())
                        .withMoveableController(MoveableControllerBuilder.builder()
                              .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                              .withEndPositions(battleShipEndPositions)
                              .withLazyMoveable(() -> battleShipHolder.getTankGridElement())
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> battleShipHolder.getTankGridElement())
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
                  .withTurret(TurretClusterBuilder.builder()
                        .withPosition(battleShipPos)
                        .withTurret(TankTurretBuilder.builder()
                              .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                              .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionBackward4Distance(40))
                              .withDetector(DetectorBuilder.builder()
                                    .withAngleInc(detectorConfig.getEvasionAngleInc())
                                    .withDetectorAngle(detectorConfig.getDetectorAngle())
                                    .withDetectorReach(detectorConfig.getDetectorReach())
                                    .withEvasionAngle(detectorConfig.getDetectorAngle())
                                    .withEvasionDistance(detectorConfig.getEvasionDistance())
                                    .build())
                              .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                              .withGunCarriage(DefaultGunCarriageBuilder.builder()
                                    .withRotationSpeed(turretRotationSpeed)
                                    .withGun(DefaultGunBuilder.builder()
                                          .withGunProjectileType(ProjectileTypes.LASER_BEAM)
                                          .withGunConfig(GunConfigBuilder.builder()
                                                .withAudioClip(AudioClipBuilder.builder()
                                                      .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                                      .build())
                                                .withSalveSize(2)
                                                .withRoundsPerMinute(150)
                                                .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                      .withDimensionInfo(DimensionInfoBuilder.builder()
                                                            .withDimensionRadius(2)
                                                            .withHeightFromBottom(tankTurretHeight)
                                                            .build())
                                                      .withVelocity(projectileVelocity)
                                                      .build())
                                                .build())
                                          .withGunShape(GunShapeBuilder.builder()
                                                .withBarrel(RectangleBuilder.builder()
                                                      .withHeight(battleTankWithImageGunHeight)
                                                      .withWidth(battleTankWithImageGunWidth)
                                                      .withCenter(tankTurret1Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .build())
                                          .build())
                                    .withShape(RectangleBuilder.builder()
                                          .withWidth(battleTankWithImageGunCarriageRadius)
                                          .withHeight(battleTankWithImageGunCarriageRadius)
                                          .withCenter(tankTurret1Pos)
                                          .build())
                                    .build())
                              .build())
                        .withTurret(TankTurretBuilder.builder()
                              .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                              .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionForward4Distance(50))
                              .withDetector(DetectorBuilder.builder()
                                    .withAngleInc(detectorConfig.getEvasionAngleInc())
                                    .withDetectorAngle(detectorConfig.getDetectorAngle())
                                    .withDetectorReach(detectorConfig.getDetectorReach())
                                    .withEvasionAngle(detectorConfig.getDetectorAngle())
                                    .withEvasionDistance(detectorConfig.getEvasionDistance())
                                    .build())
                              .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                              .withGunCarriage(DefaultGunCarriageBuilder.builder()
                                    .withRotationSpeed(turretRotationSpeed)
                                    .withGun(DefaultGunBuilder.builder()
                                          .withGunProjectileType(ProjectileTypes.LASER_BEAM)
                                          .withGunConfig(GunConfigBuilder.builder()
                                                .withAudioClip(AudioClipBuilder.builder()
                                                      .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                                      .build())
                                                .withSalveSize(2)
                                                .withRoundsPerMinute(180)
                                                .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                      .withDimensionInfo(DimensionInfoBuilder.builder()
                                                            .withDimensionRadius(2)
                                                            .withHeightFromBottom(tankTurretHeight)
                                                            .build())
                                                      .withVelocity(projectileVelocity)
                                                      .build())
                                                .build())
                                          .withGunShape(GunShapeBuilder.builder()
                                                .withBarrel(RectangleBuilder.builder()
                                                      .withHeight(battleTankWithImageGunHeight)
                                                      .withWidth(battleTankWithImageGunWidth)
                                                      .withCenter(tankTurret2Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .build())
                                          .build())
                                    .withShape(RectangleBuilder.builder()
                                          .withWidth(battleTankWithImageGunCarriageRadius)
                                          .withHeight(battleTankWithImageGunCarriageRadius)
                                          .withCenter(tankTurret2Pos)
                                          .build())
                                    .build())
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(battleShipPos)
                        .withHeight(battleTankWithImageHeight)
                        .withWidth(battleTankWithImageWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withTankStrategy(TankStrategy.ALWAYS_MOVE_AND_SHOOT)
                  .build())
            .build();

      TurretGridElement northTurretGridElement = TurretGridElementBuilder.builder()
            .withGrid(grid)
            .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(detectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(detectorConfig.getDetectorAngle())
                        .withDetectorReach(detectorConfig.getDetectorReach())
                        .withEvasionAngle(detectorConfig.getDetectorAngle())
                        .withEvasionDistance(detectorConfig.getEvasionDistance())
                        .build())
                  .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                  .withGunCarriage(DefaultGunCarriageBuilder.builder()
                        .withRotationSpeed(turretRotationSpeed)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withAudioClip(AudioClipBuilder.builder()
                                          .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                                          .build())
                                    .withSalveSize(3)
                                    .withRoundsPerMinute(250)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimensionInfo(DimensionInfoBuilder.builder()
                                                .withDimensionRadius(3)
                                                .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                                .build())
                                          .withVelocity(projectileVelocity)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunWithImageHeight)
                                          .withWidth(gunWithImageWidth)
                                          .withCenter(turretNorthPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(RectangleBuilder.builder()
                              .withWidth(gunCarriageWithImageRadius)
                              .withHeight(gunCarriageWithImageRadius)
                              .withCenter(turretNorthPos)
                              .build())
                        .build())
                  .build())
            .build();
      TurretGridElement southTurretGridElement = TurretGridElementBuilder.builder()
            .withGrid(grid)
            .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(detectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(detectorConfig.getDetectorAngle())
                        .withDetectorReach(detectorConfig.getDetectorReach())
                        .withEvasionAngle(detectorConfig.getDetectorAngle())
                        .withEvasionDistance(detectorConfig.getEvasionDistance())
                        .build())
                  .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                  .withGunCarriage(DefaultGunCarriageBuilder.builder()
                        .withRotationSpeed(turretRotationSpeed)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.BULLET)
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withAudioClip(AudioClipBuilder.builder()
                                          .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                                          .build())
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(250)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withProjectileDamage(30)
                                          .withDimensionInfo(DimensionInfoBuilder.builder()
                                                .withDimensionRadius(3)
                                                .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                                .build())
                                          .withVelocity(projectileVelocity)
                                          .build())
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunWithImageHeight)
                                          .withWidth(gunWithImageWidth)
                                          .withCenter(southNorthPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(RectangleBuilder.builder()
                              .withWidth(gunCarriageWithImageRadius)
                              .withHeight(gunCarriageWithImageRadius)
                              .withCenter(southNorthPos)
                              .build())
                        .build())
                  .build())
            .build();

      tankHolder.setAndReturnTank(tankGridElement);
      tankHolder.setTankGridElement(tankGridElement);
      battleShipHolder.setTankGridElement(battleShipGridElement);
      battleShipHolder.setAndReturnTank(battleShipGridElement);

      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      mainWindow.withBackground(ImageConstants.DEFAULT_BACKGROUND);

      List<Renderer<? extends GridElement>> renderers = new ArrayList<>();
      renderers.add(new TurretGridElementImagePainter(northTurretGridElement, ImageConstants.GUN_CARRIAGE_IMAGE, ImageConstants.GUN_IMAGE));
      renderers.add(new TurretGridElementImagePainter(southTurretGridElement, ImageConstants.GUN_CARRIAGE_IMAGE, ImageConstants.GUN_IMAGE));
      renderers.add(new TankGridElementImagePainter(tankGridElement, ImageConstants.TANK_HULL_IMAGE, ImageConstants.GUN_CARRIAGE_IMAGE,
            ImageConstants.GUN_IMAGE));
      renderers.add(new TankGridElementImagePainter(battleShipGridElement, ImageConstants.TANK_HULL_IMAGE, ImageConstants.GUN_CARRIAGE_IMAGE,
            ImageConstants.GUN_IMAGE));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers);
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Renderer<? extends GridElement>> renderers) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      MoveableAdder moveableAdder = MoveableAdderBuilder.builder()
            .withMoveableVelocity(20)
            .withCounter(80)
            .withPadding(padding)
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .build();
      int cycleTime = 15;
      new UIRefresher(mainWindow, cycleTime).start();
      new LogicHandler(mainWindow, grid, renderers, moveableAdder, cycleTime, padding, false).start();
   }
}