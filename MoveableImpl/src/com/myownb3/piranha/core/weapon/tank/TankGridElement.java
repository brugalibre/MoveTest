package com.myownb3.piranha.core.weapon.tank;

import static java.util.Objects.nonNull;

import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.moveables.Moveable;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankGridElement implements Tank, GridElement {

   private Tank tank;
   private Moveable moveable;

   public TankGridElement(Grid grid, Tank tank) {
      this.tank = tank;
      this.moveable = tank.getTankEngine().getMoveable();
      ((AbstractShape) moveable.getShape()).setGridElement(moveable);
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
   public Position getForemostPosition() {
      return moveable.getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {
      return moveable.getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return moveable.getDimensionRadius();
   }

   @Override
   public void hasGridElementDetected(GridElement gridElement, Detector detector) {
      moveable.hasGridElementDetected(gridElement, detector);
   }

   @Override
   public boolean isDetectedBy(Position detectionPos, Detector detector) {
      return moveable.isDetectedBy(detectionPos, detector);
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      return moveable.check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public void onCollision(List<GridElement> gridElements) {
      tank.onCollision(gridElements);
   }

   @Override
   public Position getPosition() {
      return tank.getPosition();
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

      private Grid grid;
      private Tank tank;
      private Moveable moveable;

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

      public TankGridElementBuilder withTankMoveable(Moveable moveable) {
         this.moveable = moveable;
         return this;
      }

      public TankGridElement build() {
         if (nonNull(moveable)) {
            TankGridElement tankGridElement = new TankGridElement(grid, tank);
            tankGridElement.moveable = moveable;
            return tankGridElement;
         }
         return new TankGridElement(grid, tank);
      }

      public static TankGridElementBuilder builder() {
         return new TankGridElementBuilder();
      }

   }
}
