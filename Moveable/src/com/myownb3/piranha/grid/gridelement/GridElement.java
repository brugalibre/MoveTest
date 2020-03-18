/**
 * 
 */
package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

/**
 * A {@link GridElement} is a most simple element which can be placed on a
 * {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface GridElement {

    /**
     * @return the current {@link Position} of this {@link GridElement}
     */
    Position getPosition();

    /**
     * @return the Grid of this {@link GridElement}
     */
    Grid getGrid();
    
    /**
     * Returns the Shape of this {@link GridElement}
     * @return the Shape of this {@link GridElement}
     */
    Shape getShape();
}