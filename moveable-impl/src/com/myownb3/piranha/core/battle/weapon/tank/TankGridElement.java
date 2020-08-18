package com.myownb3.piranha.core.battle.weapon.tank;

import static java.util.Objects.nonNull;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.battle.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.battle.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.battle.weapon.turret.Turret;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.moveables.endposition.EndPointMoveableImpl;
import com.myownb3.piranha.core.moveables.postaction.MoveablePostActionHandler;
import com.myownb3.piranha.core.statemachine.EvasionStateMachine;

public class TankGridElement extends EndPointMoveableImpl implements Tank {

   private Tank tank;
   private DimensionInfo turretDimensionInfo;

   protected TankGridElement(Tank tank, Grid grid, EvasionStateMachine evasionStateMachine, DimensionInfo tankDimensionInfo,
         DimensionInfo turretDimensionInfo, int movingIncrement) {
      super(grid, evasionStateMachine, getHandler(evasionStateMachine), movingIncrement, tank.getShape(), tankDimensionInfo,
            tank.getBelligerentParty());
      this.tank = tank;
      this.turretDimensionInfo = turretDimensionInfo;
   }

   private static MoveablePostActionHandler getHandler(EvasionStateMachine evasionStateMachine) {
      return nonNull(evasionStateMachine) ? evasionStateMachine : moveable -> true;
   }

   @Override
   public List<PathSegment> getPath(GridElement gridElement) {
      double ourDistanceFromTheGround = getOurDistanceFromTheGround();
      if (this.dimensionInfo.isWithinHeight(ourDistanceFromTheGround, gridElement.getPosition().getZ())) {
         return getShape().getHull().getPath();
      } else {
         double turretDistanceFromGround = ourDistanceFromTheGround + dimensionInfo.getHeightFromBottom();
         if (turretDimensionInfo.isWithinHeight(turretDistanceFromGround, gridElement.getPosition().getZ())) {
            return getShape().getTurretShape().getPath();
         }
      }
      return Collections.emptyList();
   }

   private double getOurDistanceFromTheGround() {
      return tank.getShape().getCenter().getZ();
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return tank.getBelligerentParty();
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

   public static class TankGridElementBuilder {

      private Tank tank;
      private Grid grid;
      private EvasionStateMachine evasionStateMachine;
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

      public TankGridElementBuilder withEvasionStateMachine(EvasionStateMachine evasionStateMachine) {
         this.evasionStateMachine = evasionStateMachine;
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
               .withHeightFromBottom(tankheightFromBottom)
               .build();
         DimensionInfoImpl turretDimensionInfo = DimensionInfoBuilder.builder()
               .withDimensionRadius(tankShape.getTurretShape().getDimensionRadius())
               .withHeightFromBottom(turretHeightFromGround)
               .build();
         TankGridElement tankGridElement = new TankGridElement(tank, grid, evasionStateMachine, tankDimension, turretDimensionInfo, movingIncrement);
         grid.addElement(tankGridElement);
         return tankGridElement;
      }

      public static TankGridElementBuilder builder() {
         return new TankGridElementBuilder();
      }
   }
}
