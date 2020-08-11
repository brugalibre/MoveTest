package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.util.MoveableAdder;
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
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement.TurretGridElementBuilder;
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
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.RectanglePathBuilderImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.path.impl.RectanglePathBuilderImpl.RectangleSides;
import com.myownb3.piranha.core.grid.gridelement.wall.WallBuilder;
import com.myownb3.piranha.core.grid.gridelement.wall.WallGridElement;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.moveables.engine.EngineStateHandler;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.EngineAcceleratorImpl.EngineAcceleratorBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.EngineTransmissionConfigImpl.EngineTransmissionConfigBuilder;
import com.myownb3.piranha.core.moveables.engine.accelerate.impl.transmission.GearImpl.GearBuilder;
import com.myownb3.piranha.core.moveables.engine.audio.EngineAudio.EngineAudioBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.launch.weapon.listener.KeyListener;
import com.myownb3.piranha.launch.weapon.listener.MouseListener;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.UIRefresher;
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
      int height = 5;
      double tankTurretHeight = GridElementConst.DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM;
      double tankHeightFromGround = GridElementConst.DEFAULT_TANK_HEIGHT_FROM_BOTTOM;
      int gunCarriageRadius = 10;
      int tankGunCarriageRadius = 13;
      double gunHeight = 25;
      double gunWidth = 7;
      int tankWidth = 40;
      int tankHeight = 90;
      int projectileVelocity = 80;
      int missileVelocity = 30;
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

      LazyGridElement lazyTurretGridElement = new LazyGridElement();
      TurretGridElement northImperialTurretGridElement = TurretGridElementBuilder.builder()
            .withGrid(grid)
            .withHeightFromBottom(GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM)
            .withTurret(TurretBuilder.builder()
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
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(detectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(detectorConfig.getDetectorAngle())
                        .withDetectorReach(detectorConfig.getDetectorReach())
                        .withEvasionAngle(detectorConfig.getDetectorAngle())
                        .withEvasionDistance(detectorConfig.getEvasionDistance())
                        .build())
                  .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withGunCarriage(DefaultGunCarriageBuilder.builder()
                        .withRotationSpeed(turretRotationSpeed)
                        .withGun(DefaultGunBuilder.builder()
                              .withGunProjectileType(ProjectileTypes.LASER_BEAM)
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
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(turretNorthPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .withMuzzleBreak(RectangleBuilder.builder()
                                          .withHeight(gunWidth * 2)
                                          .withWidth(gunWidth * 1.5)
                                          .withCenter(turretNorthPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(turretNorthPos)
                              .build())
                        .build())
                  .build())
            .build();
      lazyTurretGridElement.setGridElement(northImperialTurretGridElement);

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
                        .withVelocity(imperialTankVelocity)
                        .withDefaultEngineStateHandler()
                        .withEngineAudio(EngineAudioBuilder.builder()
                              .withDefaultAudio()
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
                  .withOnDestroyedCallbackHandler(() -> {
                     grid.remove(imperialTankHolder.getTankGridElement());
                  })
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
                                                .withVelocity(missileVelocity)
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
                                    .withRadius(tankGunCarriageRadius)
                                    .withAmountOfPoints(tankGunCarriageRadius)
                                    .withCenter(imperialTankPos)
                                    .build())
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withRectanglePathBuilder(new RectanglePathBuilderImpl(20, RectangleSides.FRONT_AND_BACK))
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
                  .withRadius(tankGunCarriageRadius)
                  .withAmountOfPoints(tankGunCarriageRadius)
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
                  .withOnDestroyedCallbackHandler(() -> {
                     grid.remove(rebelTankHolder.getTankGridElement());
                  })
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
                        .withRectanglePathBuilder(new RectanglePathBuilderImpl(20, RectangleSides.FRONT_AND_BACK))
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

      List<WallGridElement> wallSemgments = addProtectiveWall(grid);
      grid.prepare();
      WorkerThreadFactory.INSTANCE.restart();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      mainWindow.withBackground(ImageConstants.DEFAULT_BACKGROUND);
      mainWindow.addMouseListener(new MouseListener(padding, turretStrategyHandler));
      mainWindow.addKeyListener(new KeyListener(humanTankEngine));

      List<Renderer<? extends GridElement>> renderers = new ArrayList<>();
      renderers.add(new GridElementPainter(humanRebelTank, getColor(humanRebelTank), height, width));
      renderers.add(new GridElementPainter(imperialTank, getColor(imperialTank), height, width));
      renderers.add(new GridElementPainter(northImperialTurretGridElement, getColor(northImperialTurretGridElement), height, width));

      for (WallGridElement wallSegment : wallSemgments) {
         renderers.add(new GridElementPainter(wallSegment, getColor(wallSegment), height, width));
      }

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers);
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

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Renderer<? extends GridElement>> renderers) {
      int cycleTime = 20;
      SwingUtilities.invokeLater(() -> mainWindow.show());
      MoveableAdder moveableAdder = MoveableAdderBuilder.builder()
            .withMoveableVelocity(8)
            .withCounter(200)
            .withPadding(padding)
            .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
            .build();
      new UIRefresher(mainWindow, cycleTime).start();
      new LogicHandler(mainWindow, grid, renderers, moveableAdder, cycleTime, padding, false).start();
   }
}
