/**
 * 
 */
package com.myownb3.piranha.grid;

import java.util.List;

import com.myownb3.piranha.grid.gridelement.Avoidable;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.position.Position;
import com.myownb3.piranha.moveables.Moveable;

/**
 * The {@link Grid} defines the place where a {@link Moveable} can be moved and
 * {@link Position}s and {@link GridElement}s can be placed.
 * 
 * @author Dominic
 *
 */
public interface Grid {

   /**
    * This method should be called as soon as all {@link GridElement}
    * are placed on this {@link Grid} so that this Grid can prepare itself
    */
   void prepare();

   /**
    * Moves the given {@link GridElement} backward by one unit
    * 
    * @param gridElement
    *        the {@link GridElement} to move
    * @return a new instance of the moved Position
    */
   Position moveBackward(GridElement gridElement);

   /**
    * Moves the given {@link GridElement} forward by one unit
    * 
    * @param gridElement
    *        the {@link Position} to move
    * @return a new instance of the moved Position
    */
   Position moveForward(GridElement gridElement);

   /**
    * @param gridElement
    *        the element to verify
    * @return <code>true</code> if this {@link Grid} contains the given
    *         {@link GridElement} or <code>false</code> if not
    */
   boolean containsElement(GridElement gridElement);

   /**
    * Adds the given {@link GridElement} to this {@link Grid}
    * 
    * @param abstractGridElement
    */
   void addElement(GridElement gridElement);

   /**
    * Returns all {@link Avoidable}s but the given {@link GridElement} which are
    * currently placed on this {@link Grid}
    * 
    * @param gridElement
    * @return all {@link Avoidable}s but the given {@link GridElement} which are
    *         currently placed on this {@link Grid}
    */
   List<Avoidable> getAllAvoidables(GridElement gridElement);

   /**
    * Returns all {@link Avoidable}s but the given {@link GridElement} which are
    * currently placed on this {@link Grid} and which are within the given distance to the {@link GridElement}
    * <b>Note:</b> For the calculation of the distance is the {@link Position} {@link GridElement#getFurthermostFrontPosition()} used
    * 
    * @param gridElement
    * @param distance
    *        the distance
    * @return all {@link Avoidable}s but the given {@link GridElement} which are
    *         currently placed on this {@link Grid}
    */
   List<Avoidable> getAllAvoidablesWithinDistance(GridElement gridElement, int distance);

   /**
    * Returns all {@link GridElement}s but the given {@link GridElement} which are
    * currently placed on this {@link Grid}
    * 
    * @param gridElement
    * @return all {@link GridElement}s but the given {@link GridElement} which are
    *         currently placed on this {@link Grid}
    */
   List<GridElement> getAllGridElements(GridElement gridElement);

   /**
    * 
    * @returns a {@link Dimension} describing the dimension of this {@link Grid}
    */
   Dimension getDimension();

}
