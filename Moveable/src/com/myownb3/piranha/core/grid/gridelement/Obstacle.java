/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.destruction.Destructible;

/**
 * The {@link Obstacle} defines a {@link GridElement} which is 'avoidable'
 * 
 * @author Dominic
 *
 */
public interface Obstacle extends GridElement, CollisionSensitive, Destructible {
   @Override
   default boolean isAvoidable() {
      return true;
   }
}
