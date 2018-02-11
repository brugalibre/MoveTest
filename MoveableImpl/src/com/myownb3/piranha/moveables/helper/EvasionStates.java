/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.moveables.Moveable;

/**
 * Describes the different states when a {@link Moveable} is envasion any
 * {@link GridElement}
 * 
 * @author Dominic
 *
 */
public enum EvasionStates {

    NONE,
    /**
     * Describes the default or initial state of any {@link Moveable} which are
     * currently not evasion any {@link GridElement}
     */
    DEFAULT,

    /**
     * Describes the effectively state of evasion the detected {@link GridElement}
     */
    ENVASION,

    /**
     * Describes the state after a {@link GridElement} was evaded and the
     * {@link Moveable} is correcting it's direction bevore the {@link GridElement}
     * can be safely passed
     */
    POST_ENVASION,

    /**
     * Describes the state after a {@link Moveable} has evaded a {@link GridElement}
     */
    PASSING,

    /**
     * Describes the state after a {@link Moveable} has passed a {@link GridElement}
     * and is now returning to its former direction
     */
    RETURNING;
}
