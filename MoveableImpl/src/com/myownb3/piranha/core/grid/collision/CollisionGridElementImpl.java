package com.myownb3.piranha.core.grid.collision;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.grid.gridelement.GridElement;

public class CollisionGridElementImpl implements CollisionGridElement {

   private Intersection intersection;
   private GridElement gidElement;

   private CollisionGridElementImpl(Intersection intersection, GridElement gidElement) {
      this.intersection = requireNonNull(intersection);
      this.gidElement = requireNonNull(gidElement);
   }

   @Override
   public GridElement getGridElement() {
      return gidElement;
   }

   @Override
   public Intersection getIntersection() {
      return intersection;
   }

   public static CollisionGridElementImpl of(Intersection intersection, GridElement gidElement) {
      return new CollisionGridElementImpl(intersection, gidElement);
   }
}
