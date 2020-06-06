package com.myownb3.piranha.core.weapon.tank.engine;

import static java.util.Objects.requireNonNull;

import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.moveables.controller.MoveableController;

public class TankEngineImpl implements TankEngine {

   private MoveableController moveableController;

   private TankEngineImpl(MoveableController moveableController) {
      this.moveableController = requireNonNull(moveableController);
   }

   @Override
   public void moveForward() {
      moveableController.leadMoveable();
   }

   @Override
   public Moveable getMoveable() {
      return moveableController.getMoveable();
   }

   public static class TankEngineBuilder {

      private MoveableController moveableController;

      private TankEngineBuilder() {
         // private
      }

      public TankEngineBuilder withMoveableController(MoveableController moveableController) {
         this.moveableController = moveableController;
         return this;
      }

      public TankEngineImpl build() {
         return new TankEngineImpl(moveableController);
      }

      public static TankEngineBuilder builder() {
         return new TankEngineBuilder();
      }

   }
}
