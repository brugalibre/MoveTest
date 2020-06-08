package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.addNewProjectilePainters;
import static com.myownb3.piranha.launch.weapon.ProjectilePaintUtil.removeDestroyedPainters;
import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
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
import com.myownb3.piranha.core.weapon.gun.shape.GunShapeImpl.GunShapeBuilder;
import com.myownb3.piranha.core.weapon.guncarriage.SimpleGunCarriageImpl.SimpleGunCarriageBuilder;
import com.myownb3.piranha.core.weapon.turret.Turret;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.weapon.turret.TurretGridElement.TurretGridElementBuilder;
import com.myownb3.piranha.core.weapon.turret.TurretImpl.GenericTurretBuilder.TurretBuilder;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainter;
import com.myownb3.piranha.ui.render.impl.moveable.MoveablePainterConfig;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;

public class TurretTowerTestLauncher {
   public static void main(String[] args) throws InterruptedException {

      TurretTowerTestLauncher launcher = new TurretTowerTestLauncher();
      launcher.launch();
   }

   private void launch() throws InterruptedException {

      int padding = 30;
      int width = 30;
      int height = 5;
      int radius = 10;
      Position gridElementPos = Positions.of(50, 450).rotate(-90);
      Position northTurretPos = Positions.of(400, 200).rotate(120);
      Position southTurretPos = Positions.of(350, 300);

      Position endPointMoveableStartPosition = Positions.of(50, 50).rotate(-60);

      DetectorConfig turretDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorAngle(360)
            .withDetectorReach(300)
            .withEvasionAngle(0)
            .withEvasionDistance(0)
            .build();

      MirrorGrid grid = MirrorGridBuilder.builder()
            .withCollisionDetectionHandler(BouncingCollisionDetectionHandlerBuilder.builder()
                  .withBouncedPositionEvaluator(new BouncedPositionEvaluatorImpl())
                  .build())
            .withMaxX(500)
            .withMaxY(500)
            .withMinX(padding)
            .withMinY(padding)
            .build();

      TurretGridElement turretGridElementNorth = TurretGridElementBuilder.builder()
            .withGrid(grid)
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(turretDetectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(turretDetectorConfig.getDetectorAngle())
                        .withDetectorReach(turretDetectorConfig.getDetectorReach())
                        .withEvasionAngle(turretDetectorConfig.getDetectorAngle())
                        .withEvasionDistance(turretDetectorConfig.getEvasionDistance())
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
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(height)
                                          .withWidth(width)
                                          .withCenter(northTurretPos)
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(radius)
                              .withAmountOfPoints(radius)
                              .withCenter(northTurretPos)
                              .build())
                        .build())
                  .build())
            .build();

      TurretGridElement turretGridElementSouth = TurretGridElementBuilder.builder()
            .withGrid(grid)
            .withTurret(TurretBuilder.builder()
                  .withDetector(DetectorBuilder.builder()
                        .withAngleInc(turretDetectorConfig.getEvasionAngleInc())
                        .withDetectorAngle(turretDetectorConfig.getDetectorAngle())
                        .withDetectorReach(turretDetectorConfig.getDetectorReach())
                        .withEvasionAngle(turretDetectorConfig.getDetectorAngle())
                        .withEvasionDistance(turretDetectorConfig.getEvasionDistance())
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
                              .withGunShape(GunShapeBuilder.builder()
                                    .withBarrel(RectangleBuilder.builder()
                                          .withHeight(height)
                                          .withWidth(width)
                                          .withCenter(southTurretPos)
                                          .withOrientation(Orientation.VERTICAL)
                                          .build())
                                    .build())
                              .build())
                        .withShape(CircleBuilder.builder()
                              .withRadius(radius)
                              .withAmountOfPoints(radius)
                              .withCenter(southTurretPos)
                              .build())
                        .build())
                  .build())
            .build();

      DetectorConfig mainDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(90)
            .withEvasionDistance(90)
            .withDetectorAngle(80)
            .withEvasionAngle(80)
            .withEvasionAngleInc(4)
            .build();
      EndPointMoveable endPointMoveable = buildEndPointMoveable(radius, endPointMoveableStartPosition, grid, mainDetectorConfig);

      Moveable simpleGridElement = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withPosition(gridElementPos)
            .withShape(CircleBuilder.builder()
                  .withRadius(radius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .build();

      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);

      List<Renderer> renderers = new ArrayList<Renderer>();
      List<TurretGridElement> turrets = Arrays.asList(turretGridElementSouth, turretGridElementNorth);
      List<Moveable> moveables = Arrays.asList(simpleGridElement/*, endPointMoveable*/);

      renderers.addAll(
            turrets.stream().map(turret -> new GridElementPainter(turret, getColor(turret), height, width)).collect(Collectors.toList()));
      renderers.addAll(moveables.stream()
            .map(moveable -> moveable instanceof EndPointMoveable
                  ? buildMoveablePainter(width, height, moveable, mainDetectorConfig)
                  : new GridElementPainter(moveable, GridElementColorUtil.getColor(moveable), height, width))
            .collect(Collectors.toList()));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, moveables, turrets, renderers);
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
         List<TurretGridElement> turretTowers, List<Renderer> renderers) {
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
            turretTowers.stream()
                  .forEach(Turret::autodetect);
            synchronized (moveables) {
               moveables.stream()
                     .forEach(moveable -> moveable.moveForward(10));
            }
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
         }
      }, "LogicHandler").start();
   }

}
