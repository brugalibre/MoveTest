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
    public SimpleMoveable() {
	this(new Position(0, 0));
    }

    /**
     * @param position
     */
    public SimpleMoveable(Position position) {
	super();
	this.position = position;
    }
}
