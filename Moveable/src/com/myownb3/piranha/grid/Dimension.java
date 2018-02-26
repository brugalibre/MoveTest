/**
 * 
 */
package com.myownb3.piranha.grid;

/**
 * A {@link Dimension} defines a rectangle with given x,- y-coordinates as well
 * as a height and a width
 * 
 * @author Dominic
 *
 */
public interface Dimension {

    /**
     * @return the origin x-coorindate of this {@link Dimension}
     */
    int getX();

    /**
     * @return the origin y-coorindate of this {@link Dimension}
     */
    int getY();

    /**
     * @return the width of this {@link Dimension}
     */
    int getWidth();

    /**
     * @return the height of this {@link Dimension}
     */
    int getHeight();
}
