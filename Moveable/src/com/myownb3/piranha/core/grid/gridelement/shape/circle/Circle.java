/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.shape.circle;

import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

/**
 * The {@link Circle} defines the Shape of any {@link GridElement}. A Shape is
 * usualy defined by one or more {@link Position}s and is therefore depending on
 * the {@link Position} of it's {@link GridElement}
 * 
 * @author Dominic
 *
 */
public interface Circle extends Shape {

   /**
    * Returns the center of this Circle
    * 
    * @return the center of this Circle
    */
   Position getCenter();

   /**
    * Returns the radius of this circle
    * 
    * @return the radius of this circle
    */
   int getRadius();
}
