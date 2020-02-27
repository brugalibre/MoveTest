/**
 * 
 */
package com.myownb3.piranha.grid;

import static java.util.Objects.isNull;

import java.util.Objects;

import com.myownb3.piranha.grid.gridelement.Position;

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
     * 
     * @param maxY
     * @param maxX
     */
    private SwappingGrid(int maxY, int maxX) {
	super(maxX, maxY, 0, 0);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     */
    private SwappingGrid(int maxY, int maxX, int minX, int minY) {
	super(maxX, maxY, minX, minY);
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
     *                10 ___________ |__|__|__|__| |__|__|__|__| |__|__|__|__|
     *                |__|__|__|__| 5|__|__|__|__| 5 10
     * 
     * @formatter:on
     */

    /**
     * The {@link SwappingGridBuilder} helps to build a {@link SwappingGrid}
     * 
     * @author Dominic
     *
     */
    public static class SwappingGridBuilder extends AbstractGridBuilder<SwappingGrid> {

	public static SwappingGridBuilder builder() {
	    return new SwappingGridBuilder()//
		    .withMaxX(10)//
		    .withMaxY(10);
	}

	public static SwappingGridBuilder builder(int maxX, int maxY) {
	    return new SwappingGridBuilder()//
		    .withMaxX(maxX)//
		    .withMaxY(maxY);
	}

	/**
	 * Creates a new {@link SwappingGrid}
	 * 
	 * @return a new {@link SwappingGrid}
	 */
	@Override
	public SwappingGrid build() {
	    Objects.requireNonNull(maxX, "We need a max x value!");
	    Objects.requireNonNull(maxY, "We need a max y value!");
	    SwappingGrid swappingGrid;
	    if (isNull(minX) || isNull(minY)) {
		swappingGrid = new SwappingGrid(maxY, maxX);
	    } else {
		swappingGrid = new SwappingGrid(maxY, maxX, minX, minY);
	    }
	    setDetector(swappingGrid);
	    return swappingGrid;
	}
    }
}
