package com.myownb3.piranha.core.weapon.turret.turretscanner;

import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link GridElementEvaluator} is used to evaluate all the {@link GridElement} within a specific distance
 * 
 * @author Dominic
 *
 */
@FunctionalInterface
public interface GridElementEvaluator {

   /**
    * Evaluates all {@link GridElement} which are placed within the given distance on a {@link Grid}
    * 
    * @param position
    *        the {@link Position} from which the {@link GridElement} are evaluated
    * @param distance
    *        the distance
    * @return all the {@link GridElement}s
    */
   List<GridElement> evaluateGridElementsWithinDistance(Position position, int distance);
}
