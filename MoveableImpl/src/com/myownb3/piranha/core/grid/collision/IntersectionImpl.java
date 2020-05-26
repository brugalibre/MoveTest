package com.myownb3.piranha.core.grid.collision;

import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * 
 * @author Dominic
 *
 */
public class IntersectionImpl implements Intersection {
   private PathSegment pathSegment;
   private Position collisionPosition;

   private IntersectionImpl(PathSegment pathSegment, Position collisionPosition) {
      this.pathSegment = pathSegment;
      this.collisionPosition = collisionPosition;
   }



   @Override
   public PathSegment getPathSegment() {
      return pathSegment;
   }

   @Override
   public Position getCollisionPosition() {
      return collisionPosition;
   }

   /**
    * Creates a new {@link IntersectionImpl}
    * 
    * @param pathSegment
    *        the main {@link PathSegment}
    * @param collisionPosition
    *        the {@link Position} which is collided
    * @return a new {@link IntersectionImpl}
    */
   public static Intersection of(PathSegment pathSegment, Position collisionPosition) {
      return new IntersectionImpl(pathSegment, collisionPosition);
   }
}
