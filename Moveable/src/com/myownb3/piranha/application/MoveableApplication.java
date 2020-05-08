package com.myownb3.piranha.application;

import java.util.List;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;

/**
 * Defines any application which can run a {@link Moveable} on a {@link Grid}
 * 
 * @author Dominic
 *
 */
public interface MoveableApplication {

   /**
    * Runs this {@link MoveableApplication}
    * 
    * @return a List with {@link Position}s the {@link Moveable} has passed
    */
   List<Position> run();

   /**
    * @return all the {@link GridElement} which take part in this {@link MoveableApplication}
    */
   List<GridElement> getAllGridElements();

}
