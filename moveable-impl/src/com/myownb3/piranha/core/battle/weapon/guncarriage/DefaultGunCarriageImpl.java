package com.myownb3.piranha.core.battle.weapon.guncarriage;

import com.myownb3.piranha.core.battle.weapon.gun.Gun;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;

public class DefaultGunCarriageImpl extends AbstractGunCarriage {

   private static final long serialVersionUID = 8545224553428882224L;

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
