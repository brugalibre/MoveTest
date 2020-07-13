package com.myownb3.piranha.core.weapon.guncarriage;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.weapon.gun.Gun;

public class DefaultGunCarriageImpl extends AbstractGunCarriage {

   private DefaultGunCarriageImpl(Shape shape, Gun gun, double rotationSpeed) {
      super(shape, gun, rotationSpeed);
   }

   public static class DefaultGunCarriageBuilder extends AbstractGunCarriageBuilder<DefaultGunCarriageImpl> {

      private DefaultGunCarriageBuilder() {
         super();
      }

      @Override
      public DefaultGunCarriageImpl build() {
         return new DefaultGunCarriageImpl(shape, gun, rotationSpeed);
      }

      public static DefaultGunCarriageBuilder builder() {
         return new DefaultGunCarriageBuilder();
      }
   }
}
