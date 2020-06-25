package com.myownb3.piranha.core.grid.gridelement.constants;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.weapon.tank.Tank;
import com.myownb3.piranha.core.weapon.turret.Turret;

/**
 * Defines some constants for a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public class GridElementConst {

   /** Defines the gap between the ground and a {@link GridElement} */
   public static final double DEFAULT_HEIGHT_FROM_BOTTOM = 50.0;

   /** Defines the height of a {@link Tank}, measured from it's bottom line */
   public static final double DEFAULT_TANK_HEIGHT_FROM_BOTTOM = 25.0;

   /** Defines the height of a {@link Turret} for a {@link Tank}, measured from it's bottom line */
   public static final double DEFAULT_TANK_TURRET_HEIGHT_FROM_BOTTOM = 20.0;

   private GridElementConst() {
      // private
   }
}
