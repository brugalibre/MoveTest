package com.myownb3.piranha.core.battle.weapon.gun.projectile.strategy;

import static com.myownb3.piranha.core.battle.weapon.target.TargetGridElement.isSameGridElementTarget;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElement;
import com.myownb3.piranha.core.battle.weapon.target.TargetGridElementEvaluator;
import com.myownb3.piranha.core.battle.weapon.trajectory.impl.TargetPositionLeadEvaluatorImpl;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.position.Position;
import com.myownb3.piranha.core.statemachine.impl.handler.orientatingstate.Orientation2PositionHelper;

public class MissileProjectileStrategyHandler extends DefaultProjectileStrategyHandler {

   private TargetPositionLeadEvaluatorImpl targetPositionLeadEvaluator;
   private TargetGridElementEvaluator targetGridElementEvaluator;
   private Optional<TargetGridElement> nearestDetectedTargetGridElementOpt;
   private double rotationSpeed;
   private Orientation2PositionHelper helper;

   private MissileProjectileStrategyHandler(TargetGridElementEvaluator targetGridElementEvaluator, Shape shape, int missileVelocity,
         double rotationSpeed) {
      super(shape);
      this.targetGridElementEvaluator = targetGridElementEvaluator;
      this.helper = new Orientation2PositionHelper();
      this.rotationSpeed = rotationSpeed;
      this.targetPositionLeadEvaluator = new TargetPositionLeadEvaluatorImpl(missileVelocity);
      this.nearestDetectedTargetGridElementOpt = Optional.empty();
   }

   @Override
   public void handleProjectileStrategy() {
      super.handleProjectileStrategy();
      Optional<TargetGridElement> currentNearestTargetGridElementOpt =
            targetGridElementEvaluator.getNearestTargetGridElement(shape.getForemostPosition());
      if (hasFirstTimeTargetEvaluated(currentNearestTargetGridElementOpt)) {
         nearestDetectedTargetGridElementOpt = Optional.ofNullable(currentNearestTargetGridElementOpt.get());
      } else if (isSameGridElementTarget(nearestDetectedTargetGridElementOpt, currentNearestTargetGridElementOpt)) {
         TargetGridElement currentTargetGridElement = currentNearestTargetGridElementOpt.get();
         currentTargetGridElement.setPrevAcquiredPos(nearestDetectedTargetGridElementOpt.get().getCurrentGridElementPosition());

         Position newMissilePos = evalNewMissilePosition(currentTargetGridElement);

         shape.transform(newMissilePos);
         updateNearestDetectedTargetGridElem(currentTargetGridElement, newMissilePos);
      } else {
         nearestDetectedTargetGridElementOpt = Optional.empty();
      }
   }

   private void updateNearestDetectedTargetGridElem(TargetGridElement currentTargetGridElement, Position newMissilePos) {
      currentTargetGridElement.setTargetPosWithLead2Acquire(newMissilePos);
      nearestDetectedTargetGridElementOpt = Optional.of(currentTargetGridElement);
   }

   private boolean hasFirstTimeTargetEvaluated(Optional<TargetGridElement> currentNearestTargetGridElementOpt) {
      return !nearestDetectedTargetGridElementOpt.isPresent() && currentNearestTargetGridElementOpt.isPresent();
   }

   private Position evalNewMissilePosition(TargetGridElement nearestTargetGridElement) {
      Position missilePos = shape.getCenter();
      Position targetPos = targetPositionLeadEvaluator.calculateTargetConsideringLead(nearestTargetGridElement, missilePos);
      double angle2Turn = evalAngle2Turn(targetPos, missilePos);
      return missilePos.rotate(angle2Turn);
   }

   private double evalAngle2Turn(Position targetGridElementPosWithLead, Position missilePos) {
      double angleDiff = missilePos.calcAngleRelativeTo(targetGridElementPosWithLead);
      return helper.getAngle2Turn(angleDiff, rotationSpeed);
   }

   /**
    * Creates a new {@link MissileProjectileStrategyHandler}
    * 
    * @param targetGridElementEvaluator
    *        the {@link TargetGridElementEvaluator}
    * @param shape
    *        the {@link Projectile} {@link Shape}
    * @param missileVelocity
    *        the velocity of the missile
    * @param rotationSpeed
    *        the rotation speed of the missile
    * @return a new {@link MissileProjectileStrategyHandler}
    */
   public static MissileProjectileStrategyHandler of(TargetGridElementEvaluator targetGridElementEvaluator, Shape shape, int missileVelocity,
         double rotationSpeed) {
      requireNonNull(targetGridElementEvaluator);
      requireNonNull(shape);
      return new MissileProjectileStrategyHandler(targetGridElementEvaluator, shape, missileVelocity, rotationSpeed);
   }
}
