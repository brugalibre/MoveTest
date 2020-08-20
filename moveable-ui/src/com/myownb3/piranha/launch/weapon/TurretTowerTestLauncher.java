package com.myownb3.piranha.launch.weapon;

import static com.myownb3.piranha.ui.render.util.GridElementColorUtil.getColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

import com.myownb3.piranha.application.battle.TankBattleApplication;
import com.myownb3.piranha.application.battle.impl.TankBattleApplicationImpl.TankBattleApplicationBuilder;
import com.myownb3.piranha.application.battle.impl.turret.TankBattleApplicationTurretBuilder;
import com.myownb3.piranha.application.battle.impl.MoveableAdderImpl.MoveableAdderBuilder;
import com.myownb3.piranha.audio.constants.AudioConstants;
import com.myownb3.piranha.audio.impl.AudioClipImpl.AudioClipBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentPartyConst;
import com.myownb3.piranha.core.battle.weapon.gun.config.GunConfigImpl.GunConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileTypes;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.config.ProjectileConfigImpl.ProjectileConfigBuilder;
import com.myownb3.piranha.core.battle.weapon.turret.TurretGridElement;
import com.myownb3.piranha.core.collision.bounce.impl.BouncedPositionEvaluatorImpl;
import com.myownb3.piranha.core.collision.bounce.impl.BouncingCollisionDetectionHandlerImpl.BouncingCollisionDetectionHandlerBuilder;
import com.myownb3.piranha.core.detector.config.DetectorConfig;
import com.myownb3.piranha.core.detector.config.impl.DetectorConfigImpl.DetectorConfigBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.MirrorGrid;
import com.myownb3.piranha.core.grid.MirrorGrid.MirrorGridBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Orientation;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.EndPointMoveable;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineConfigBuilder;
import com.myownb3.piranha.ui.application.LogicHandler;
import com.myownb3.piranha.ui.application.MainWindow;
import com.myownb3.piranha.ui.application.impl.LogicHandlerImpl;
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

   private void launch() {

      int padding = 30;
      int width = 30;
      int height = 5;
      int radius = 10;
      Position gridElementPos = Positions.of(50, 450).rotate(-90);
      Position northTurretPos = Positions.of(400, 200).rotate(120);
      Position southTurretPos = Positions.of(350, 300);

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

      int projectileVelocity = 10;
      TankBattleApplication tankBattleApplication = TankBattleApplicationBuilder.builder()
            .withGrid(grid)
            .withMoveableAdder(MoveableAdderBuilder.builder()
                  .withCounter(Integer.MAX_VALUE)
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .build())
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withGrid(grid)
                  .withGunOrientation(Orientation.VERTICAL)
                  .withDetectorConfig(turretDetectorConfig)
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
                                    .withHeightFromBottom(0)
                                    .build())
                              .withVelocity(projectileVelocity)
                              .build())
                        .build())
                  .withGunCarriageShape(CircleBuilder.builder()
                        .withRadius(radius)
                        .withAmountOfPoints(radius)
                        .withCenter(northTurretPos)
                        .build())
                  .withGunHeight(height)
                  .withGunWidth(width)
                  .withTurretPosition(northTurretPos)
                  .withTurretRotationSpeed(3)
                  .build())
            .addTurretGridElement(TankBattleApplicationTurretBuilder.builder()
                  .withBelligerentParty(BelligerentPartyConst.REBEL_ALLIANCE)
                  .withGrid(grid)
                  .withDetectorConfig(turretDetectorConfig)
                  .withProjectileType(ProjectileTypes.BULLET)
                  .withGunOrientation(Orientation.VERTICAL)
                  .withGunConfig(GunConfigBuilder.builder()
                        .withAudioClip(AudioClipBuilder.builder()
                              .withAudioResource(AudioConstants.BULLET_SHOT_SOUND)
                              .build())
                        .withSalveSize(1)
                        .withRoundsPerMinute(350)
                        .withProjectileConfig(ProjectileConfigBuilder.builder()
                              .withDimensionInfo(DimensionInfoBuilder.builder()
                                    .withDimensionRadius(3)
                                    .withHeightFromBottom(0)
                                    .build())
                              .withVelocity(projectileVelocity)
                              .build())
                        .build())
                  .withGunCarriageShape(CircleBuilder.builder()
                        .withRadius(radius)
                        .withAmountOfPoints(radius)
                        .withCenter(southTurretPos)
                        .build())
                  .withGunHeight(height)
                  .withGunWidth(width)
                  .withTurretPosition(southTurretPos)
                  .withTurretRotationSpeed(3)
                  .build())
            .build();

      DetectorConfig mainDetectorConfig = DetectorConfigBuilder.builder()
            .withDetectorReach(90)
            .withEvasionDistance(90)
            .withDetectorAngle(80)
            .withEvasionAngle(80)
            .withEvasionAngleInc(4)
            .build();

      Moveable simpleGridElement = MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withShape(CircleBuilder.builder()
                  .withRadius(radius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .withVelocity(3)
            .build();

      grid.prepare();
      MainWindow mainWindow = new MainWindow(grid.getDimension().getWidth(), grid.getDimension().getHeight(), padding, width);
      List<Renderer<?>> renderers = new ArrayList<>();
      List<TurretGridElement> turrets = tankBattleApplication.getTurretGridElements();
      List<Moveable> moveables = Arrays.asList(simpleGridElement/*, endPointMoveable*/);

      renderers.addAll(
            turrets.stream().map(turret -> new GridElementPainter(turret, getColor(turret))).collect(Collectors.toList()));
      renderers.addAll(moveables.stream()
            .map(moveable -> moveable instanceof EndPointMoveable
                  ? buildMoveablePainter(moveable, mainDetectorConfig)
                  : new GridElementPainter(moveable, GridElementColorUtil.getColor(moveable)))
            .collect(Collectors.toList()));

      mainWindow.addSpielfeld(renderers, grid);
      showGuiAndStartPainter(mainWindow, grid, renderers, tankBattleApplication);
   }

   private MoveablePainter buildMoveablePainter(Moveable moveable, DetectorConfig mainDetectorConfig) {
      return new MoveablePainter(moveable, getColor(moveable), MoveablePainterConfig.of(EvasionStateMachineConfigBuilder.builder()
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

   private static void showGuiAndStartPainter(MainWindow mainWindow, Grid grid,
         List<Renderer<?>> renderers, TankBattleApplication tankBattleApplication) {
      SwingUtilities.invokeLater(() -> mainWindow.show());
      int cycleTime = 15;
      LogicHandler logicHandler = new LogicHandlerImpl(renderers, tankBattleApplication);
      grid.addOnGridElementAddListener(logicHandler);
      new Thread(() -> {
         while (true) {
            tankBattleApplication.run();
            logicHandler.onCylce();
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
         }
      }, "LogicHandlerImpl").start();
      new Thread(() -> {
         while (true) {
            SwingUtilities.invokeLater(() -> mainWindow.refresh());
            try {
               Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
               Thread.currentThread().interrupt();
            }
         }
      }, "UIRefresher").start();
   }

}
