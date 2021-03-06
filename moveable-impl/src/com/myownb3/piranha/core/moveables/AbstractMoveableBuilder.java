/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import static com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder.getDefaultDimensionInfo;

import java.util.Objects;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;

/**
 * The {@link MoveableBuilder} is a tool to build a {@link SimpleMoveable}
 * 
 * @author Dominic
 *
 */
public abstract class AbstractMoveableBuilder<V extends AbstractMoveable, T extends AbstractMoveableBuilder<V, T>> {

   protected AbstractMoveable moveable;
   protected MoveablePostActionHandler handler;
   protected Grid grid;
   protected Shape shape;
   protected int velocity;

   protected AbstractMoveableBuilder() {
      velocity = 1;
      handler = moveableIn -> true;
   }

   public T withShape(Shape shape) {
      this.shape = shape;
      return getThis();
   }

   public T withGrid(Grid grid) {
      this.grid = grid;
      return getThis();
   }

   public T withVelocity(int velocity) {
      this.velocity = velocity;
      return getThis();
   }

   public T withHandler(MoveablePostActionHandler handler) {
      this.handler = Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
      return getThis();
   }

   public abstract V build();

   protected abstract T getThis();

   public static class SimpleMoveable extends AbstractMoveable {

      private SimpleMoveable(Grid grid, MoveablePostActionHandler handler, Shape shape, int velocity) {
         super(grid, handler, shape, getDefaultDimensionInfo(shape.getDimensionRadius()), velocity);
      }
   }

   public static class MoveableBuilder extends AbstractMoveableBuilder<SimpleMoveable, MoveableBuilder> {

      @Override
      public SimpleMoveable build() {
         moveable = new SimpleMoveable(grid, handler, shape, velocity);
         handler.handlePostConditions(moveable);
         grid.addElement(moveable);
         return (SimpleMoveable) this.moveable;
      }

      public static MoveableBuilder builder() {
         return new MoveableBuilder();
      }

      @Override
      protected MoveableBuilder getThis() {
         return this;
      }
   }
}
