/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.obstacle;

import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.destruction.Destructible;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link Obstacle} defines a {@link GridElement} which is 'avoidable'
 * 
 * @author Dominic
 *
 */
public interface Obstacle extends GridElement, CollisionSensitive, Destructible {
   // no - op
}
