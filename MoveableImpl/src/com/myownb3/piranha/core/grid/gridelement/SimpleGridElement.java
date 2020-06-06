package com.myownb3.piranha.core.grid.gridelement;

import static java.util.Objects.isNull;

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

   private SimpleGridElement(Grid grid, Position position) {
      super(grid, position);
   }

   private SimpleGridElement(Grid grid, Position pos, Shape shape) {
      super(grid, pos, shape);
   }

   public static class SimpleGridElementBuilder extends AbstractGridElementBuilder<SimpleGridElement, SimpleGridElementBuilder> {

      private SimpleGridElementBuilder() {
         // private
      }

      public static SimpleGridElementBuilder builder() {
         return new SimpleGridElementBuilder();
      }

      @Override
      protected SimpleGridElementBuilder getThis() {
         return this;
      }

      @Override
      public SimpleGridElement build() {
         if (isNull(shape)) {
            return new SimpleGridElement(grid, position);
         }
         return new SimpleGridElement(grid, position, shape);
      }
   }
}
