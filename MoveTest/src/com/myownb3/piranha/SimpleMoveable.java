/**
 * 
 */
package com.myownb3.piranha;

/**
 * @author Dominic
 *
 */
public class SimpleMoveable extends AbstractMovable {

    /**
     * @param position
     */
    public SimpleMoveable(Grid grid) {
	this(grid, new Position(0, 0));
    }

    /**
     * @param position
     */
    public SimpleMoveable() {
	this(new Grid(), new Position(0, 0));
    }

    /**
     * @param position
     */
    public SimpleMoveable(Grid grid, Position position) {
	super();
	this.position = position;
	this.grid = grid;
    }
}
