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
 * An {@link DetectableMoveableHelper} improves the default helper with
 * functions for evasion
 * 
 * @author Dominic
 *
 */
public class DetectableMoveableHelper extends MoveableHelper {

    protected Detector detector;

    public DetectableMoveableHelper(Detector detector) {
	super();
	this.detector = detector;
    }

    @Override
    public void checkPostConditions(Grid grid, Moveable moveable) {

	super.checkPostConditions(grid, moveable);
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