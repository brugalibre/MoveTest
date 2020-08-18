package com.myownb3.piranha.launch.weapon;

import java.util.List;

import com.myownb3.piranha.application.Application;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.MoveableObstacleImpl;
import com.myownb3.piranha.core.moveables.Moveable;

public class RandomMoveableApp implements Application {

   private Moveable moveable;
   private List<GridElement> gridelements;

   public RandomMoveableApp(Moveable moveable, List<GridElement> gridelements) {
      this.moveable = moveable;
      this.gridelements = gridelements;
   }

   @Override
   public void run() {
      moveable.moveForward();
      moveGridElementsForward(gridelements);
   }

   @Override
   public void prepare() {
      // TODO Auto-generated method stub

   }

   private static void moveGridElementsForward(List<GridElement> gridelements) {
      gridelements.stream()
            .filter(MoveableObstacleImpl.class::isInstance)
            .map(MoveableObstacleImpl.class::cast)
            .forEach(obstacle -> obstacle.moveForward());
   }

}
