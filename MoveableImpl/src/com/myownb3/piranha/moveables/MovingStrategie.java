/**
 * 
 */
package com.myownb3.piranha.moveables;

/**
 * @author Dominic
 *
 */
public enum MovingStrategie {

    /**
     * Indicates that the {@link MoveableController} leads a {@link Moveable} by
     * moving it 'forward'
     */
    FORWARD,

    /**
     * Indicates that the {@link MoveableController} leads a {@link Moveable} by
     * moving it 'forward'. If there are any turns to take, the turn is taken
     * slowly, distributed to each move forward
     */
    FORWARD_CURVED,

    /**
     * Indicates that the {@link MoveableController} leads a {@link Moveable} by
     * moving it 'backward'
     */
    BACKWARD;
}
