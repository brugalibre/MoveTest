package com.myownb3.piranha.core.weapon.tank;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankGridElement extends EndPointMoveableImpl implements Tank {

   private Tank tank;
   private DimensionInfo turretDimensionInfo;

   protected TankGridElement(Tank tank, Grid grid, MoveablePostActionHandler handler, DimensionInfo tankDimensionInfo,
         DimensionInfo turretDimensionInfo, int movingIncrement) {
      super(grid, tank.getShape().getCenter(), handler, movingIncrement, tank.getShape(), tankDimensionInfo, tank.getBelligerentParty());
      this.tank = tank;
      this.turretDimensionInfo = turretDimensionInfo;
   }

   @Override
   public List<PathSegment> getPath(DimensionInfo otherDimensionInfo) {
      if (this.dimensionInfo.isWithinHeight(otherDimensionInfo)) {
         return getShape().getHull().getPath();
      } else if (turretDimensionInfo.isWithinHeight(otherDimensionInfo)) {
         return getShape().getTurretShape().getPath();
      }
      return Collections.emptyList();
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
      private double tankheightFromBottom;
      private double turretHeightFromGround;

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

      public TankGridElementBuilder withTankheightFromBottom(double tankheightFromBottom) {
         this.tankheightFromBottom = tankheightFromBottom;
         return this;
      }

      public TankGridElementBuilder withTurretHeightFromBottom(double turretHeightFromBottom) {
         this.turretHeightFromGround = turretHeightFromBottom;
         return this;
      }

      public TankGridElement build() {
         TankShape tankShape = tank.getShape();
         DimensionInfoImpl tankDimension = DimensionInfoBuilder.builder()
               .withDimensionRadius(tankShape.getDimensionRadius())
               .withDistanceToGround(tankShape.getCenter().getZ())
               .withHeightFromBottom(tankheightFromBottom)
               .build();
         DimensionInfoImpl turretDimensionInfo = DimensionInfoBuilder.builder()
               .withDimensionRadius(tankShape.getDimensionRadius())
               .withDistanceToGround(tankShape.getCenter().getZ() + tankheightFromBottom)
               .withHeightFromBottom(turretHeightFromGround)
               .build();
         return new TankGridElement(tank, grid, moveablePostActionHandler, tankDimension, turretDimensionInfo, movingIncrement);
      }

      public static TankGridElementBuilder builder() {
         return new TankGridElementBuilder();
      }
   }
}
