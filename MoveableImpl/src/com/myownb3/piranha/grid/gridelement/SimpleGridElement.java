package com.myownb3.piranha.grid.gridelement;

import com.myownb3.piranha.grid.Grid;
import com.myownb3.piranha.grid.gridelement.shape.Shape;

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

   public SimpleGridElement(Grid grid, Position endPos, Shape shape) {
      super(grid, endPos, shape);
   }
}
