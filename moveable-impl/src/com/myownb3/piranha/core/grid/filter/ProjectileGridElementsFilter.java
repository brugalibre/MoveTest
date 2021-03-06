package com.myownb3.piranha.core.grid.filter;

import java.util.function.Predicate;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.ProjectileGridElement;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

public class ProjectileGridElementsFilter implements Predicate<GridElement> {


   /**
    * @param avoidableGridElement
    *        the {@link GridElement} under test
    * @return <code>true</code> if the detected {@link GridElement} is a {@link ProjectileGridElement} or <code>false</code> if not
    */
   @Override
   public boolean test(GridElement avoidableGridElement) {
      return avoidableGridElement instanceof ProjectileGridElement;
   }
}
