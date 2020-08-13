package com.myownb3.piranha.core.collision.bounce.impl;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

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
      if (isTank(movedGridElement)) {
         return false;
      } else {
         return !isOtherGridElementProjectile(gridElement);
      }
   }

   /*
    * the moved GridElement which was hit by a projectile must not bounce (since it was hit!)
    */
   private static boolean isOtherGridElementProjectile(GridElement gridElement) {
      return gridElement instanceof Projectile;
   }

   private static boolean isTank(GridElement movedGridElement) {
      return movedGridElement instanceof Tank;
   }
}
