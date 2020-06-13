package com.myownb3.piranha.core.moveables;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.weapon.AutoDetectable;

public class AutoMoveable extends AbstractMoveable implements AutoDetectable {

   public AutoMoveable(Grid grid, Position position, MoveablePostActionHandler handler, Shape shape) {
      super(grid, position, handler, shape);
   }

   @Override
   public void autodetect() {
      moveForward();
   }

   public static class AutoMoveableBuilder extends AbstractMoveableBuilder<AutoMoveable, AutoMoveableBuilder> {

      public AutoMoveableBuilder() {
         super();
      }

      public AutoMoveable build() {
         moveable = new AutoMoveable(grid, position, handler, shape);
         handler.handlePostConditions(moveable);
         return (AutoMoveable) this.moveable;
      }

      public static AutoMoveableBuilder builder() {
         return new AutoMoveableBuilder();
      }

      @Override
      protected AutoMoveableBuilder getThis() {
         return this;
      }
   }
}