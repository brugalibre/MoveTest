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
    * @return the distance a {@link GridElement} has from itself to the ground
    */
   double getDistanceToGround();

   /**
    * 
    * @return the height of a {@link GridElement} measured from it's bottom
    */
   double getHeightFromBottom();

   /**
    * Return <code>true</code> if this {@link DimensionInfo} is within the reach of the other regarding it's distance to the ground as well
    * it's height from ground. Returns <code>false</code> if not
    * 
    * @param otherDimensionInfo
    *        the other DimensionInfo
    * @return <code>true</code> if this {@link DimensionInfo} is within the reach of the other or <code>false</code> if not
    */
   boolean isWithinHeight(DimensionInfo otherDimensionInfo);

}
