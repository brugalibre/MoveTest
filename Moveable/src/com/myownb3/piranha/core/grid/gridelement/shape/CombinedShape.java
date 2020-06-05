package com.myownb3.piranha.core.grid.gridelement.shape;

import java.util.List;

import com.myownb3.piranha.core.grid.gridelement.shape.circle.Circle;
import com.myownb3.piranha.core.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * A {@link CombinedShape} is a shape which consist of more than one 'simple' {@link Shape} like the {@link Circle} or {@link Rectangle}
 * 
 * @author Dominic
 *
 */
public interface CombinedShape extends Shape {

   /**
    * @return the {@link Position}s for collision detecting
    */
   List<Position> getPath4CollisionDetection();
}
