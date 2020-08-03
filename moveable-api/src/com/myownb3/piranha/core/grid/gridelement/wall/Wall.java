package com.myownb3.piranha.core.grid.gridelement.wall;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.obstacle.Obstacle;

/**
 * The {@link Wall} defines a wall which is also a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public interface Wall extends Obstacle {

   @Override
   default boolean isAvoidable() {
      return true;
   }
}
