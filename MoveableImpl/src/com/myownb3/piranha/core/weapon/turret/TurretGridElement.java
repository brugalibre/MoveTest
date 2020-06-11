package com.myownb3.piranha.core.weapon.turret;

import com.myownb3.piranha.core.battle.belligerent.Belligerent;
import com.myownb3.piranha.core.battle.belligerent.party.BelligerentParty;
import com.myownb3.piranha.core.grid.Grid;
import com.myownb3.piranha.core.grid.gridelement.AbstractGridElement;
import com.myownb3.piranha.core.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.weapon.turret.shape.TurretShape;
import com.myownb3.piranha.core.weapon.turret.states.TurretState;

public class TurretGridElement extends AbstractGridElement implements Turret {

   private Turret turret;

   private TurretGridElement(Grid grid, Turret turret) {
      super(grid, turret.getShape().getCenter(), turret.getShape());
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
   public boolean isEnemy(Belligerent otherBelligerent) {
      return turret.isEnemy(otherBelligerent);
   }

   @Override
   public GunCarriage getGunCarriage() {
      return turret.getGunCarriage();
   }

   @Override
   public TurretState getTurretStatus() {
      return turret.getTurretStatus();
   }

   @Override
   public TurretShape getShape() {
      return turret.getShape();
   }

   @Override
   public boolean isShooting() {
      return turret.isShooting();
   }

   @Override
   public boolean isAvoidable() {
      return true;
   }

   public static class TurretGridElementBuilder {

      private Grid grid;
      private Turret turret;

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

      public TurretGridElement build() {
         return new TurretGridElement(grid, turret);
      }

      public static TurretGridElementBuilder builder() {
         return new TurretGridElementBuilder();
      }
   }
}
