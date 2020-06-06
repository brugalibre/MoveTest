package com.myownb3.piranha.core.weapon.tank;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.tank.engine.TankEngine;
import com.myownb3.piranha.core.weapon.tank.shape.TankShape;
import com.myownb3.piranha.core.weapon.tank.shape.TankShapeImpl.TankShapeBuilder;
import com.myownb3.piranha.core.weapon.turret.Turret;

public class TankImpl implements Tank {

   private Turret turret;
   private TankEngine tankEngine;
   private TankShape tankShape;

   private TankImpl(Turret turret, TankEngine tankEngine, TankShape tankShape) {
      this.turret = turret;
      this.tankEngine = tankEngine;
      this.tankShape = tankShape;
   }

   @Override
   public void autodetect() {
      turret.autodetect();
   }

   @Override
   public Turret getTurret() {
      return turret;
   }

   @Override
   public TankShape getShape() {
      return tankShape;
   }

   @Override
   public Position getPosition() {
      return tankShape.getCenter();
   }

   public static final class TankBuilder {

      private Turret turret;
      private TankEngine tankEngine;
      private Shape tankHull;

      private TankBuilder() {
         // private
      }

      public TankBuilder withTurret(Turret turret) {
         this.turret = turret;
         return this;
      }

      public TankBuilder withHull(Shape tankHull) {
         this.tankHull = tankHull;
         return this;
      }

      public TankImpl build() {
         TankShape tankShape = TankShapeBuilder.builder()
               .withHull(tankHull)
               .withTurretShape(turret.getShape())
               .build();
         return new TankImpl(turret, tankEngine, tankShape);
      }

      public static TankBuilder builder() {
         return new TankBuilder();
      }

   }

}
