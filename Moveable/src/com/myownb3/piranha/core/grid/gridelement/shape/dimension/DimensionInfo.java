package com.myownb3.piranha.core.grid.gridelement.shape.dimension;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * The {@link DimensionInfo} contains informations about the physical dimension and location of a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public interface DimensionInfo {

   /**
    * @return the radius within this {@link Shape} could be placed
    */
   double getDimensionRadius();

   /**
    * 
    * @return the height of a {@link GridElement} measured from it's bottom
    */
   double getHeightFromBottom();

   /**
    * Return <code>true</code> if this {@link GridElement} is within the reach of the other regarding it's distance to the ground as well
    * it's height from ground. Returns <code>false</code> if not
    * 
    * @param ourDistanceToGround
    *        the distance to the ground of the {@link GridElement} of this {@link DimensionInfo}
    * @param otherGridElemDistanceToGround
    *        the distance to the ground of the {@link GridElement} of the other {@link GridElement}
    * 
    * @return <code>true</code> if this {@link GridElement} is within the reach of the other or <code>false</code> if not
    */
   boolean isWithinHeight(double ourDistanceToGround, double otherGridElemDistanceToGround);
}
