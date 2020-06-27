package com.myownb3.piranha.core.weapon.gun.projectile.strategy;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.weapon.gun.projectile.DescentHandler;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;

public class BulletProjectileStrategyHandler implements ProjectileStrategyHandler {

   protected Shape shape;
   private DescentHandler descentHandler;

   protected BulletProjectileStrategyHandler(Shape shape) {
      this.shape = shape;
      this.descentHandler = buildDecentHandler(shape.getCenter(), shape.getDimensionRadius());
   }

   @Override
   public void handleProjectileStrategy() {
      Position position = descentHandler.evlPositionForNewHeight(shape.getCenter());
      shape.transform(position);
   }

   private DescentHandler buildDecentHandler(Position position, double dimensionRadius) {
      return new DescentHandler(position, dimensionRadius * 10, 0);
   }

   /**
    * Creates a new {@link BulletProjectileStrategyHandler}
    * 
    * @param shape
    *        the {@link Shape} of the {@link Projectile}
    * @return a new {@link BulletProjectileStrategyHandler}
    */
   public static BulletProjectileStrategyHandler of(Shape shape) {
      return new BulletProjectileStrategyHandler(shape);
   }
}
