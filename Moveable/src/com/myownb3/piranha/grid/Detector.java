/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.GridElement;

/**
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
    boolean hasObjectDetected(GridElement gridElement, Position position);

    /**
     * Returns <code>true</code> if this {@link Detector} has already detected the
     * given Position and also has initiated an avoiding procedure
     * 
     * @param gridElement
     *            the {@link GridElement} to avoid
     * @param position
     *            the {@link Position}
     * @return <code>true</code> if this {@link Detector} has initiated an avoiding
     *         procedure otherwise <code>false</code>
     */
    boolean isAvoiding(GridElement gridElement, Position position);
}
