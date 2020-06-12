package com.myownb3.piranha.core.weapon.tank;

import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankGridElement extends EndPointMoveableImpl implements Tank, GridElement {

   private Tank tank;

   protected TankGridElement(Tank tank, Grid grid, MoveablePostActionHandler handler, int movingIncrement) {
      super(grid, tank.getShape().getCenter(), handler, movingIncrement, tank.getShape(), tank.getBelligerentParty());
      this.tank = tank;
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return tank.getBelligerentParty();
   }

   @Override
   public boolean isEnemy(Belligerent otherBelligerent) {
      return tank.isEnemy(otherBelligerent);
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

   @Override
   public boolean isDestroyed() {
      return tank.isDestroyed();
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      tank.onCollision(gridElements);
   }

   @Override
   public TankEngine getTankEngine() {
      return tank.getTankEngine();
   }

   @Override
   public boolean isAvoidable() {
      return true;
   }

   public static class TankGridElementBuilder {

      private Tank tank;
      private Grid grid;
      private MoveablePostActionHandler moveablePostActionHandler;
      private int movingIncrement;

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

      public TankGridElementBuilder withEngineVelocity(int movingIncrement) {
         this.movingIncrement = movingIncrement;
         return this;
      }

      public TankGridElementBuilder withMoveablePostActionHandler(MoveablePostActionHandler moveablePostActionHandler) {
         this.moveablePostActionHandler = moveablePostActionHandler;
         return this;
      }

      public TankGridElement build() {
         return new TankGridElement(tank, grid, moveablePostActionHandler, movingIncrement);
      }

      public static TankGridElementBuilder builder() {
         return new TankGridElementBuilder();
      }

   }
}
