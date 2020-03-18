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
     * Return the current {@link Position} of this {@link GridElement}
     * @return the current {@link Position} of this {@link GridElement}
     */
    Position getPosition();

    /**
     * Returns the {@link Position} of this {@link GridElement} which faces the same
     * direction than it's center {@link Position} but is placed on it's
     * {@link Shape}
     * 
     * @return the {@link Position} of this {@link GridElement}
     */
    Position getFurthermostFrontPosition();

    /**
     * Returns the {@link Position} of this {@link GridElement} which faces the oposit
     * direction than it's center {@link Position} but is placed on it's
     * {@link Shape}
     * 
     * @return the {@link Position} of this {@link GridElement}
     */
    Position getFurthermostBackPosition();

    /**
     * @return the Grid of this {@link GridElement}
     */
    Grid getGrid();
    
    /**
     * Returns the Shape of this {@link GridElement}
     * @return the Shape of this {@link GridElement}
     */
    Shape getShape();

    /**
     * Returns <code>true</code> if the given {@link Avoidable} is detected by this
     * {@link GridElement}
     * 
     * @param avoidable
     *            the {@link Avoidable} to check
     * @return <code>true</code> if the given {@link Avoidable} is detected by this
     *         {@link GridElement}. Otherwise returns <code>false</code>
     */
    boolean hasDetected(Avoidable avoidable);
}