package com.myownb3.piranha.launch.weapon;

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

import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.GridElementDetectorImpl;
import com.myownb3.piranha.core.detector.cluster.tripple.TrippleDetectorClusterImpl.TrippleDetectorClusterBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.detector.strategy.DetectingStrategy;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.EndPositions;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.AutoDetectable;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.weapon.tank.TankHolder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.tank.detector.TankDetectorImpl.TankDetectorBuilder;
import com.myownb3.piranha.core.weapon.tank.turret.TankTurret.TankTurretBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;
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
      Position turretSouthPos = Positions.of(390, 430).rotate(120);
      Position turretNorthPos = Positions.of(390, 70).rotate(60);
      Position tankPos = Positions.of(480, 100).rotate(180);
      Position tankTurretPos = Positions.movePositionForward4Distance(tankPos, 20);

      List<EndPosition> endPositions = new ArrayList<>();
      endPositions.add(EndPositions.of(Positions.of(350, 210), 10));
      endPositions.add(EndPositions.of(Positions.of(450, 400), 10));
      endPositions.add(EndPositions.of(Positions.of(450, 100), 10));

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withCollisionDetectionHandler(BouncingCollisionDetectionHandlerBuilder.builder()
                  .withBouncedPositionEvaluator(new BouncedPositionEvaluatorImpl())
                  .build())
            .withMaxX(500)
            .withMaxY(500)
            .withMinX(padding)
            .withMinY(padding)
            .build();

      int tankWidth = 40;
      int tankHeight = 90;
      int gunCarriageRadius = 10;
      double gunHeight = 25;
      double gunWidth = 7;

      DetectorConfig detectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(250)
            .withDetectorAngle(360)
            .build();

      Moveable simpleGridElement = buildNewMoveable(grid);
      TankHolder tankHolder = new TankHolder();

      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withTank(TankBuilder.builder()
                  .withTankDetector(TankDetectorBuilder.builder()
                        .withTankGridElement(() -> tankHolder.getTankGridElement())
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
                  .withGrid(grid)
                  .withEngineVelocity(10)
                  .withEndPositions(endPositions)
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
                                          .withRoundsPerMinute(200)
                                          .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                .withDimension(new DimensionImpl(0, 0, 3, 3))
                                                .build())
                                          .withVelocity(3)
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
                  .build())
            .build();

      TurretGridElement southTurretGridElement = TurretGridElementBuilder.builder()
            .withGrid(grid)
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
                        .withRotationSpeed(3)
                        .withGun(BulletGunBuilder.builder()
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(100)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimension(new DimensionImpl(0, 0, 3, 3))
                                          .build())
                                    .withVelocity(3)
                                    .build())
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(turretSouthPos)
                                          .withOrientation(Orientation.HORIZONTAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(gunCarriageRadius)
                              .withAmountOfPoints(gunCarriageRadius)
                              .withCenter(turretSouthPos)
                              .build())
                        .build())
                  .build())
            .build();
      TurretGridElement northTurretGridElement = TurretGridElementBuilder.builder()
            .withGrid(grid)
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
                        .withRotationSpeed(3)
                        .withGun(BulletGunBuilder.builder()
                              .withGunConfig(GunConfigBuilder.builder()
                                    .withSalveSize(1)
                                    .withRoundsPerMinute(100)
                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
                                          .withDimension(new DimensionImpl(0, 0, 3, 3))
                                          .build())
                                    .withVelocity(3)
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
      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);

      List<Renderer> renderers = new ArrayList<>();
      List<AutoDetectable> autoDetectables = Arrays.asList(tankGridElement, southTurretGridElement, northTurretGridElement);

      renderers.addAll(autoDetectables.stream().map(tank -> new GridElementPainter((GridElement) tank, getColor((GridElement) tank), height, width))
            .collect(Collectors.toList()));
      renderers.add(new GridElementPainter(southTurretGridElement, getColor(southTurretGridElement), height, width));
      renderers.add(new GridElementPainter(simpleGridElement, getColor(simpleGridElement), height, width));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, autoDetectables, renderers);
   }

   private static Moveable buildNewMoveable(Grid grid) {
      double yCordinate = MathUtil.getRandom(450) + padding;
      double angle2Rotate = -MathUtil.getRandom(90) + 15;
      Position gridElementPos = Positions.of(100, yCordinate).rotate(angle2Rotate);
      int gridElementRadius = 10;
      return MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(gridElementPos)
            .withHealth(80)
            .withShape(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<AutoDetectable> autoDetectables, List<Renderer> renderers) {
      Set<String> existingProjectiles = new HashSet<>();
      List<Moveable> moveables = new ArrayList<>();
      SwingUtilities.invokeLater(() -> mainWindow.show());

      int cycleTime = 15;
      new Thread(() -> {
         int cycleCounter = 0;
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            autoDetectables.stream()
                  .filter(Tank.class::isInstance)
                  .forEach(AutoDetectable::autodetect);
            addNewProjectilePainters(grid, renderers, existingProjectiles);
            removeDestroyedPainters(renderers);
            new ArrayList<>(grid.getAllGridElements(null)).stream()
                  .filter(Moveable.class::isInstance)
                  .map(Moveable.class::cast)
                  .filter(isGridElementAlive(grid))
                  .filter(isNotTank())
                  .forEach(moveable -> moveable.moveForward(10));

            cycleCounter++;
            if (cycleCounter == 400) {
               cycleCounter = 0;
               double moveableCounter = moveables.stream()
                     .filter(isGridElementAlive(grid))
                     .count();
               if (moveableCounter <= 3) {
                  buildAndAddMoveable(grid, renderers, moveables);
                  buildAndAddSimpleGridElement(grid, renderers);
               }
            }
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
         }
      }, "LogicHandler").start();
   }

   private static Predicate<? super Moveable> isNotTank() {
      return moveable -> !(moveable instanceof TankGridElement);
   }

   private static void buildAndAddSimpleGridElement(Grid grid, List<Renderer> renderers) {
      double yCordinate = MathUtil.getRandom(450) + padding;
      double angle2Rotate = -MathUtil.getRandom(90) + 15;
      Position gridElementPos = Positions.of(300, yCordinate).rotate(angle2Rotate);
      int gridElementRadius = 10;
      ObstacleImpl obstacleImpl = ObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(gridElementPos)
            .withShape(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();
      synchronized (renderers) {
         renderers.add(new GridElementPainter(obstacleImpl, GridElementColorUtil.getColor(obstacleImpl), 0, 0));
      }
   }

   private static void buildAndAddMoveable(Grid grid, List<Renderer> renderers, List<Moveable> moveables) {
      Moveable moveable = buildNewMoveable(grid);
      renderers.add(new GridElementPainter(moveable, GridElementColorUtil.getColor(moveable), 0, 0));
      moveables.add(moveable);
   }

   private static Predicate<? super GridElement> isGridElementAlive(Grid grid) {
      return gridElement -> grid.containsElement(gridElement);
   }
}
