/**
 * 
 */
package com.myownb3.piranha.moveables.helper;

import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.GridElement;
import com.myownb3.piranha.moveables.Moveable;
import com.myownb3.piranha.moveables.detector.Detector;

/**
 * An {@link DetectableMoveableHelper} implements the {@link MoveablePostActionHandler}
 * with basic functions of detecting other {@link GridElement}. This includes
 * the ability to identify weather or not a certain {@link GridElement} is
 * evaded
 * 
 * @author Dominic
 *
 */
public class DetectableMoveableHelper implements MoveablePostActionHandler {

    protected Detector detector;

    public DetectableMoveableHelper(Detector detector) {
	super();
	this.detector = detector;
    }

    @Override
    public void handlePostConditions(Grid grid, Moveable moveable) {

	checkSurrounding(grid, moveable);
    }

    protected boolean check4Evasion(Grid grid, GridElement moveable) {
	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	return gridElements.stream()//
		.anyMatch(gridElement -> detector.isEvasion(gridElement));
    }

    protected void checkSurrounding(Grid grid, Moveable moveable) {
	List<GridElement> gridElements = grid.getSurroundingGridElements(moveable);
	gridElements.stream()//
		.forEach(gridElement -> detector.detectObject(gridElement, moveable.getPosition()));
    }
}