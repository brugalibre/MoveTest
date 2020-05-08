/**
 * 
 */
package com.myownb3.piranha.core.grid.gridelement;

/**
 * The {@link Obstacle} defines a {@link GridElement} which is 'avoidable'
 * 
 * @author Dominic
 *
 */
public interface Obstacle extends GridElement {
   @Override
   default boolean isAvoidable() {
      return true;
   }
}
