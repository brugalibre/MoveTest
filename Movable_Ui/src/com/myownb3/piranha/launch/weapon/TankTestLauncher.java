package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.addNewProjectilePainters;
import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.removeDestroyedPainters;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.filter.ProjectileGridElementsFilter;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.constants.GridElementConst;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.weapon.tank.TankHolder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngineImpl.TankEngineBuilder;
import com.myownb3.piranha.core.weapon.tank.strategy.TankStrategy;
import com.myownb3.piranha.core.weapon.tank.turret.TankTurretBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.core.weapon.turret.cluster.TurretClusterImpl.TurretClusterBuilder;
import com.myownb3.piranha.launch.weapon.listener.MoveableAdder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.util.MathUtil;

public class TankTestLauncher {
   private static final int padding = 30;

   public static void main(String[] args) throws InterruptedException {
      TankTestLauncher launcher = new TankTestLauncher();
      launcher.launch();
   }

   private void launch() throws InterruptedException {

      int width = 30;
      int height = 5;
      Position turretNorthPos = Positions.of(70, 70).rotate(-60);

      double turretHeight = GridElementConst.DEFAULT_HEIGHT_FROM_BOTTOM;
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
            .withMaxX(650)
            .withMaxY(650)
            .withMinX(padding)
            .withMinY(padding)
            .build();

      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;
      int projectileVelocity = 40;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
            .withDetectorAngle(360)
            .build();

      Moveable simpleGridElement = buildNewMoveable(grid);
      TankHolder tankHolder = new TankHolder();

      ProjectileGridElementsFilter projectileGridElementsFilter = new ProjectileGridElementsFilter();

      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(12)
            .withTankheightFromBottom(tankTurretHeight)
            .withTurretHeightFromBottom(tankTurretHeight)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
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
                              .withEndPositions(endPositions)
                              .withLazyMoveable(() -> tankHolder.getTankGridElement())
                              .withPostMoveForwardHandler(res -> {
                              })
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
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
                              .withDetectingGridElementFilter(
                                    FilterGridElementsMovingAway.of(() -> tankHolder.getTankGridElement()).and(projectileGridElementsFilter::test))
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
                        .withGunCarriage(SimpleGunCarriageBuilder.builder()
                              .withRotationSpeed(4)
                              .withGun(BulletGunBuilder.builder()
                                    .withGunConfig(GunConfigBuilder.builder()
                                          .withSalveSize(3)
                                          .withRoundsPerMinute(300)
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
                                                .withCenter(tankTurretPos)
                                                .withOrientation(Orientation.HORIZONTAL)
                                                .build())
                                          .withMuzzleBreak(RectangleBuilder.builder()
                                                .withHeight(gunWidth * 1.5)
                                                .withWidth(gunWidth * 1.5)
                                                .withCenter(tankTurretPos)
                                                .withOrientation(Orientation.HORIZONTAL)
                                                .build())
                                          .build())
                                    .build())
                              .withShape(CircleBuilder.builder()
                                    .withRadius(gunCarriageRadius)
                                    .withAmountOfPoints(gunCarriageRadius)
                                    .withCenter(tankTurretPos)
                                    .build())
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(tankPos)
                        .withHeight(tankHeight)
                        .withWidth(tankWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();


      int battleShipParkingAngle = -90;
      TankHolder battleShipHolder = new TankHolder();
      Position battleShipPos = Positions.of(150, 600, tankHeightFromGround).rotate(90);
      Position tankTurret1Pos = Positions.of(140, 100).rotate(180);
      Position tankTurret2Pos = Positions.of(30, 100).rotate(180);
      Position tankTurret3Pos = Positions.of(30, 100).rotate(180);

      List<EndPosition> battleShipEndPositions = new ArrayList<>();
      battleShipEndPositions.add(EndPositions.of(Positions.of(550, 600), 10));
      battleShipEndPositions.add(EndPositions.of(battleShipPos, 10));

      TankGridElement battleShipGridElement = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(6)
            .withTankheightFromBottom(tankTurretHeight)
            .withTurretHeightFromBottom(tankTurretHeight)
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
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
                        .withMoveableController(MoveableControllerBuilder.builder()
                              .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                              .withEndPositions(battleShipEndPositions)
                              .withLazyMoveable(() -> battleShipHolder.getTankGridElement())
                              .withPostMoveForwardHandler(res -> {
                              })
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> battleShipHolder.getTankGridElement())
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
                              .withDetectingGridElementFilter(
                                    FilterGridElementsMovingAway.of(() -> battleShipHolder.getTankGridElement())
                                          .and(projectileGridElementsFilter::test))
                              .build())
                        .build())
                  .withTurret(TurretClusterBuilder.builder()
                        .withPosition(battleShipPos)
                        .withTurret(TankTurretBuilder.builder()
                              .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                              .withPositionTransformator(transformedTankPos -> Positions.movePositionBackward4Distance(transformedTankPos, 70))
                              .withDetector(DetectorBuilder.builder()
                                    .withAngleInc(detectorConfig.getEvasionAngleInc())
                                    .withDetectorAngle(detectorConfig.getDetectorAngle())
                                    .withDetectorReach(detectorConfig.getDetectorReach())
                                    .withEvasionAngle(detectorConfig.getDetectorAngle())
                                    .withEvasionDistance(detectorConfig.getEvasionDistance())
                                    .build())
                              .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                              .withGunCarriage(SimpleGunCarriageBuilder.builder()
                                    .withRotationSpeed(2)
                                    .withGun(BulletGunBuilder.builder()
                                          .withGunConfig(GunConfigBuilder.builder()
                                                .withSalveSize(2)
                                                .withRoundsPerMinute(150)
                                                .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                      .withDimensionInfo(DimensionInfoBuilder.builder()
                                                            .withDimensionRadius(3)
                                                            .withHeightFromBottom(3)
                                                            //                                                            .withDistanceToGround(tankHeightFromGround + tankTurretHeight)
                                                            .build())
                                                      .withVelocity(projectileVelocity)
                                                      .build())
                                                .build())
                                          .withGunShape(GunShapeBuilder.builder()
                                                .withBarrel(RectangleBuilder.builder()
                                                      .withHeight(gunHeight)
                                                      .withWidth(gunWidth)
                                                      .withCenter(tankTurret1Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .withMuzzleBreak(RectangleBuilder.builder()
                                                      .withHeight(gunWidth * 1.5)
                                                      .withWidth(gunWidth * 1.5)
                                                      .withCenter(tankTurret1Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .build())
                                          .build())
                                    .withShape(CircleBuilder.builder()
                                          .withRadius(gunCarriageRadius)
                                          .withAmountOfPoints(gunCarriageRadius)
                                          .withCenter(tankTurret1Pos)
                                          .build())
                                    .build())
                              .build())
                        .withTurret(TankTurretBuilder.builder()
                              .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                              .withPositionTransformator(transformedTankPos -> transformedTankPos)
                              .withDetector(DetectorBuilder.builder()
                                    .withAngleInc(detectorConfig.getEvasionAngleInc())
                                    .withDetectorAngle(detectorConfig.getDetectorAngle())
                                    .withDetectorReach(detectorConfig.getDetectorReach())
                                    .withEvasionAngle(detectorConfig.getDetectorAngle())
                                    .withEvasionDistance(detectorConfig.getEvasionDistance())
                                    .build())
                              .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                              .withGunCarriage(SimpleGunCarriageBuilder.builder()
                                    .withRotationSpeed(2)
                                    .withGun(BulletGunBuilder.builder()
                                          .withGunConfig(GunConfigBuilder.builder()
                                                .withSalveSize(2)
                                                .withRoundsPerMinute(180)
                                                .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                      .withDimensionInfo(DimensionInfoBuilder.builder()
                                                            .withDimensionRadius(3)
                                                            .withHeightFromBottom(3)
                                                            //                                                            .withDistanceToGround(tankHeightFromGround + tankTurretHeight)
                                                            .build())
                                                      .withVelocity(projectileVelocity)
                                                      .build())
                                                .build())
                                          .withGunShape(GunShapeBuilder.builder()
                                                .withBarrel(RectangleBuilder.builder()
                                                      .withHeight(gunHeight)
                                                      .withWidth(gunWidth)
                                                      .withCenter(tankTurret3Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .withMuzzleBreak(RectangleBuilder.builder()
                                                      .withHeight(gunWidth * 1.5)
                                                      .withWidth(gunWidth * 1.5)
                                                      .withCenter(tankTurret3Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .build())
                                          .build())
                                    .withShape(CircleBuilder.builder()
                                          .withRadius(gunCarriageRadius)
                                          .withAmountOfPoints(gunCarriageRadius)
                                          .withCenter(tankTurret3Pos)
                                          .build())
                                    .build())
                              .build())
                        .withTurret(TankTurretBuilder.builder()
                              .withParkingAngleEvaluator(() -> battleShipParkingAngle)
                              .withPositionTransformator(transformedTankPos -> Positions.movePositionForward4Distance(transformedTankPos, 70))
                              .withDetector(DetectorBuilder.builder()
                                    .withAngleInc(detectorConfig.getEvasionAngleInc())
                                    .withDetectorAngle(detectorConfig.getDetectorAngle())
                                    .withDetectorReach(detectorConfig.getDetectorReach())
                                    .withEvasionAngle(detectorConfig.getDetectorAngle())
                                    .withEvasionDistance(detectorConfig.getEvasionDistance())
                                    .build())
                              .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                              .withGunCarriage(SimpleGunCarriageBuilder.builder()
                                    .withRotationSpeed(2)
                                    .withGun(BulletGunBuilder.builder()
                                          .withGunConfig(GunConfigBuilder.builder()
                                                .withSalveSize(2)
                                                .withRoundsPerMinute(180)
                                                .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                      .withDimensionInfo(DimensionInfoBuilder.builder()
                                                            .withDimensionRadius(3)
                                                            .withHeightFromBottom(3)
                                                            .build())
                                                      .withVelocity(projectileVelocity)
                                                      .build())
                                                .build())
                                          .withGunShape(GunShapeBuilder.builder()
                                                .withBarrel(RectangleBuilder.builder()
                                                      .withHeight(gunHeight)
                                                      .withWidth(gunWidth)
                                                      .withCenter(tankTurret2Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .withMuzzleBreak(RectangleBuilder.builder()
                                                      .withHeight(gunWidth * 1.5)
                                                      .withWidth(gunWidth * 1.5)
                                                      .withCenter(tankTurret2Pos)
                                                      .withOrientation(Orientation.HORIZONTAL)
                                                      .build())
                                                .build())
                                          .build())
                                    .withShape(CircleBuilder.builder()
                                          .withRadius(gunCarriageRadius)
                                          .withAmountOfPoints(gunCarriageRadius)
                                          .withCenter(tankTurret2Pos)
                                          .build())
                                    .build())
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(battleShipPos)
                        .withHeight(tankHeight * 2)
                        .withWidth(2 * tankWidth / 3)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withTankStrategy(TankStrategy.ALWAYS_MOVE_AND_SHOOT)
                  .build())
            .build();

      TurretGridElement northTurretGridElement = TurretGridElementBuilder.builder()
            .withGrid(grid)
            .withHeightFromBottom(turretHeight)
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(detectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(detectorConfig.getDetectorAngle())
                        .withDetectorReach(detectorConfig.getDetectorReach())
                        .withEvasionAngle(detectorConfig.getDetectorAngle())
                        .withEvasionDistance(detectorConfig.getEvasionDistance())
                        .build())
                  .withGridElementEvaluator((position, distance) -> grid.getAllGridElementsWithinDistance(position, distance))
                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
                        .withRotationSpeed(2)
                        .withGun(BulletGunBuilder.builder()
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(2)
                                    .withRoundsPerMinute(100)
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

      tankHolder.setAndReturnTank(tankGridElement);
      tankHolder.setTankGridElement(tankGridElement);
      battleShipHolder.setTankGridElement(battleShipGridElement);
      battleShipHolder.setAndReturnTank(battleShipGridElement);

      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);

      List<Renderer> renderers = new ArrayList<>();
      renderers.add(new GridElementPainter(northTurretGridElement, getColor(northTurretGridElement), height, width));
      renderers.add(new GridElementPainter(simpleGridElement, getColor(simpleGridElement), height, width));
      renderers.add(new GridElementPainter(tankGridElement, getColor(tankGridElement), height, width));
      renderers.add(new GridElementPainter(battleShipGridElement, getColor(battleShipGridElement), height, width));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers);
   }

   private static Moveable buildNewMoveable(Grid grid) {
      double yCordinate = MathUtil.getRandom(450) + padding;
      double angle2Rotate = -MathUtil.getRandom(90) + 15;
      Position gridElementPos = Positions.of(200, yCordinate).rotate(angle2Rotate);
      int gridElementRadius = 10;
      return MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(gridElementPos)
            .withHealth(480)
            .withShape(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .withVelocity(5)
            .build();
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Renderer> renderers) {
      Set<String> existingProjectiles = new HashSet<>();
      SwingUtilities.invokeLater(() -> mainWindow.show());
      MoveableAdder moveableAdder = new MoveableAdder();
      int cycleTime = 15;
      new Thread(() -> {
         int cycleCounter = 0;
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            addNewProjectilePainters(grid, renderers, existingProjectiles);
            removeDestroyedPainters(renderers);
            grid.getAllGridElements(null).parallelStream()
                  .filter(isGridElementAlive(grid))
                  .filter(AutoDetectable.class::isInstance)
                  .map(AutoDetectable.class::cast)
                  .forEach(AutoDetectable::autodetect);
            cycleCounter++;
            if (moveableAdder.check4NewMoveables2Add(grid, renderers, cycleCounter, padding)) {
               cycleCounter = 0;
            }
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
         }
      }, "LogicHandler").start();
   }

   private static Predicate<? super GridElement> isGridElementAlive(Grid grid) {
      return gridElement -> grid.containsElement(gridElement);
   }
}
