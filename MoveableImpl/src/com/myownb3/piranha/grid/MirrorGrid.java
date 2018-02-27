/**
 * 
 */
package com.myownb3.piranha.grid;

import com.myownb3.piranha.moveables.Moveable;

/**
 * The {@link MirrorGrid} is a {@link Grid} which mirrors any {@link Moveable}
 * as soon as this moveable is crossing the grids borders
 * 
 * @author Dominic
 *
 */
public class MirrorGrid extends DefaultGrid {

    /**
     * 
     * @param maxY
     * @param maxX
     */
    public MirrorGrid(int maxY, int maxX) {
	super(maxY, maxX, 0, 0);
    }

    /**
     * 
     * @param maxY
     * @param maxX
     * @param minX
     * @param minY
     */
    public MirrorGrid(int maxY, int maxX, int minX, int minY) {
	super(maxY, maxX, minX, minY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.myownb3.piranha.grid.DefaultGrid#moveForward(com.myownb3.piranha.grid.
     * Position)
     */
    @Override
    public Position moveForward(Position position) {
	Position movedPos = super.moveForward(position);

	if (movedPos.getX() == minX) {
	    movedPos.rotate(180 - 2 * movedPos.getDirection().getAngle());
	} else if (movedPos.getX() == maxX) {

	    movedPos.rotate(180 - 2 * movedPos.getDirection().getAngle());
	}

	return movedPos;
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
	if (newY >= maxY) {
	    return maxY;
	} else if (newY <= minY) {
	    return minY;
	}
	// if (newY >= maxY) {
	// return newY - maxY;
	// } else if (newY <= minY) {
	// return position.getY();
	// }
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

	if (newX >= maxX) {
	    return maxX;
	} else if (newX <= minX) {
	    return minX;
	}
	return newX;

	// if (newX >= maxX) {
	// // X-Wert negieren (+ -> -)
	// return maxX - (newX - maxX);
	// } else if (newX <= minX) {
	// // X-Wert negieren (- -> +)
	// return maxX - (newX - maxX);
	// }
    }

}
