package com.myownb3.piranha.core.collision.bounce.impl;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;

/**
 * The {@link BouncableLookupTable} defines which {@link GridElement}s can bounced. If a {@link GridElement} can be bounced
 * depends on the {@link GridElement} with which it colliedes
 * 
 * @author Dominic
 *
 */
public class BouncableLookupTable {

   private BouncableLookupTable() {
      // private
   }

   /**
    * Evaluates if the given moved {@link GridElement}, which is collided with the other {@link GridElement} can be bounced
    * 
    * @param movedGridElement
    *        the moved {@link GridElement}
    * @param gridElement
    *        the {@link GridElement} with which the moved one was collided
    * @return <code>true</code> if the moved {@link GridElement} can be bounced or <code>false</code> if not
    * 
    */
   public static boolean isBouncable(GridElement movedGridElement, GridElement gridElement) {
      if (isProjectile(movedGridElement)) {
         return isProjectileBouncableForCollisionWith(gridElement);
      } else {
         return !isProjectile(gridElement);
      }
   }

   private static boolean isProjectileBouncableForCollisionWith(GridElement gridElement) {
      return false; // right now, a projectile can not bounce on any GridElement
   }

   private static boolean isProjectile(GridElement movedGridElement) {
      return movedGridElement instanceof Projectile;
   }
}
