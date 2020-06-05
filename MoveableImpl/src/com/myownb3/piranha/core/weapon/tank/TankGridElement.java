package com.myownb3.piranha.core.weapon.tank;

import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.moveables.AbstractMoveable;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankGridElement extends AbstractMoveable implements Tank {

   private Tank tank;

   public TankGridElement(Grid grid, Tank tank) {
      super(grid, tank.getPosition(), tank.getShape());
      this.tank = tank;
   }

   @Override
   public TankShape getShape() {
      return tank.getShape();
   }

   @Override
   public Turret getTurret() {
      return tank.getTurret();
   }

   @Override
   public void autodetect() {
      tank.autodetect();
   }

   public static class TankGridElementBuilder {

      private Grid grid;
      private Tank tank;

      private TankGridElementBuilder() {
         // private
      }

      public TankGridElementBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TankGridElementBuilder withTank(Tank tank) {
         this.tank = tank;
         return this;
      }

      public TankGridElement build() {
         return new TankGridElement(grid, tank);
      }

      public static TankGridElementBuilder builder() {
         return new TankGridElementBuilder();
      }
   }
}
