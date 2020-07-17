package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
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
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.TankHolder;
import com.myownb3.piranha.core.battle.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.engine.human.HumanTankEngine.HumanTankEngineBuilder;
import com.myownb3.piranha.core.battle.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.battle.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluatorImpl.TargetGridElementEvaluatorBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.strategy.handler.impl.human.HumanControlledTurretStrategyHandler;
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
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.launch.weapon.listener.KeyListener;
import com.myownb3.piranha.launch.weapon.listener.MouseListener;
import com.myownb3.piranha.launch.weapon.listener.MoveableAdder;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.UIRefresher;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public class HumanTankTestLauncher {
   private static final int MAX_X = 600;
   private static final int MAX_Y = 700;
   private static final int padding = 0;

   public static void main(String[] args) throws InterruptedException {
      HumanTankTestLauncher launcher = new HumanTankTestLauncher();
      launcher.launch();
   }

   private void launch() throws InterruptedException {

      // Common
      int width = 30;
      int height = 5;
      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;
      int tankWidth = 40;
      int tankHeight = 90;
      int projectileVelocity = 30;

      // Rebel
      Position rebelTankPos = Positions.of(450, 600).rotate(80);
      int rebelTankVelocity = 25;
      double rebelHealth = 25000;

      // imperial
      double imperialHealth = 200;
      int imperialTankVelocity = 20;
      Position imperialTankPos = Positions.of(200, 100);
      List<EndPosition> imperialTankEndPositions = new ArrayList<>();
      imperialTankEndPositions.add(EndPositions.of(Positions.of(200, 600), 10));
      imperialTankEndPositions.add(EndPositions.of(Positions.of(imperialTankPos), 10));
      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(350)
            .withDetectorAngle(180)
            .build();

      DetectorConfig missileDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
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
      int missileCounterMeasureDetectionDistance = 80;
      TankGridElement imperialTank = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(imperialTankVelocity)
            .withTankheightFromBottom(tankHeightFromGround)
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
                  .withHealth(imperialHealth)
                  .withTankEngine(TankEngineBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withRestartRunningAudio(false)
                              .withAudioResource(AudioConstants.TANK_TRACK_RATTLE_VAR2)
                              .build())
                        .withMoveableController(MoveableControllerBuilder.builder()
                              .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                              .withEndPositions(imperialTankEndPositions)
                              .withLazyMoveable(() -> imperialTankHolder.getTankGridElement())
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> imperialTankHolder.getTankGridElement())
                        .withGrid(grid)
                        .withDetector(TrippleDetectorClusterBuilder.builder()
                              .withCenterDetector(DetectorBuilder.builder()
                                    .withAngleInc(1)
                                    .withDetectorAngle(90)
                                    .withDetectorReach(400)
                                    .withEvasionAngle(90)
                                    .withEvasionDistance(missileCounterMeasureDetectionDistance)
                                    .build())
                              .withLeftSideDetector(DetectorBuilder.builder()
                                    .withAngleInc(1)
                                    .withDetectorAngle(90)
                                    .withDetectorReach(400)
                                    .withEvasionAngle(90)
                                    .withEvasionDistance(missileCounterMeasureDetectionDistance)
                                    .build(), 90)
                              .withRightSideDetector(DetectorBuilder.builder()
                                    .withAngleInc(1)
                                    .withDetectorAngle(90)
                                    .withDetectorReach(400)
                                    .withEvasionAngle(90)
                                    .withEvasionDistance(missileCounterMeasureDetectionDistance)
                                    .build(), 90)
                              .withStrategy(DetectingStrategy.SUPPORTIVE_FLANKS_WITH_DETECTION)
                              .withAutoDetectionStrategyHandler()
                              .build())
                        .build())
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withTurret(TankTurretBuilder.builder()
                        .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                        .withParkingAngleEvaluator(() -> imperialTankHolder.getPosition().getDirection().getAngle())
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
                                    .withGunProjectileType(ProjectileTypes.MISSILE)
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
                                                .withVelocity(projectileVelocity)
                                                .withTargetGridElementEvaluator(TargetGridElementEvaluatorBuilder.builder()
                                                      .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                                                      .withDetector(DetectorBuilder.builder()
                                                            .withDetectorAngle(missileDetectorConfig.getDetectorAngle())
                                                            .withDetectorReach(missileDetectorConfig.getDetectorReach())
                                                            .build())
                                                      .withGridElementEvaluator(
                                                            (position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                                                      .build())
                                                .withProjectileDamage(100)
                                                .build())
                                          .build())
                                    .withGunShape(GunShapeBuilder.builder()
                                          .withBarrel(RectangleBuilder.builder()
                                                .withHeight(gunHeight)
                                                .withWidth(gunWidth)
                                                .withCenter(imperialTankPos)
                                                .withOrientation(Orientation.HORIZONTAL)
                                                .build())
                                          .withMuzzleBreak(RectangleBuilder.builder()
                                                .withHeight(gunWidth * 1.5)
                                                .withWidth(gunWidth * 1.5)
                                                .withCenter(imperialTankPos)
                                                .withOrientation(Orientation.HORIZONTAL)
                                                .build())
                                          .build())
                                    .build())
                              .withShape(CircleBuilder.builder()
                                    .withRadius(gunCarriageRadius)
                                    .withAmountOfPoints(gunCarriageRadius)
                                    .withCenter(imperialTankPos)
                                    .build())
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(imperialTankPos)
                        .withHeight(tankHeight)
                        .withWidth(tankWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();

      imperialTankHolder.setAndReturnTank(imperialTank);
      imperialTankHolder.setTankGridElement(imperialTank);

      TankHolder rebelTankHolder = new TankHolder();
      HumanTankEngine humanTankEngine = HumanTankEngineBuilder.builder()
            .withAudioClip(AudioClipBuilder.builder()
                  .withRestartRunningAudio(false)
                  .withAudioResource(AudioConstants.TANK_TRACK_RATTLE)
                  .build())
            .withLazyMoveable(() -> rebelTankHolder.getTankGridElement())
            .build();

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
                        .withMuzzleBreak(RectangleBuilder.builder()
                              .withHeight(gunWidth * 1.5)
                              .withWidth(gunWidth * 1.5)
                              .withCenter(rebelTankPos)
                              .withOrientation(Orientation.HORIZONTAL)
                              .build())
                        .build())
                  .build())
            .withShape(CircleBuilder.builder()
                  .withRadius(gunCarriageRadius)
                  .withAmountOfPoints(gunCarriageRadius)
                  .withCenter(rebelTankPos)
                  .build())
            .build();
      HumanControlledTurretStrategyHandler turretStrategyHandler = new HumanControlledTurretStrategyHandler(gunCarriage);

      TankGridElement humanRebelTank = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(rebelTankVelocity)
            .withTankheightFromBottom(tankHeightFromGround)
            .withTurretHeightFromBottom(tankTurretHeight)
            .withTank(TankBuilder.builder()
                  .withHealth(rebelHealth)
                  .withTankEngine(humanTankEngine)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTurret(TurretBuilder.builder()
                        .withGunCarriage(gunCarriage)
                        .withTurretStrategyHandler(turretStrategyHandler)
                        .withPositionTransformator(pos -> pos.movePositionForward(150))
                        .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(rebelTankPos)
                        .withHeight(tankHeight)
                        .withWidth(tankWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withTankStrategy(TankStrategy.HUMAN_CONTROLLED)
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
                  .build())

            .build();

      rebelTankHolder.setAndReturnTank(humanRebelTank);
      rebelTankHolder.setTankGridElement(humanRebelTank);

      grid.prepare();
      WorkerThreadFactory.INSTANCE.restart();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      mainWindow.withBackground("res/image/background_1.jpg");
      mainWindow.addMouseListener(new MouseListener(padding, turretStrategyHandler));
      mainWindow.addKeyListener(new KeyListener(humanTankEngine));

      List<Renderer<? extends GridElement>> renderers = new ArrayList<>();
      renderers.add(new GridElementPainter(humanRebelTank, getColor(humanRebelTank), height, width));
      renderers.add(new GridElementPainter(imperialTank, getColor(imperialTank), height, width));


      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers);
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Renderer<? extends GridElement>> renderers) {
      int cycleTime = 20;
      SwingUtilities.invokeLater(() -> mainWindow.show());
      MoveableAdder moveableAdder = new MoveableAdder(MAX_X, MAX_Y, 8, 120);
      new UIRefresher(mainWindow, cycleTime).start();
      new LogicHandler(mainWindow, grid, renderers, moveableAdder, cycleTime, padding, true).start();
   }
}
