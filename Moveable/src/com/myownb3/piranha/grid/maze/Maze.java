package com.myownb3.piranha.grid.maze;


import java.util.List;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.GridElement;
import com.myownb3.piranha.grid.gridelement.Obstacle;
import com.myownb3.piranha.grid.gridelement.position.EndPosition;
import com.myownb3.piranha.grid.gridelement.shape.rectangle.Rectangle;
import com.myownb3.piranha.grid.maze.corridor.CorridorSegment;

/**
 * A {@link Maze} contains multiple {@link GridElement} such as {@link Rectangle} defining the maze but also other {@link Obstacle}s
 * which are placed within the maze
 * 
 * @author Dominic
 *
 */
public interface Maze {

   /**
    * Return all {@link GridElement} of this Maze. This includes also the {@link GridElement} of each {@link CorridorSegment} as well as
    * other {@link GridElement}
    * 
    * @return all {@link GridElement} of this Maze.
    */
   List<GridElement> getAllMazeGridElements();

   /**
    * 
    * @return all {@link CorridorSegment}s of this Maze
    */
   List<CorridorSegment> getMazeCorridorSegments();

   /**
    * @return an optional {@link List} of {@link EndPosition} which are placed on this {@link Maze}
    */
   List<EndPosition> getEndPositions();

   /**
    * 
    * @return the {@link Grid} of this {@link Maze}
    */
   Grid getGrid();
}
