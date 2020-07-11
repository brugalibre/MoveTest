package com.myownb3.piranha.launch.weapon.listener;

import java.util.List;
import java.util.function.Predicate;

import com.myownb3.piranha.core.destruction.DestructionHelper;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl.MoveableObstacleBuilder;
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
import com.myownb3.piranha.util.MathUtil;

public class MoveableAdder {

   private int amountOfMoveables = 5;
   private int moveableVelocity = 20;
   private int counter = 50;

   public MoveableAdder() {
      // default constructor
   }

   public MoveableAdder(int moveableVelocity, int counter) {
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
         if (simpleGridElementCounter <= amountOfMoveables) {
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
      double yCordinate = MathUtil.getRandom(450) + padding;
      double angle2Rotate = -MathUtil.getRandom(90) + 15;
      Position gridElementPos = Positions.of(200, yCordinate).rotate(angle2Rotate);
      int gridElementRadius = 10;
      return MoveableObstacleBuilder.builder()
            .withGrid(grid)
            .withHealth(400)
            .withShape(CircleBuilder.builder()
                  .withRadius(gridElementRadius)
                  .withAmountOfPoints(20)
                  .withCenter(gridElementPos)
                  .build())
            .withVelocity(moveableVelocity)
            .build();
   }

   private static void buildAndAddSimpleGridElement(Grid grid, List<Renderer<? extends GridElement>> renderers, double padding) {
      double yCordinate = MathUtil.getRandom(450) + padding;
      double angle2Rotate = -MathUtil.getRandom(90) + 15;
      Position gridElementPos = Positions.of(300, yCordinate).rotate(angle2Rotate);
      int gridElementRadius = 10;
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

   private static Predicate<? super GridElement> isNotProjectile() {
      return moveable -> !(moveable instanceof ProjectileGridElement);
   }

   private static Predicate<? super GridElement> isGridElementAlive() {
      return DestructionHelper::isNotDestroyed;
   }
}
