package com.myownb3.piranha.core.grid.gridelement;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import java.util.Objects;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;

/**
 * The {@link SimpleGridElement} provides the most basic functions of a {@link GridElement}
 * 
 * @author Dominic
 *
 */
public final class SimpleGridElement extends AbstractGridElement {

   private SimpleGridElement(Grid grid, Shape shape, DimensionInfo dimensionInfo) {
      super(shape, dimensionInfo);
      grid.addElement(this);
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
         Objects.requireNonNull(shape, "A SimpleGridElement needs a Shape!");
         return new SimpleGridElement(grid, shape, getDefaultDimensionInfo(shape.getDimensionRadius()));
      }
   }
}
