/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * An {@link Avoidable} defines any {@link GridElement} which can be detected by
 * any {@link Detector} and therefore avoided by a {@link Moveable}
 * 
 * @author Dominic
 *
 */
public interface Avoidable extends GridElement {
   //no-op
}
