package com.myownb3.piranha.launch.weapon.listener;

import static com.myownb3.piranha.util.MathUtil.getRandom;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape.TIEFighterShapeImpl.TIEFighterShapeBuilder;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.destruction.DestructionAudio;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper;
import com.myownb3.piranha.core.battle.destruction.DestructionHelper.DestructionHelperBuilder;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.battle.weapon.tank.TankGridElement;
import com.myownb3.piranha.core.detector.DetectorImpl.DetectorBuilder;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.lazy.LazyEndPoinMoveable;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.position.EndPosition;
import com.myownb3.piranha.core.grid.position.EndPositions;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.grid.position.Positions;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController;
import com.myownb3.piranha.core.moveables.controller.AutoMoveableController.AutoMoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MoveableController.MoveableControllerBuilder;
import com.myownb3.piranha.core.moveables.controller.MovingStrategy;
import com.myownb3.piranha.core.statemachine.impl.EvasionStateMachineImpl.EvasionStateMachineBuilder;
import com.myownb3.piranha.ui.application.evasionstatemachine.config.DefaultConfig;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;
import com.myownb3.piranha.util.MathUtil;

public class MoveableAdder {

   private int amountOfNonMoveables = 5;
   private int amountOfMoveables = 5;
   private int moveableVelocity = 20;
   private int counter = 50;
   private double maxX;
   private double maxY;
   private BelligerentParty belligerent;

   public MoveableAdder(int maxX, int maxY) {
      this.maxX = maxX;
      this.maxY = maxY;
   }

   public MoveableAdder(int maxX, int maxY, int moveableVelocity, int counter, BelligerentParty belligerentParty) {
      this(maxX, maxY);
      this.moveableVelocity = moveableVelocity;
      this.counter = counter;
      this.belligerent = belligerentParty;
   }

   public boolean check4NewMoveables2Add(Grid grid, List<Renderer<? extends GridElement>> renderers, int cycleCounter, int padding) {
      if (cycleCounter >= counter) {
         double moveableCounter = countMoveables(grid);
         double simpleGridElementCounter = countNonMoveables(grid);
         if (moveableCounter <= amountOfMoveables) {
            buildAndAddMoveable(grid, renderers, padding);
         }
         if (simpleGridElementCounter <= amountOfNonMoveables) {
            buildAndAddSimpleGridElement(grid, renderers, padding);
         }
         return true;
      }
      return false;
   }

   private double countNonMoveables(Grid grid) {
      return grid.getAllGridElements(null).stream()
            .filter(isMoveable().negate())
            .filter(isNotProjectile())
            .filter(isObstacle())
            .filter(isGridElementAlive())
            .count();
   }

   private double countMoveables(Grid grid) {
      return grid.getAllGridElements(null).stream()
            .filter(isMoveable())
            .filter(isNotProjectile())
            .filter(isNotTank())
            .filter(isGridElementAlive())
            .count();
   }

   private Moveable buildNewMoveable(Grid grid, int padding) {
      int gridElementRadius = 9;
      double yCordinate = min(getRandom(maxY) + padding, maxY - 3d * gridElementRadius);
      double xCordinate = min(getRandom(maxX) + padding, maxX - 3d * gridElementRadius);
      double angle2Rotate = -getRandom(90) + 15;
      Position gridElementPos = Positions.of(xCordinate, yCordinate)
            .rotate(angle2Rotate);

      LazyEndPoinMoveable lazyEndPoinMoveable = new LazyEndPoinMoveable();
      List<EndPosition> endPosList = getEndPosList(15, gridElementPos, gridElementRadius);
      AutoMoveableController autoMoveableController = AutoMoveableControllerBuilder.builder()
            .withDestructionHelper(DestructionHelperBuilder.builder()
                  .withDamage(3)
                  .withHealth(500)
                  .withSelfDestructiveDamage(moveableVelocity)
                  .withOnDestroyedCallbackHandler(() -> {
                     grid.remove(lazyEndPoinMoveable.getGridElement());
                     new DestructionAudio().playDefaultExplosion();
                  })
                  .build())
            .withBelligerentParty(belligerent)
            .withDimensionInfo(DimensionInfoBuilder.getDefaultDimensionInfo(gridElementRadius))
            .withMoveableController(MoveableControllerBuilder.builder()
                  .withEndPositions(endPosList)
                  .withStrategie(MovingStrategy.FORWARD_INCREMENTAL)
                  .withLazyMoveable(() -> lazyEndPoinMoveable.getGridElement())
                  .build())
            .withGrid(grid)
            .withEvasionStateMachine(EvasionStateMachineBuilder.builder()
                  .withGrid(grid)
                  .withDetector(DetectorBuilder.builder()
                        .build())
                  .withEvasionStateMachineConfig(DefaultConfig.INSTANCE.getDefaultEvasionStateMachineConfig())
                  .build())
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(gridElementRadius)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(moveableVelocity)
            .build();
      lazyEndPoinMoveable.setGridElement(autoMoveableController);
      return autoMoveableController;
   }

   private void buildAndAddSimpleGridElement(Grid grid, List<Renderer<? extends GridElement>> renderers, double padding) {
      int gridElementRadius = 8;
      double yCordinate = min(getRandom(maxY) + padding, maxY - 3d * gridElementRadius);
      double xCordinate = min(getRandom(maxX) + padding, maxX - 3d * gridElementRadius);
      double angle2Rotate = -getRandom(90) + 15;
      Position gridElementPos = Positions.of(xCordinate, yCordinate).rotate(angle2Rotate);
      ObstacleImpl obstacleImpl = ObstacleBuilder.builder()
            .withGrid(grid)
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

   private static List<EndPosition> getEndPosList(int amountOfEndPos, Position gridElementPos, double gridElementRadius) {
      double distance = gridElementRadius * (MathUtil.getRandom(8) + 4);
      Position position = gridElementPos.movePositionForward4Distance(distance);
      List<EndPosition> endPosList = new ArrayList<>(amountOfEndPos);
      endPosList.add(EndPositions.of(position, 5));
      for (int i = 0; i < amountOfEndPos; i++) {
         double degree = MathUtil.getRandom(360);
         position = position.rotate(degree)
               .movePositionForward4Distance(distance);
         endPosList.add(EndPositions.of(position, 10));
      }
      return endPosList;
   }

   private void buildAndAddMoveable(Grid grid, List<Renderer<? extends GridElement>> renderers, int padding) {
      Moveable moveable = buildNewMoveable(grid, padding);
      renderers.add(new GridElementPainter(moveable, GridElementColorUtil.getColor(moveable), 0, 0));
   }

   private static Predicate<? super GridElement> isMoveable() {
      return moveable -> moveable instanceof Moveable
            && !(moveable instanceof ProjectileGridElement);
   }

   private Predicate<? super GridElement> isObstacle() {
      return moveable -> (moveable instanceof Obstacle);
   }

   private static Predicate<? super GridElement> isNotProjectile() {
      return moveable -> !(moveable instanceof ProjectileGridElement);
   }

   private Predicate<? super GridElement> isNotTank() {
      return moveable -> !(moveable instanceof TankGridElement);
   }

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }
}
