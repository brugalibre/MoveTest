package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.launch.weapon.BattleRendererBuilder.createRenderer4TankBattleApplication;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationBuilder;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationTankBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationHumanTurretBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTankTurretBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTurretBuilder;
import com.myownb3.piranha.application.battle.util.MoveableAdder.MoveableAdderBuilder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.weapon.countermeasure.DecoyFlareDispenser.DecoyFlareDispenserBuilder;
import com.myownb3.piranha.core.battle.weapon.countermeasure.MissileCounterMeasureSystemImpl.MissileCounterMeasureSystemBuilder;
import com.myownb3.piranha.core.battle.weapon.countermeasure.config.DecoyFlareConfigImpl.DecoyFlareConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.DefaultGunImpl.DefaultGunBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.DefaultGunCarriageImpl.DefaultGunCarriageBuilder;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine.HumanTankEngineBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.human.HumanControlledTurretStrategyHandler;
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
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.WallBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.launch.weapon.listener.KeyListener;
import com.myownb3.piranha.launch.weapon.listener.MouseListener;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.evasionstatemachine.config.DefaultConfig;
import com.myownb3.piranha.ui.application.impl.UILogicUtil;
import com.myownb3.piranha.ui.constants.ImageConstants;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public class HumanTankTestLauncher {
   private static final int MAX_X = 800;
   private static final int MAX_Y = 900;
   private static final int padding = 0;

   public static void main(String[] args) {
      HumanTankTestLauncher launcher = new HumanTankTestLauncher();
      launcher.launch();
   }

   private void launch() {

      // Common
      int width = 30;
      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      int gunCarriageRadius = 10;
      int tankGunCarriageRadius = 13;
      double gunHeight = 25;
      double gunWidth = 7;
      int tankWidth = 40;
      int tankHeight = 90;
      int projectileVelocity = 150;
      int missileVelocity = 60;
      double turretRotationSpeed = 3;

      // Rebel
      Position rebelTankPos = Positions.of(450, 600).rotate(80);
      int rebelTankEngineAccelerationSpeed = 1300;
      double rebelTankEngineManuallySlowDownSpeed = 200;
      double rebelTankEngineNaturallySlowDownSpeed = 900;
      int rebelTankVelocity = 25;
      double rebelHealth = 350;

      // imperial
      double imperialHealth = 200;
      double imperialTurretHealth = 100;
      int imperialTankVelocity = 20;
      Position turretNorthPos = Positions.of(700, 70);
      Position imperialTankPos = Positions.of(200, 100);
      List<EndPosition> imperialTankEndPositions = new ArrayList<>();
      imperialTankEndPositions.add(EndPositions.of(Positions.of(200, 600), 10));
      imperialTankEndPositions.add(EndPositions.of(Positions.of(imperialTankPos), 10));

      int missileCounterMeasureDetectionDistance = 80;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(450)
            .withDetectorAngle(180)
            .build();

      DetectorConfig missileDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(300)
            .withDetectorAngle(180)
            .build();

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withCollisionDetectionHandler(BouncingCollisionDetectionHandlerBuilder.builder()
                  .withBouncedPositionEvaluator(new BouncedPositionEvaluatorImpl())
                  .build())
            .withMaxX(MAX_X)
            .withMaxY(MAX_Y)
            .withMinX(padding)
            .withMinY(padding)
            .build();

      TankHolder imperialTankHolder = new TankHolder();
      TankHolder rebelTankHolder = new TankHolder();

      HumanTankEngine humanTankEngine =
            buildHumanTankEngine(rebelTankEngineAccelerationSpeed, rebelTankEngineManuallySlowDownSpeed, rebelTankEngineNaturallySlowDownSpeed,
                  rebelTankVelocity, rebelTankHolder);
      GunCarriage gunCarriage = DefaultGunCarriageBuilder.builder()
            .withRotationSpeed(5)
            .withGun(DefaultGunBuilder.builder()
                  .withGunProjectileType(ProjectileTypes.BULLET)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(1)
                        .withRoundsPerMinute(900)
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
                              .withHeight(gunHeight)
                              .withWidth(gunWidth)
                              .withCenter(rebelTankPos)
                              .withOrientation(Orientation.HORIZONTAL)
                              .build())
                        .withMuzzleBrake(RectangleBuilder.builder()
                              .withHeight(gunWidth * 1.5)
                              .withWidth(gunWidth * 1.5)
                              .withCenter(rebelTankPos)
                              .withOrientation(Orientation.HORIZONTAL)
                              .build())
                        .build())
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(tankGunCarriageRadius)
                  .withAmountOfPoints(tankGunCarriageRadius)
                  .withCenter(rebelTankPos)
                  .build())
            .build();
      HumanControlledTurretStrategyHandler turretStrategyHandler = new HumanControlledTurretStrategyHandler(gunCarriage);

      LazyGridElement lazyTurretGridElement = new LazyGridElement();
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(MoveableAdderBuilder.builder()
                  .withMoveableVelocity(8)
                  .withCounter(200)
                  .withPadding(padding)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .withEvasionStateMachineConfig(DefaultConfig.INSTANCE.getDefaultEvasionStateMachineConfig())
            .addTankGridElement(imperialTankHolder, TankBattleApplicationTankBuilder.builder()
                  .withEndPositions(imperialTankEndPositions)
                  .withGrid(grid)
                  .withHealth(imperialHealth)
                  .withTankHeight(tankHeight)
                  .withEngineVelocity(imperialTankVelocity)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankPos(imperialTankPos)
                  .withDefaultOnDestructionHandler(() -> grid.remove(imperialTankHolder.getTankGridElement()))
                  .withTankWidth(tankWidth)
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
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
                        .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                        .withParkingAngleEvaluator(() -> imperialTankHolder.getPosition().getDirection().getAngle())
                        .withDetectorConfig(detectorConfig)
                        .withProjectileType(ProjectileTypes.MISSILE)
                        .withGunConfig(GunConfigBuilder.builder()
                              .withAudioClip(AudioClipBuilder.builder()
                                    .withAudioResource(AudioConstants.MISSILE_SHOT_SOUND)
                                    .build())
                              .withSalveSize(1)
                              .withRoundsPerMinute(70)
                              .withProjectileConfig(ProjectileConfigBuilder.builder()
                                    .withDimensionInfo(DimensionInfoBuilder.builder()
                                          .withDimensionRadius(3)
                                          .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                          .build())
                                    .withVelocity(missileVelocity)
                                    .withTargetGridElementEvaluator(TargetGridElementEvaluatorBuilder.builder()
                                          .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                                          .withDetector(DetectorBuilder.builder()
                                                .withDetectorAngle(missileDetectorConfig.getDetectorAngle())
                                                .withDetectorReach(missileDetectorConfig.getDetectorReach())
                                                .build())
                                          .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                                          .build())
                                    .withProjectileDamage(100)
                                    .build())
                              .build())
                        .withGunCarriageShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(imperialTankPos)
                              .build())
                        .withMuzzleBrake()
                        .withGunHeight(gunHeight)
                        .withGunWidth(gunWidth)
                        .withTurretPosition(imperialTankPos)
                        .withTurretRotationSpeed(turretRotationSpeed)
                        .build())
                  .build(imperialTankHolder))
            .addTankGridElement(rebelTankHolder, TankBattleApplicationTankBuilder.builder()
                  .withGrid(grid)
                  .withHealth(rebelHealth)
                  .withTankHeight(tankHeight)
                  .withEngineVelocity(rebelTankVelocity)
                  .withTankTurretHeight(tankTurretHeight)
                  .withTankPos(rebelTankPos)
                  .withTankWidth(tankWidth)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.HUMAN_CONTROLLED)
                  .withTankEngine(humanTankEngine)
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> rebelTankHolder.getTankGridElement())
                        .withGrid(grid)
                        .withMissileCounterMeasureSystem(MissileCounterMeasureSystemBuilder.builder()
                              .withDecoyFlareDispenser(DecoyFlareDispenserBuilder.builder()
                                    .withMinTimeBetweenDispensing(2000)
                                    .withDecoyFlareConfig(DecoyFlareConfigBuilder.builder()
                                          .withAmountDecoyFlares(15)
                                          .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                                          .withDecoyFlareSpreadAngle(90)
                                          .withDecoyFlareTimeToLife(30)
                                          .withVelocity(8)
                                          .withDimensionInfo(DimensionInfoBuilder.builder()
                                                .withDimensionRadius(2)
                                                .withHeightFromBottom(tankHeightFromGround + tankTurretHeight)
                                                .build())
                                          .withProjectileDamage(0)
                                          .build())
                                    .build())
                              .withGrid(grid)
                              .withDetector(DetectorBuilder.builder()
                                    .withAngleInc(1)
                                    .withDetectorAngle(360)
                                    .withDetectorReach(400)
                                    .withEvasionAngle(360)
                                    .withEvasionDistance(missileCounterMeasureDetectionDistance)
                                    .build())
                              .withGridElementSupplier(() -> rebelTankHolder.getTankGridElement())
                              .build())
                        .withDetector(DetectorBuilder.builder()
                              .withAngleInc(1)
                              .withDetectorAngle(360)
                              .withDetectorReach(400)
                              .withEvasionAngle(360)
                              .withEvasionDistance(missileCounterMeasureDetectionDistance)
                              .build())
                        .build())
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
                  .addTurret(TankBattleApplicationHumanTurretBuilder.builder()
                        .withGrid(grid)
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .withProjectileType(ProjectileTypes.BULLET)
                        .withTurretStrategyHandler(turretStrategyHandler)
                        .withPositionTransformator(pos -> pos.movePositionForward(150))
                        .withGunCarriage(gunCarriage)
                        .build())
                  .build(imperialTankHolder))
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withGrid(grid)
                  .withDetectorConfig(detectorConfig)
                  .withProjectileType(ProjectileTypes.LASER_BEAM)
                  .withDestructionHelper(DestructionHelperBuilder.builder()
                        .withDamage(0)
                        .withHealth(imperialTurretHealth)
                        .withSelfDestructiveDamage(0)
                        .withDestroyedAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.EXPLOSION_SOUND)
                              .build())
                        .withOnDestroyedCallbackHandler(() -> {
                           grid.remove(lazyTurretGridElement.getGridElement());
                        })
                        .build())
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.LASER_BEAM_BLAST_SOUND)
                              .build())
                        .withSalveSize(1)
                        .withRoundsPerMinute(200)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withProjectileDamage(30)
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
                        .withCenter(turretNorthPos)
                        .build())
                  .withMuzzleBrake()
                  .withGunHeight(gunHeight)
                  .withGunWidth(gunWidth)
                  .withTurretPosition(turretNorthPos)
                  .withTurretRotationSpeed(turretRotationSpeed)
                  .build(), lazyTurretGridElement)
            .build();

      List<WallGridElement> wallSemgments = addProtectiveWall(grid);
      grid.prepare();
      WorkerThreadFactory.INSTANCE.restart();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      mainWindow.withBackground(ImageConstants.DEFAULT_BACKGROUND);
      mainWindow.withImageIcon(ImageConstants.TANK_IMAGE);
      mainWindow.addMouseListener(new MouseListener(padding, turretStrategyHandler));
      mainWindow.addKeyListener(new KeyListener(humanTankEngine));

      List<Renderer<? extends GridElement>> renderers = createRenderer4TankBattleApplication(tankBattleApplication);

      for (WallGridElement wallSegment : wallSemgments) {
         renderers.add(new GridElementPainter(wallSegment, getColor(wallSegment)));
      }

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers, tankBattleApplication);
   }

   private HumanTankEngine buildHumanTankEngine(int rebelTankEngineAccelerationSpeed, double rebelTankEngineManuallySlowDownSpeed,
         double rebelTankEngineNaturallySlowDownSpeed, int rebelTankVelocity, TankHolder rebelTankHolder) {
      return HumanTankEngineBuilder.builder()
            .withEngineAudio(EngineAudioBuilder.builder()
                  .withDefaultAudio()
                  .build())
            .withLazyMoveable(() -> rebelTankHolder.getTankGridElement())
            .withVelocity(rebelTankVelocity)
            .withEngineStateHandler(new EngineStateHandler(EngineAcceleratorBuilder.builder()
                  .withEngineTransmissionConfig(EngineTransmissionConfigBuilder.builder()
                        .addGear(GearBuilder.builder()
                              .withAccelerationSpeed(rebelTankEngineAccelerationSpeed / 3)
                              .withMaxVelocity(rebelTankVelocity / 3)
                              .withNumber(1)
                              .buil())
                        .addGear(GearBuilder.builder()
                              .withAccelerationSpeed(2 * rebelTankEngineAccelerationSpeed / 3)
                              .withMaxVelocity(2 * rebelTankVelocity / 3)
                              .withNumber(1)
                              .buil())
                        .addGear(GearBuilder.builder()
                              .withAccelerationSpeed(rebelTankEngineAccelerationSpeed)
                              .withMaxVelocity(rebelTankVelocity)
                              .withNumber(3)
                              .buil())
                        .build())
                  .withManuallySlowDownSpeed(rebelTankEngineManuallySlowDownSpeed)
                  .withNaturallySlowDownSpeed(rebelTankEngineNaturallySlowDownSpeed)
                  .build()))
            .build();
   }

   private List<WallGridElement> addProtectiveWall(MirrorGrid grid) {
      Position wallSegmentPos = Positions.of(390, 700);
      int wallSegmentLength = 40;
      int wallSegmentWidth = 10;
      double wallHealth = 800.0;
      return WallBuilder.builder()
            .withGrid(grid)
            .withWallHealth(wallHealth)
            .withWallStartPos(wallSegmentPos)
            .withWallSegmentWidth(wallSegmentWidth)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .rotate(80.0)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addGap(2 * wallSegmentLength / 3d)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .addWallSegment(wallSegmentLength)
            .build();
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Renderer<? extends GridElement>> renderers,
         TankBattleApplication tankBattleApplication) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      int logicCycleTime = 15;
      int uiRefreshCycleTime = 5;
      UILogicUtil.startUIRefresher(mainWindow, uiRefreshCycleTime);
      UILogicUtil.startLogicHandler(grid, mainWindow, renderers, tankBattleApplication, logicCycleTime);
   }
}
