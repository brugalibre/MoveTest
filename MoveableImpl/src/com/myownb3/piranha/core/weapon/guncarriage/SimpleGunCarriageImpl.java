package com.myownb3.piranha.core.weapon.guncarriage;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.Gun;

public class SimpleGunCarriageImpl extends AbstractGunCarriage {

   private SimpleGunCarriageImpl(Shape shape, Gun gun, double rotationSpeed) {
      super(shape, gun, rotationSpeed);
   }

   public static class SimpleGunCarriageBuilder extends AbstractGunCarriageBuilder<SimpleGunCarriageImpl> {

      private SimpleGunCarriageBuilder() {
         super();
      }

      @Override
      public SimpleGunCarriageImpl build() {
         return new SimpleGunCarriageImpl(shape, gun, rotationSpeed);
      }

      public static SimpleGunCarriageBuilder builder() {
         return new SimpleGunCarriageBuilder();
      }
   }
}
