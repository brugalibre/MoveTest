package com.myownb3.piranha.core.battle.weapon.turret;

import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfoImpl.DimensionInfoBuilder;

public class TurretGridElement extends AbstractGridElement implements Turret {

   private Turret turret;

   private TurretGridElement(Grid grid, Turret turret, DimensionInfo dimensionInfo) {
      super(grid, turret.getShape(), dimensionInfo);
      this.turret = turret;
   }

   @Override
   public void autodetect() {
      turret.autodetect();
   }

   @Override
   public BelligerentParty getBelligerentParty() {
      return turret.getBelligerentParty();
   }

   @Override
   public Shape getShape() {
      return turret.getShape();
   }

   @Override
   public boolean isAcquiring() {
      return turret.isAcquiring();
   }

   @Override
   public boolean isShooting() {
      return turret.isShooting();
   }

   public static class TurretGridElementBuilder {

      private Grid grid;
      private Turret turret;
      private double heightFromBottom;

      private TurretGridElementBuilder() {
         // private
      }

      public TurretGridElementBuilder withGrid(Grid grid) {
         this.grid = grid;
         return this;
      }

      public TurretGridElementBuilder withTurret(Turret turret) {
         this.turret = turret;
         return this;
      }

      public TurretGridElementBuilder withHeightFromBottom(double heightFromBottom) {
         this.heightFromBottom = heightFromBottom;
         return this;
      }

      public TurretGridElement build() {
         Shape turretShape = turret.getShape();
         return new TurretGridElement(grid, turret, DimensionInfoBuilder.builder()
               .withDimensionRadius(turretShape.getDimensionRadius())
               .withHeightFromBottom(heightFromBottom)
               .build());
      }

      public static TurretGridElementBuilder builder() {
         return new TurretGridElementBuilder();
      }
   }
}
