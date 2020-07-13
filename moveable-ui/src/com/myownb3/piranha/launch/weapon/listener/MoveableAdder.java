package com.myownb3.piranha.launch.weapon.listener;

import static com.myownb3.piranha.util.MathUtil.getRandom;
import static java.lang.Math.min;

import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.core.battle.belligerent.galacticempire.tfighter.shape.TIEFighterShapeImpl.TIEFighterShapeBuilder;
import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl;
import com.myownb3.piranha.core.grid.gridelement.obstacle.ObstacleImpl.ObstacleBuilder;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.circle.CircleImpl.CircleBuilder;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.ui.render.Renderer;
import com.myownb3.piranha.ui.render.impl.GridElementPainter;
import com.myownb3.piranha.ui.render.util.GridElementColorUtil;

public class MoveableAdder {

   private int amountOfNonMoveables = 5;
   private int amountOfMoveables = 5;
   private int moveableVelocity = 20;
   private int counter = 50;
   private double maxX;
   private double maxY;

   public MoveableAdder(int maxX, int maxY) {
      this.maxX = maxX;
      this.maxY = maxY;
   }

   public MoveableAdder(int maxX, int maxY, int moveableVelocity, int counter) {
      this(maxX, maxY);
      this.moveableVelocity = moveableVelocity;
      this.counter = counter;
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
      return MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withHealth(500)
            .withShape(TIEFighterShapeBuilder.builder()
                  .withBallCockpit(CircleBuilder.builder()
                        .withRadius(gridElementRadius)
                        .withAmountOfPoints(20)
                        .withCenter(gridElementPos)
                        .build())
                  .build())
            .withVelocity(moveableVelocity)
            .build();
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

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }
}
