/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement.obstacle;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.destruction.Destructible;
import com.myownb3.piranha.core.collision.CollisionSensitive;
import com.myownb3.piranha.core.grid.gridelement.GridElement;

/**
 * The {@link Obstacle} defines a {@link GridElement} which is 'avoidable'
 * 
 * @author Dominic
 *
 */
public interface Obstacle extends GridElement, CollisionSensitive, Destructible, Belligerent {
   // no - op
}
