package com.myownb3.piranha.core.collision;

import java.util.List;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * A {@link CollisionSensitive} defines a object which can act on collision with other {@link GridElement}ss
 * 
 * @author Dominic
 *
 */
public interface CollisionSensitive {

   /**
    * Is called whenever this {@link GridElement} is collided
    * 
    * @param gridElements
    *        the {@link GridElement}s with which this {@link CollisionSensitive} was collided.
    */
   void onCollision(List<GridElement> gridElements);
}
