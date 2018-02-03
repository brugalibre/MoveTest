/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Grid;

/**
 * An {@link AvoidableMoveable} is able to avoid certain {@link GridElement}s on
 * a given {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface AvoidableMoveable extends Moveable {

    // /**
    // * If this {@link Moveable} is currently avoiding any {@link GridElement} then
    // * this call will continue the avoiding protocol
    // */
    // public void continueAvoiding();
}
