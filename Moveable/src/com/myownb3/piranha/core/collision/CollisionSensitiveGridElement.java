package com.myownb3.piranha.core.collision;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * A {@link CollisionSensitiveGridElement} defines a {@link GridElement} which can react as soon as it collides
 * 
 * @author Dominic
 *
 */
public interface CollisionSensitiveGridElement extends GridElement {

   /**
    * Is called whenever this {@link GridElement} is collided
    */
   void onCollision();
}
