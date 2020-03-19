/**
 * 
 */
package com.myownb3.piranha.grid.gridelement.shape;

import java.util.List;

import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Position;

/**
 * The {@link Shape} defines the Shape of any {@link GridElement}. A Shape is
 * usualy defined by one or more {@link Position}s and is therefore depending on
 * the {@link Position} of it's {@link GridElement}
 * 
 * @author Dominic
 *
 */
public interface Shape {

   /**
    * Returns the path of this shape described by a line a pen would follow to draw
    * the shape.
    * 
    * @return the path of this shape described by a line a pen would follow to draw
    *         the shape.
    */
   List<Position> getPath();

   /**
    * Returns the {@link Position} placed on the path which is equivalent to the
    * given Position. That means the position to return faces the same direction.
    * 
    * @param position
    *        the given {@link Position}
    * @return the {@link Position} placed on the path which is equivalent to the
    *         given Position
    */
   Position getPositionOnPathFor(Position position);

   /**
    * Transform this shape according the new {@link Position}
    * 
    * @param position
    */
   void transform(Position position);
}
