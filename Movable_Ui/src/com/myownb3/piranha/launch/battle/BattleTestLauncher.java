package com.myownb3.piranha.launch.battle;

import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.addNewProjectilePainters;
import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.removeDestroyedPainters;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl.GridElementDetectorBuilder;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.filter.FilterGridElementsMovingAway;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
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
import com.myownb3.piranha.core.weapon.tank.turret.TankTurret.TankTurretBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;
import com.myownb3.piranha.worker.WorkerThreadFactory;

public class BattleTestLauncher {
   private static final int padding = 30;

   public static void main(String[] args) throws InterruptedException {
      BattleTestLauncher launcher = new BattleTestLauncher();
      launcher.launch();
   }

   private void launch() throws InterruptedException {

      int width = 30;
      int height = 5;
      Position imperialTankPos = Positions.of(450, 600).rotate(80);
      Position rebelTankPos = Positions.of(200, 100);

      List<EndPosition> imperialTankEndPositions = new ArrayList<>();
      imperialTankEndPositions.add(EndPositions.of(Positions.of(450, 100), 10));
      imperialTankEndPositions.add(EndPositions.of(Positions.of(imperialTankPos), 10));

      List<EndPosition> rebelTankEndPositions = new ArrayList<>();
      rebelTankEndPositions.add(EndPositions.of(Positions.of(200, 600), 10));
      rebelTankEndPositions.add(EndPositions.of(Positions.of(rebelTankPos), 10));

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withCollisionDetectionHandler(BouncingCollisionDetectionHandlerBuilder.builder()
                  .withBouncedPositionEvaluator(new BouncedPositionEvaluatorImpl())
                  .build())
            .withMaxX(700)
            .withMaxY(600)
            .withMinX(padding)
            .withMinY(padding)
            .build();

      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(350)
            .withDetectorAngle(180)
            .build();

      TankHolder imperialTankHolder = new TankHolder();
      TankGridElement imperialTank = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(5)
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
                              .withEndPositions(imperialTankEndPositions)
                              //                              .withEndPositions(rebelTankEndPositions)
                              .withLazyMoveable(() -> imperialTankHolder.getTankGridElement())
                              .withPostMoveForwardHandler(res -> {
                              })
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> imperialTankHolder.getTankGridElement())
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
                              .withDetectingGridElementFilter(FilterGridElementsMovingAway.of(() -> imperialTankHolder.getTankGridElement()))
                              .build())
                        .build())
                  .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                  .withTurret(TankTurretBuilder.builder()
                        .withParkingAngleEvaluator(() -> imperialTankHolder.getPosition().getDirection().getAngle())
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
                                                .withDimension(new DimensionImpl(0, 0, 3, 3))
                                                .withVelocity(7)
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
                        .withBelligerentParty(BelligerentPartyConst.GALACTIC_EMPIRE)
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(imperialTankPos)
                        .withHeight(tankHeight)
                        .withWidth(tankWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();

      TankHolder rebellTankHolder = new TankHolder();
      TankGridElement rebellTank = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withEngineVelocity(5)
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
                              .withEndPositions(rebelTankEndPositions)
                              .withLazyMoveable(() -> rebellTankHolder.getTankGridElement())
                              .withPostMoveForwardHandler(res -> {
                              })
                              .build())
                        .build())
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> rebellTankHolder.getTankGridElement())
                        .withGridElementDetector(new GridElementDetectorImpl(grid, TrippleDetectorClusterBuilder.builder()
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
                              .build()))
                        .build())
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTurret(TankTurretBuilder.builder()
                        .withParkingAngleEvaluator(() -> rebellTankHolder.getPosition().getDirection().getAngle())
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
                                                .withDimension(new DimensionImpl(0, 0, 3, 3))
                                                .withVelocity(10)
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
                              .build())
                        .build())
                  .withHull(RectangleBuilder.builder()
                        .withCenter(rebelTankPos)
                        .withHeight(tankHeight)
                        .withWidth(tankWidth)
                        .withOrientation(Orientation.HORIZONTAL)
                        .build())
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withTankStrategy(TankStrategy.WAIT_WHILE_SHOOTING_MOVE_UNDER_FIRE)
                  .build())
            .build();

      rebellTankHolder.setAndReturnTank(rebellTank);
      rebellTankHolder.setTankGridElement(rebellTank);
      imperialTankHolder.setAndReturnTank(imperialTank);
      imperialTankHolder.setTankGridElement(imperialTank);

      grid.prepare();
      WorkerThreadFactory.INSTANCE.restart();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);

      List<Renderer> renderers = new ArrayList<>();
      List<AutoDetectable> autoDetectables = Arrays.asList(imperialTank, rebellTank);
      List<Moveable> moveables = new ArrayList<>();

      renderers.addAll(autoDetectables.stream().map(tank -> new GridElementPainter((GridElement) tank, getColor((GridElement) tank), height, width))
            .collect(Collectors.toList()));
      renderers.addAll(moveables.stream()
            .map(moveable -> new GridElementPainter(moveable, GridElementColorUtil.getColor(moveable), height, width))
            .collect(Collectors.toList()));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, autoDetectables, renderers);
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<AutoDetectable> autoDetectables,
         List<Renderer> renderers) {
      Set<String> existingProjectiles = new HashSet<>();
      SwingUtilities.invokeLater(() -> mainWindow.show());

      int cycleTime = 15;

      new Thread(() -> {
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            autoDetectables.stream()
                  .forEach(AutoDetectable::autodetect);
            addNewProjectilePainters(grid, renderers, existingProjectiles);
            removeDestroyedPainters(renderers);
            new ArrayList<>(grid.getAllGridElements(null)).parallelStream()
                  .filter(Moveable.class::isInstance)
                  .map(Moveable.class::cast)
                  .filter(isGridElementAlive(grid))
                  .forEach(moveable -> moveable.moveForward(10));

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
