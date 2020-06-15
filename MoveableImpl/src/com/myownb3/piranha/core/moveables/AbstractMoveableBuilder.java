/**
 * 
 */
package com.myownb3.piranha.core.moveables;

import java.util.Objects;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
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
   protected Position position;
   protected Grid grid;
   protected Shape shape;
   protected int velocity;

   protected AbstractMoveableBuilder() {
      velocity = 1;
      handler = (b) -> {
      };
   }

   public AbstractMoveableBuilder<V, T> withShape(Shape shape) {
      this.shape = shape;
      return this;
   }

   public AbstractMoveableBuilder<V, T> withPosition(Position position) {
      this.position = position;
      return this;
   }

   public AbstractMoveableBuilder<V, T> withGrid(Grid grid) {
      this.grid = grid;
      return getThis();
   }

   public AbstractMoveableBuilder<V, T> withVelocity(int velocity) {
      this.velocity = velocity;
      return getThis();
   }

   public AbstractMoveableBuilder<V, T> withHandler(MoveablePostActionHandler handler) {
      this.handler = Objects.requireNonNull(handler, "A Moveable always needs a MoveablePostActionHandler!");
      return this;
   }

   public abstract V build();

   protected abstract AbstractMoveableBuilder<V, T> getThis();

   public static class SimpleMoveable extends AbstractMoveable {

      private SimpleMoveable(Grid grid, Position position, MoveablePostActionHandler handler, Shape shape, int velocity) {
         super(grid, position, handler, shape, velocity);
      }
   }

   public static class MoveableBuilder extends AbstractMoveableBuilder<SimpleMoveable, MoveableBuilder> {

      public SimpleMoveable build() {
         moveable = new SimpleMoveable(grid, position, handler, shape, velocity);
         handler.handlePostConditions(moveable);
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
