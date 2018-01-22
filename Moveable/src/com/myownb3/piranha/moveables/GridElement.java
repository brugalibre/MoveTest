/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.Position;

/**
 * A {@link GridElement} is any element which can be placed on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface GridElement {

    /**
     * @return the current {@link Position} of this {@link GridElement}
     */
    Position getPosition();
}