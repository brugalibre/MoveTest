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
     * Transform this shape according the new {@link Position} 
     * 
     * @param position
     */
    void transform(Position position);
}
