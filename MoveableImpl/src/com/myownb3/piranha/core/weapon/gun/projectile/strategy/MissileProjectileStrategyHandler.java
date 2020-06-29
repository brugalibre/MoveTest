package com.myownb3.piranha.core.weapon.gun.projectile.strategy;

import java.util.Optional;

import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.Orientation2PositionHelper;
import com.myownb3.piranha.core.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.weapon.target.TargetGridElementEvaluator;

public class MissileProjectileStrategyHandler extends BulletProjectileStrategyHandler {

   private TargetGridElementEvaluator targetGridElementEvaluator;
   private double rotationSpeed;
   private Orientation2PositionHelper helper;

   private MissileProjectileStrategyHandler(TargetGridElementEvaluator targetGridElementEvaluator, Shape shape) {
      super(shape);
      this.targetGridElementEvaluator = targetGridElementEvaluator;
      this.helper = new Orientation2PositionHelper();
      this.rotationSpeed = 10;
   }

   @Override
   public void handleProjectileStrategy() {
      super.handleProjectileStrategy();
      Optional<TargetGridElement> nearestTargetGridElementOpt = targetGridElementEvaluator.getNearestTargetGridElement(shape.getForemostPosition());
      nearestTargetGridElementOpt.ifPresent(nearestTargetGridElement -> {
         Position newMissilePos = evalNewMissilePosition(nearestTargetGridElement);
         shape.transform(newMissilePos);
      });
   }

   private Position evalNewMissilePosition(TargetGridElement nearestTargetGridElement) {
      Position missilePos = shape.getCenter();
      double angle2Turn = evalAngle2Turn(nearestTargetGridElement, missilePos);
      return missilePos.rotate(angle2Turn);
   }

   private double evalAngle2Turn(TargetGridElement nearestTargetGridElement, Position missilePos) {
      double angleDiff = missilePos.calcAngleRelativeTo(nearestTargetGridElement.getCurrentGridElementPosition());
      return helper.getAngle2Turn(angleDiff, rotationSpeed);
   }

   /**
    * Creates a new {@link MissileProjectileStrategyHandler}
    * 
    * @param targetGridElementEvaluator
    *        the {@link TargetGridElementEvaluator}
    * @param shape
    *        the {@link Projectile} {@link Shape}
    * @return a new {@link MissileProjectileStrategyHandler}
    */
   public static MissileProjectileStrategyHandler of(TargetGridElementEvaluator targetGridElementEvaluator, Shape shape) {
      return new MissileProjectileStrategyHandler(targetGridElementEvaluator, shape);
   }
}
