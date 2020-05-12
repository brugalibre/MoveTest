package com.myownb3.piranha.core.grid.collision;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link CollisionGridElement} defines a {@link GridElement} which is collided. Therefore a {@link CollisionGridElement} contains
 * this very collided {@link GridElement} as well as the {@link Intersection} which precise the collision
 * 
 * @author Dominic
 *
 */
public interface CollisionGridElement {

   /**
    * @return the {@link GridElement}
    */
   GridElement getGridElement();


   /**
    * @return the {@link Intersection} of the collision
    */
   Intersection getIntersection();

   @Override
   int hashCode();

   @Override
   boolean equals(Object obj);
}
