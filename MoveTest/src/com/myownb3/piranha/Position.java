/**
 * 
 */
package com.myownb3.piranha;

/**
 * @author Dominic
 *
 */
public class Position {

    private int y;
    private int x;
    private String direction;

    /**
     * @param x
     * @param y
     */
    public Position(int x, int y) {
	this.x = x;
	this.y = y;
	this.direction = "N";
    }

    /**
     * @return the direction
     */
    public final String getDirection() {
	return this.direction;
    }

    /**
     * @return the y
     */
    public int getY() {
	return this.y;
    }

    /**
     * @return the x
     */
    public int getX() {
	return this.x;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + this.x;
	result = prime * result + this.y;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Position other = (Position) obj;
	if (this.x != other.x)
	    return false;
	if (this.y != other.y)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "X-Axis: '" + x + "', Y-Axis: '" + y + "'";
    }
}
