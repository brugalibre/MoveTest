package com.myownb3.piranha.launch.weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationBuilder;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationTankBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTankTurretBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTurretBuilder;
import com.myownb3.piranha.application.battle.util.MoveableAdder;
import com.myownb3.piranha.application.battle.util.MoveableAdder.MoveableAdderBuilder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.cluster.TurretCluster;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
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
      Position northTurretPos = Positions.of(70, 70).rotate(-60);
      Position southTurretPos = Positions.of(700, 450).rotate(120);

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
      int tankWidth = 70;
      int tankHeight = 90;
      int gunCarriageRadius = 30;
      double gunHeight = 50;
      double gunWidth = 14;
      int battleTankGunCarriageRadius = 25;
      int battleTankGunHeight = 35;
      int battleTankGunWidth = 14;
      int projectileVelocity = 50;

      int battleShipParkingAngle = -90;
      double battleTankHeight = tankHeight * 2d;
      double battleTankWidth = 3 * tankWidth / 3d;
      TankHolder battleShipHolder = new TankHolder();
      Position battleShipPos = Positions.of(150, 600, tankHeightFromGround).rotate(90);
      Position tankTurret1Pos = Positions.of(140, 100).rotate(180);
      Position tankTurret2Pos = Positions.of(30, 100).rotate(180);

      List<EndPosition> battleShipEndPositions = new ArrayList<>();
      battleShipEndPositions.add(EndPositions.of(Positions.of(750, 600), 10));
      battleShipEndPositions.add(EndPositions.of(battleShipPos, 10));

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
            .withDetectorAngle(360)
            .build();

      DetectorConfig missileDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(200)
            .withDetectorAngle(120)
            .build();

      TankHolder tankHolder = new TankHolder();

      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .addTankGridElement(tankHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(endPositions)
                  .withGrid(grid)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankHeight(tankHeight)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankPos(tankPos)
                  .withTankWidth(tankWidth)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                  .withEvasionStateMachine(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(1)
                        .withReturningMinDistance(1)
                        .withReturningAngleMargin(1)
                        .withPassingDistance(25)
                        .withPostEvasionReturnAngle(4)
                        .withDetectorConfig(DetectorConfigBuilder.builder()
                              .build())
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> tankHolder.getPosition().getDirection().getAngle())
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.MISSILE)
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
                                          .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                                          .build())
                                    .withProjectileDamage(300)
                                    .build())
                              .build())
                        .withGunCarriageShape(RectangleBuilder.builder()
                              .withHeight(gunCarriageRadius)
                              .withWidth(gunCarriageRadius)
                              .withCenter(tankTurretPos)
                              .build())
                        .withGunHeight(gunHeight)
                        .withGunWidth(gunWidth)
                        .withTurretPosition(tankTurretPos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(tankHolder))
            .addTankGridElement(battleShipHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(battleShipEndPositions)
                  .withGrid(grid)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankPos(battleShipPos)
                  .withTankHeight(battleTankHeight)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankWidth(battleTankWidth)
                  .withTankStrategy(TankStrategy.ALWAYS_MOVE_AND_SHOOT)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE_VAR2)
                  .withEvasionStateMachine(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(180)
                        .withReturningMinDistance(1)
                        .withReturningAngleMargin(1)
                        .withPassingDistance(25)
                        .withPostEvasionReturnAngle(4)
                        .withDetectorConfig(DetectorConfigBuilder.builder()
                              .build())
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionBackward4Distance(50))
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
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
                        .withGunCarriageShape(RectangleBuilder.builder()
                              .withHeight(battleTankGunCarriageRadius)
                              .withWidth(battleTankGunCarriageRadius)
                              .withCenter(tankTurret1Pos)
                              .build())
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret1Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionForward4Distance(50))
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
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
                        .withGunCarriageShape(RectangleBuilder.builder()
                              .withHeight(battleTankGunCarriageRadius)
                              .withWidth(battleTankGunCarriageRadius)
                              .withCenter(tankTurret2Pos)
                              .build())
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret2Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(battleShipHolder))
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withGrid(grid)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withDetectorConfig(detectorConfig)
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(3)
                        .withRoundsPerMinute(200)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.builder()
                                    .withDimensionRadius(3)
                                    .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                    .build())
                              .withVelocity(projectileVelocity)
                              .build())
                        .build())
                  .withGunCarriageShape(RectangleBuilder.builder()
                        .withHeight(gunCarriageRadius)
                        .withWidth(gunCarriageRadius)
                        .withCenter(northTurretPos)
                        .build())
                  .withGunHeight(gunHeight)
                  .withGunWidth(gunWidth)
                  .withTurretPosition(northTurretPos)
                  .withTurretRotationSpeed(turretRotationSpeed)
                  .build())
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withGrid(grid)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withDetectorConfig(detectorConfig)
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(1)
                        .withRoundsPerMinute(250)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.builder()
                                    .withDimensionRadius(3)
                                    .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                    .build())
                              .withVelocity(projectileVelocity)
                              .build())
                        .build())
                  .withGunCarriageShape(RectangleBuilder.builder()
                        .withHeight(gunCarriageRadius)
                        .withWidth(gunCarriageRadius)
                        .withCenter(southTurretPos)
                        .build())
                  .withGunHeight(gunHeight)
                  .withGunWidth(gunWidth)
                  .withTurretPosition(southTurretPos)
                  .withTurretRotationSpeed(turretRotationSpeed)
                  .build())
            .build();

      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      mainWindow.withBackground(ImageConstants.DEFAULT_BACKGROUND);

      List<Renderer<? extends GridElement>> renderers = createRenderer4GridElements(tankBattleApplication);;

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers);
   }

   private List<Renderer<? extends GridElement>> createRenderer4GridElements(TankBattleApplication tankBattleApplication) {
      List<Renderer<? extends GridElement>> renderers = new ArrayList<>();
      renderers.addAll(tankBattleApplication.getTurretGridElements()
            .stream()
            .map(buildTurretGridElementImagePainter())
            .collect(Collectors.toList()));
      renderers.addAll(tankBattleApplication.getTankGridElements()
            .stream()
            .map(buildTankGridElementImagePainter())
            .collect(Collectors.toList()));
      return renderers;
   }

   private Function<? super TurretGridElement, ? extends TurretGridElementImagePainter> buildTurretGridElementImagePainter() {
      return turretGridElement -> new TurretGridElementImagePainter(turretGridElement, ImageConstants.GUN_CARRIAGE_IMAGE,
            ImageConstants.GUN_IMAGE);
   }

   private Function<? super TankGridElement, ? extends TankGridElementImagePainter> buildTankGridElementImagePainter() {
      return tankGridElement -> {
         if (tankGridElement.getTurret() instanceof TurretCluster) {
            return new TankGridElementImagePainter(tankGridElement, ImageConstants.TANK_HULL_SYMECTRIC_IMAGE,
                  ImageConstants.GUN_CARRIAGE_IMAGE, ImageConstants.GUN_IMAGE);
         } else {
            return new TankGridElementImagePainter(tankGridElement, ImageConstants.TANK_HULL_IMAGE, ImageConstants.GUN_CARRIAGE_IMAGE,
                  ImageConstants.GUN_IMAGE);
         }
      };
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
