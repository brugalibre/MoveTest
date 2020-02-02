/**
 * 
 */
package com.myownb3.piranha.detector;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.grid.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * A {@link Detector} is used to detect other {@link GridElement} on a
 * {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface Detector {

    /**
     * Evaluates if the given {@link GridElement} has been detected by this Detector
     * 
     * @param gridElement
     *            the {@link GridElement} to detect
     * @param position
     *            the current Position from which the {@link Detector} trys to
     *            detect
     * @return <code>true</code> if the object was detected, <code>false</code> if
     *         not
     */
    void detectObject(GridElement gridElement, Position position);

    /**
     * 
     * @return <code>true</code> if this {@link Moveable} is currently evasion the
     *         given {@link GridElement}
     */
    boolean isEvasion(GridElement gridElement);

    /**
     * Returns <code>true</code> if the given {@link GridElement} is currently
     * detected or <code>false</code> if not
     * 
     * @param gridElement
     * @return <code>true</code> if the given {@link GridElement} is currently
     *         detected or <code>false</code> if not
     */
    boolean hasObjectDetected(GridElement gridElement);

    /**
     * Returns the angle increment for which a {@link Moveable} can make a turn in
     * order to avoid a {@link GridElement} which is is on a collision path. This
     * method will return <code>0</code> if this {@link Detector} is currently not
     * evasion any {@link GridElement}
     * 
     * @param position
     *            the origin Position
     * @returns the evasion angle increment
     */
    double getEvasionAngleRelative2(Position position);
}
