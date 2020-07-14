package com.myownb3.piranha.core.battle.weapon.countermeasure;

import com.myownb3.piranha.core.battle.weapon.gun.projectile.Projectile;
import com.myownb3.piranha.core.battle.weapon.tank.Tank;
import com.myownb3.piranha.core.battle.weapon.tank.detector.TankDetector;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.dimension.DimensionInfo;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.gridelement.shape.path.compare.PathSegmentEndComparator;
import com.myownb3.piranha.core.grid.position.Position;

/**
 * The {@link DecoyFlareDeployPosEvaluator} is responsible for evaluating the {@link Position} from which the decoy flares are deployed
 * 
 * @author Dominic
 *
 */
public class DecoyFlareDeployPosEvaluator {

   private double heightFromBottom;
   private double deployDistanceFromHull;

   public DecoyFlareDeployPosEvaluator(DimensionInfo dimensionInfo) {
      this.heightFromBottom = dimensionInfo.getHeightFromBottom();
      this.deployDistanceFromHull = dimensionInfo.getDimensionRadius() * 15;
   }

   /**
    * Evaluates the {@link Position} from which the decoy flares are going to be deployed. This depends mainly of the flank of the
    * {@link Tank} from which a missile was detected
    * 
    * @param detectedProjectilePosition
    *        the {@link Position} of the detected {@link Projectile}
    * @param detectorPos
    *        the {@link Position} of the {@link TankDetector}
    * @param shape2Protect
    *        the shape which is to protect from a missile
    * 
    * @return the {@link Position} from which the decoy flares are going to be deployed
    */
   public Position getDeployFromPosition(Position detectedProjectilePosition, Position detectorPos, Shape shape2Protect) {
      PathSegment nearestPathSegment = getNearestPathSegment2DetectedProjectile(detectedProjectilePosition, shape2Protect);
      return getPathSegmentMiddleToProjectileDirection(detectedProjectilePosition, nearestPathSegment, detectorPos.getZ());
   }

   private static PathSegment getNearestPathSegment2DetectedProjectile(Position detectedProjectilePosition, Shape shape2Protect) {
      return shape2Protect.getPath()
            .stream()
            .sorted(new PathSegmentEndComparator(detectedProjectilePosition))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No nearest PathSegment evaluated!?"));
   }

   private Position getPathSegmentMiddleToProjectileDirection(Position detectedProjectilePosition, PathSegment nearestPathSegment,
         double height) {
      Position positionInPathSegmentMiddle = getPositionInPathSegmentMiddle(nearestPathSegment);
      double angle2DetectedProjectile = positionInPathSegmentMiddle.calcAngleRelativeTo(detectedProjectilePosition);
      return positionInPathSegmentMiddle
            .rotate(angle2DetectedProjectile)
            .raise(heightFromBottom + height)
            .movePositionBackward4Distance(deployDistanceFromHull);
   }

   private Position getPositionInPathSegmentMiddle(PathSegment nearestPathSegment) {
      double angle2BeginPos = nearestPathSegment.getEnd().calcAngleRelativeTo(nearestPathSegment.getBegin());
      return nearestPathSegment.getEnd()
            .rotate(angle2BeginPos)
            .movePositionForward4Distance(nearestPathSegment.getLenght() / 2d);
   }
}
