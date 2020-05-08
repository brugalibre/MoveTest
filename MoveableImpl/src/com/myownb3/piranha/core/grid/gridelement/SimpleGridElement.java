package com.myownb3.piranha.core.grid.gridelement;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link SimpleGridElement} provides the most basic functions of a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public class SimpleGridElement extends AbstractGridElement {

   public SimpleGridElement(Grid grid, Position position) {
      super(grid, position);
   }

   public SimpleGridElement(Grid grid, Position pos, Shape shape) {
      super(grid, pos, shape);
   }
}
