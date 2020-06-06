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
import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.DimensionImpl;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.RectangleImpl.RectangleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl.EndPointMoveableBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachine.EvasionStateMachineBuilder;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.BulletGunImpl.BulletGunBuilder;
import com.myownb3.piranha.core.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.weapon.tank.TankGridElement.TankGridElementBuilder;
import com.myownb3.piranha.core.weapon.tank.TankImpl.TankBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.TurretBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;

public class TankTestLauncher {
   public static void main(String[] args) throws InterruptedException {

      TankTestLauncher launcher = new TankTestLauncher();
      launcher.launch();
   }

   private void launch() throws InterruptedException {

      int padding = 30;
      int width = 30;
      int height = 5;
      Position turretPos = Positions.of(400, 400).rotate(120);
      Position tankPos = Positions.of(200, 200).rotate(-90);
      Position gridElementPos = Positions.of(300, 300).rotate(90);

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
            .withDetectorReach(390)
            .withDetectorAngle(180)
            .build();

      int gridElementRadius = 10;
      Moveable simpleGridElement = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(gridElementPos)
            .withShape(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();

      TankGridElement tankGridElement = TankGridElementBuilder.builder()
            .withGrid(grid)
            .withTank(TankBuilder.builder()
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
                                          .withRoundsPerMinute(350)
                                          .withProjectileConfig(ProjectileConfigBuilder.builder()
                                                .withDimension(new DimensionImpl(0, 0, 3, 3))
                                                .build())
                                          .withVelocity(3)
                                          .build())
                                    .withRectangle(RectangleBuilder.builder()
                                          .withHeight(gunHeight)
                                          .withWidth(gunWidth)
                                          .withCenter(tankPos)
                                          .withOrientation(Orientation.HORIZONTAL)
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
                  .build())
            .build();
      //
      //      TurretGridElement turretGridElement = TurretGridElementBuilder.builder()
      //            .withGrid(grid)
      //            .withTurret(TurretBuilder.builder()
      //                  .withDetector(DetectorBuilder.builder()
      //                        .withAngleInc(1)
      //                        .withDetectorAngle(1)
      //                        .withDetectorReach(1)
      //                        .withEvasionAngle(1)
      //                        .withEvasionDistance(1)
      //                        .build())
      //                  .withGridElementEvaluator((position, distance) -> Collections.emptyList())
      //                  .withGunCarriage(SimpleGunCarriageBuilder.builder()
      //                        .withRotationSpeed(3)
      //                        .withGun(BulletGunBuilder.builder()
      //                              .withGunConfig(GunConfigBuilder.builder()
      //                                    .withSalveSize(1)
      //                                    .withRoundsPerMinute(350)
      //                                    .withProjectileConfig(ProjectileConfigBuilder.builder()
      //                                          .withDimension(new DimensionImpl(0, 0, 3, 3))
      //                                          .build())
      //                                    .withVelocity(3)
      //                                    .build())
      //                              .withRectangle(RectangleBuilder.builder()
      //                                    .withHeight(gunHeight)
      //                                    .withWidth(gunWidth)
      //                                    .withCenter(turretPos)
      //                                    .withOrientation(Orientation.VERTICAL)
      //                                    .build())
      //                              .build())
      //                        .withShape(CircleBuilder.builder()
      //                              .withRadius(gunCarriageRadius)
      //                              .withAmountOfPoints(gunCarriageRadius)
      //                              .withCenter(turretPos)
      //                              .build())
      //                        .build())
      //                  .build())
      //            .build();

      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);

      List<Renderer> renderers = new ArrayList<>();
      List<TankGridElement> tanks = Arrays.asList(tankGridElement);
      List<Moveable> moveables = new ArrayList<>();
      moveables.add(tankGridElement);
      moveables.add(simpleGridElement);

      renderers.addAll(
            tanks.stream().map(tank -> new GridElementPainter(tank, getColor(tank), height, width)).collect(Collectors.toList()));
      //      renderers.add(new GridElementPainter(turretGridElement, getColor(turretGridElement), height, width));
      renderers.addAll(moveables.stream()
            .map(moveable -> new GridElementPainter(moveable, GridElementColorUtil.getColor(moveable), height, width))
            .collect(Collectors.toList()));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, moveables, tanks, renderers);
   }

   private EndPointMoveable buildEndPointMoveable(int radius, Position endPointMoveableStartPosition, MirrorGrid grid, DetectorConfig config) {
      return EndPointMoveableBuilder.builder()
            .withGrid(grid)
            .withStartPosition(endPointMoveableStartPosition)
            .withShape(CircleBuilder.builder()
                  .withRadius(radius)
                  .withAmountOfPoints(20)
                  .withCenter(endPointMoveableStartPosition)
                  .build())
            .withMoveablePostActionHandler(EvasionStateMachineBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withDetectorReach(config.getDetectorReach())
                        .withEvasionDistance(config.getEvasionDistance())
                        .withDetectorAngle(config.getDetectorAngle())
                        .withEvasionAngle(config.getEvasionAngle())
                        .withAngleInc(config.getEvasionAngleInc())
                        .build())
                  .withEvasionStateMachineConfig(EvasionStateMachineConfigBuilder.builder()
                        .withReturningAngleIncMultiplier(1)
                        .withOrientationAngle(1)
                        .withReturningMinDistance(0.06)
                        .withReturningAngleMargin(0.7d)
                        .withPassingDistance(25)
                        .withPostEvasionReturnAngle(4)
                        .withDetectorConfig(config)
                        .build())
                  .build())
            .withMovingIncrement(2)
            .build();
   }

   private MoveablePainter buildMoveablePainter(int width, int height, Moveable moveable, DetectorConfig mainDetectorConfig) {
      return new MoveablePainter(moveable, getColor(moveable), height, width,
            MoveablePainterConfig.of(EvasionStateMachineConfigBuilder.builder()
                  .withReturningAngleIncMultiplier(1)
                  .withOrientationAngle(1)
                  .withReturningMinDistance(0.06)
                  .withReturningAngleMargin(0.7d)
                  .withPassingDistance(25)
                  .withPostEvasionReturnAngle(4)
                  .withDetectorConfig(mainDetectorConfig)
                  .build(),
                  true, false));
   }

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid, List<Moveable> simpleGridElement,
         List<TankGridElement> tanks, List<Renderer> renderers) {
      Set<String> existingProjectiles = new HashSet<>();
      List<Moveable> moveables = new ArrayList<>();
      moveables.addAll(simpleGridElement);
      SwingUtilities.invokeLater(() -> mainWindow.show());
      int cycleTime = 15;
      new Thread(() -> {
         while (true) {
            addNewProjectilePainters(grid, renderers, existingProjectiles, moveables);
            removeDestroyedPainters(renderers);
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
         }
      }, "GUI-Refresher").start();
      new Thread(() -> {
         while (true) {
            tanks.stream()
                  .forEach(Tank::autodetect);
            synchronized (moveables) {
               moveables.stream()
                     .filter(isGridElementAlive())
                     .forEach(moveable -> moveable.moveForward(10));
            }
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
         }
      }, "LogicHandler").start();
   }

   private static Predicate<? super Moveable> isGridElementAlive() {
      return gridElement -> gridElement instanceof Destructible ? !((Destructible) gridElement).isDestroyed() : true;
   }

}
