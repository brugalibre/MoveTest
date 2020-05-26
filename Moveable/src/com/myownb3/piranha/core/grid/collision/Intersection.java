package com.myownb3.piranha.core.grid.collision;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * 
 * An {@link Intersection} descripes the intersection of a collision
 * 
 * @author Dominic
 *
 */
public interface Intersection {

   /**
    * @return the {@link PathSegment} of the {@link GridElement} which has detected the collision
    */
   PathSegment getPathSegment();

   /**
    * 
    * @return the {@link Position} which is collided with the {@link PathSegment}
    */
   Position getCollisionPosition();

}
