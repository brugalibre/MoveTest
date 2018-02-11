/**
 * 
 */
package com.myownb3.piranha.moveables;

import com.myownb3.piranha.grid.Position;

/**
 * @author Dominic
 *
 */
@FunctionalInterface
public interface Updater {
    public void update(Moveable moveable, Position pos);
}