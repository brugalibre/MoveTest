package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.core.battle.destruction.DestructionHelper.getIsDestroyedBooleanSupplier;
import static com.myownb3.piranha.launch.weapon.BattleRendererBuilder.createRenderer4TankBattleApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationBuilder;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationTankBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTankTurretBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTurretBuilder;
import com.myownb3.piranha.application.battle.impl.MoveableAdderImpl;
import com.myownb3.piranha.application.battle.impl.MoveableAdderImpl.MoveableAdderBuilder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.lazy.GenericLazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.launch.weapon.listener.DelegateOnGunFireListener;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.evasionstatemachine.config.DefaultConfig;
import com.myownb3.piranha.ui.application.impl.UILogicUtil;
import com.myownb3.piranha.ui.image.constants.ImageConsts;
import com.myownb3.piranha.ui.render.Renderer;

public class TankTestLauncher {
   private static final int MAX_Y = 820;
   private static final int MAX_X = 850;
   private static final int padding = 0;

   public static void main(String[] args) {
      TankTestLauncher launcher = new TankTestLauncher();
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
      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;
      int battleTankGunCarriageRadius = 8;
      double battleTankGunHeight = 20;
      double battleTankGunWidth = 5;
      int projectileVelocity = 150;
      int missileVelocity = 80;
      int moveableVelocity = 35;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
            .withDetectorAngle(360)
            .build();

      DetectorConfig missileDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(200)
            .withDetectorAngle(120)
            .build();

      int battleShipParkingAngle = -90;
      TankHolder tankHolder = new TankHolder();
      TankHolder battleShipHolder = new TankHolder();
      GenericLazyGridElement<TurretGridElement> southTurretLazy = new GenericLazyGridElement<>();
      GenericLazyGridElement<TurretGridElement> northTurretLazy = new GenericLazyGridElement<>();
      Position battleShipPos = Positions.of(150, 600, tankHeightFromGround).rotate(90);
      Position tankTurret1Pos = Positions.of(140, 100).rotate(180);
      Position tankTurret2Pos = Positions.of(30, 100).rotate(180);
      Position tankTurret3Pos = Positions.of(30, 100).rotate(180);

      List<EndPosition> battleShipEndPositions = new ArrayList<>();
      battleShipEndPositions.add(EndPositions.of(Positions.of(750, 600), 10));
      battleShipEndPositions.add(EndPositions.of(battleShipPos, 10));

      MoveableAdderImpl moveableAdder = MoveableAdderBuilder.builder()
            .withMoveableVelocity(moveableVelocity)
            .withCounter(100)
            .withPadding(padding)
            .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
            .build();

      DelegateOnGunFireListener delegateOnGunFireListener = new DelegateOnGunFireListener();
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(moveableAdder)
            .withEvasionStateMachineConfig(DefaultConfig.INSTANCE.getDefaultEvasionStateMachineConfig())
            .addTankGridElement(tankHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(endPositions)
                  .withGrid(grid)
                  .withTankHeight(tankHeight)
                  .withEngineVelocity(12)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankPos(tankPos)
                  .withTankWidth(tankWidth)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE)
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
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> tankHolder.getPosition().getDirection().getAngle())
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.MISSILE)
                        .withOnGunFireListeners(Collections.singletonList(delegateOnGunFireListener))
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.MISSILE_SHOT_SOUND)
                                    .withIsCloseableSupplier(getIsDestroyedBooleanSupplier(tankHolder.getTankGridElement()))
                                    .build())
                              .withSalveSize(1)
                              .withRoundsPerMinute(100)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.builder()
                                          .withDimensionRadius(3)
                                          .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                          .build())
                                    .withVelocity(missileVelocity)
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
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(tankTurretPos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(gunHeight)
                        .withGunWidth(gunWidth)
                        .withTurretPosition(tankTurretPos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(tankHolder))
            .addTankGridElement(battleShipHolder, TankBattleApplicationTankBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withEndPositions(battleShipEndPositions)
                  .withGrid(grid)
                  .withTankPos(battleShipPos)
                  .withEngineVelocity(10)
                  .withTankHeight(tankHeight * 2d)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankWidth(2 * tankWidth / 3d)
                  .withTankStrategy(TankStrategy.ALWAYS_MOVE_AND_SHOOT)
                  .withTankEngineAudioResource(AudioConstants.TANK_TRACK_RATTLE_VAR2)
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
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionBackward4Distance(70))
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                    .withIsCloseableSupplier(() -> battleShipHolder.getTankGridElement().isDestroyed())
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
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(battleTankGunCarriageRadius)
                              .withAmountOfPoints(battleTankGunCarriageRadius)
                              .withCenter(tankTurret1Pos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret1Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos)
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                    .withIsCloseableSupplier(() -> battleShipHolder.getTankGridElement().isDestroyed())
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
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(battleTankGunCarriageRadius)
                              .withAmountOfPoints(battleTankGunCarriageRadius)
                              .withCenter(tankTurret2Pos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret2Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .addTurret(TankBattleApplicationTankTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                        .withPositionTransformator(transformedTankPos -> transformedTankPos.movePositionForward4Distance(70))
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.LASER_BEAM)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                                    .withIsCloseableSupplier(() -> battleShipHolder.getTankGridElement().isDestroyed())
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
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(battleTankGunCarriageRadius)
                              .withAmountOfPoints(battleTankGunCarriageRadius)
                              .withCenter(tankTurret3Pos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(battleTankGunHeight)
                        .withGunWidth(battleTankGunWidth)
                        .withTurretPosition(tankTurret3Pos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(battleShipHolder))
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withGrid(grid)
                  .withDetectorConfig(detectorConfig)
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .withIsCloseableSupplier(getIsDestroyedBooleanSupplier(northTurretLazy.getGridElement()))
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
                  .withGunCarriageShape(CircleBuilder.builder()
                        .withRadius(gunCarriageRadius)
                        .withAmountOfPoints(gunCarriageRadius)
                        .withCenter(northTurretPos)
                        .build())
                  .withMuzzleBrake()
                  .withGunHeight(gunHeight)
                  .withGunWidth(gunWidth)
                  .withTurretPosition(northTurretPos)
                  .withTurretRotationSpeed(turretRotationSpeed)
                  .build())
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withGrid(grid)
                  .withDetectorConfig(detectorConfig)
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .withIsCloseableSupplier(getIsDestroyedBooleanSupplier(southTurretLazy.getGridElement()))
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
                  .withGunCarriageShape(CircleBuilder.builder()
                        .withRadius(gunCarriageRadius)
                        .withAmountOfPoints(gunCarriageRadius)
                        .withCenter(southTurretPos)
                        .build())
                  .withMuzzleBrake()
                  .withGunHeight(gunHeight)
                  .withGunWidth(gunWidth)
                  .withTurretPosition(southTurretPos)
                  .withTurretRotationSpeed(turretRotationSpeed)
                  .build())
            .build();

      tankBattleApplication.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      mainWindow.withBackground(ImageConsts.DEFAULT_BACKGROUND);
      mainWindow.withImageIcon(ImageConsts.TANK_IMAGE);

      List<Renderer<?>> renderers = createRenderer4TankBattleApplication(tankBattleApplication);
      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers, tankBattleApplication, delegateOnGunFireListener);
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Renderer<?>> renderers,
         TankBattleApplication tankBattleApplication, DelegateOnGunFireListener delegateOnGunFireListener) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      int logicCycleTime = 45;
      int uiRefreshCycleTime = 15;
      UILogicUtil.startUIRefresher(mainWindow, uiRefreshCycleTime);
      UILogicUtil.startLogicHandler(grid, renderers, tankBattleApplication, delegateOnGunFireListener, logicCycleTime);
   }
}
