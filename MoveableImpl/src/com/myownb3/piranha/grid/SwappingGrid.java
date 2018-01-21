/**
 * 
 */
package com.myownb3.piranha.grid;

/**
 * 
 * A {@link SwappingGrid} makes sure, that {@link PositionImpl} are not
 * misplaced. That means, if any Position is placed out of bounds the Grid
 * places the Position on its reverse location
 * 
 * @author Dominic
 *
 */
public class SwappingGrid extends DefaultGrid {

    /**
     * Creates a default Grid which has a size of 10 to 10
     */
    public SwappingGrid() {
	super(10, 10);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public SwappingGrid(int maxY, int maxX) {
	super(maxY, maxX, 0, 0);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public SwappingGrid(int maxY, int maxX, int minX, int minY) {
	super(maxY, maxX, minX, minY);
    }

    /**
     * Evaluates the new y value. Additionally it checks weather or not the new
     * value is in bounds. If not, the value is swapped
     * 
     * @param newY
     * @return the new y-Value within grid bounds
     */
    @Override
    protected double getNewYValue(Position position, double forwardY) {
	double newY = super.getNewYValue(position, forwardY);
	if (newY > maxY) {
	    newY = (newY - maxY) + minY;
	} else if (newY < minY) {
	    newY = maxY - (minY - newY);
	}
	return newY;
    }

    /**
     * Evaluates the new x value. Additionally it checks weather or not the new
     * value is in bounds. If not, the value is swapped
     * 
     * @param newX
     * @return the new x-Value within grid bounds
     */
    @Override
    protected double getNewXValue(Position position, double forwardX) {
	double newX = super.getNewXValue(position, forwardX);
	if (newX > maxX) {
	    newX = (newX - maxX) + minX;
	} else if (newX < minX) {
	    newX = maxX - (minX - newX);
	}
	return newX;
    }

    /**
     * @formatter:off
     * 
     *10 ___________ 
     * 	|__|__|__|__|
     * 	|__|__|__|__|
     * 	|__|__|__|__|
     * 	|__|__|__|__|
     * 5|__|__|__|__|
     *  5	     10 
     * 
     * @formatter:on
     */
}
