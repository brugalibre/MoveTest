package com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.gun.projectile.descent.DescentHandler;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;

public class DefaultProjectileStrategyHandler implements ProjectileStrategyHandler {

   protected Shape shape;
   private DescentHandler descentHandler;

   protected DefaultProjectileStrategyHandler(Shape shape) {
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
    * Creates a new {@link DefaultProjectileStrategyHandler}
    * 
    * @param shape
    *        the {@link Shape} of the {@link Projectile}
    * @return a new {@link DefaultProjectileStrategyHandler}
    */
   public static DefaultProjectileStrategyHandler of(Shape shape) {
      return new DefaultProjectileStrategyHandler(shape);
   }
}
