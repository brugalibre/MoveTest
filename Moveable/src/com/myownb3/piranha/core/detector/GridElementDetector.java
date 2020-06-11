package com.myownb3.piranha.core.detector;

import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * The {@link GridElementDetector} is used to detect {@link GridElement}s on a {@link Grid} using {@link Detector}s
 * 
 * @author Dominic
 *
 */
public interface GridElementDetector {

   /**
    * Verifies if there is any {@link GridElement} on the {@link Grid} for which
    * there is currently an evasion with the given {@link Moveable}
    * 
    * @param gridElement
    *        the {@link GridElement} for which the evasion is checked
    * 
    * @return <code>true</code> if there is any evasion or <code>false</code> if
    *         not
    */
   boolean check4Evasion(GridElement gridElement);

   /**
    * Checks the surrounding of the given {@link Moveable} the {@link Grid}
    * 
    * @param gridElement
    *        the given {@link GridElement}
    */
   void checkSurrounding(GridElement gridElement);

   /**
    * Evaluates all {@link GridElement} which are currently detected by this {@link GridElementDetector} on the {@link Grid}
    * 
    * @param detectableGridElement
    *        the {@link GridElement} which wants to know all detected {@link GridElement}
    * 
    * @return all {@link GridElement} which are currently detected
    */
   List<GridElement> getDetectedGridElement(GridElement detectableGridElement);
}
