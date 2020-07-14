package com.myownb3.piranha.core.battle.weapon.turret.shape;

import static com.myownb3.piranha.core.grid.gridelement.shape.ShapeUtil.combinePath;

import java.util.Collections;
import java.util.List;

import com.myownb3.piranha.core.battle.weapon.gun.shape.GunShape;
import com.myownb3.piranha.core.battle.weapon.guncarriage.GunCarriage;
import com.myownb3.piranha.core.collision.CollisionDetectionHandler;
import com.myownb3.piranha.core.collision.CollisionDetectionResult;
import com.myownb3.piranha.core.detector.Detector;
import com.myownb3.piranha.core.grid.gridelement.GridElement;
import com.myownb3.piranha.core.grid.gridelement.position.PositionTransformator;
import com.myownb3.piranha.core.grid.gridelement.position.Positions;
import com.myownb3.piranha.core.grid.gridelement.shape.AbstractShape;
import com.myownb3.piranha.core.grid.gridelement.shape.Shape;
import com.myownb3.piranha.core.grid.gridelement.shape.path.PathSegment;
import com.myownb3.piranha.core.grid.position.Position;

public class TurretShapeImpl extends AbstractShape implements TurretShape {

   private GunCarriage gunCarriage;
   private PositionTransformator positionTransformator;

   private TurretShapeImpl(List<PathSegment> path, GunCarriage gunCarriage, PositionTransformator positionTransformator) {
      super(path, gunCarriage.getShape().getCenter());
      this.gunCarriage = gunCarriage;
      this.positionTransformator = positionTransformator;
      this.collisionDetector = buildCollisionDetector();
   }

   @Override
   public void setGridElement(GridElement gridElement) {
      super.setGridElement(gridElement);
      ((AbstractShape) getGunCarriageShape()).setGridElement(gridElement);
      ((AbstractShape) getGunShape()).setGridElement(gridElement);
   }

   @Override
   public CollisionDetectionResult check4Collision(CollisionDetectionHandler collisionDetectionHandler, Position newPosition,
         List<GridElement> gridElements2Check) {
      CollisionDetectionResult collisionWithGunCarriageDetectionResult =
            getGunCarriageShape().check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
      if (collisionWithGunCarriageDetectionResult.isCollision()) {
         return collisionWithGunCarriageDetectionResult;
      }
      return getGunShape().check4Collision(collisionDetectionHandler, newPosition, gridElements2Check);
   }

   @Override
   public Position getForemostPosition() {
      return getGunShape().getForemostPosition();
   }

   @Override
   public Position getRearmostPosition() {
      return gunCarriage.getShape().getRearmostPosition();
   }

   @Override
   public double getDimensionRadius() {
      return Math.max(getGunCarriageShape().getDimensionRadius(), getGunShape().getDimensionRadius());
   }

   @Override
   public void transform(Position position) {
      Position transformedTankPos4Turret = positionTransformator.transform(position);
      Position newTurretPos = getNewTurretPosButKeepDirection(transformedTankPos4Turret);
      super.transform(newTurretPos);
      this.gunCarriage.evalAndSetPosition(newTurretPos);
      this.path = combinePath(getGunShape(), getGunCarriageShape());
   }

   private Position getNewTurretPosButKeepDirection(Position newTurretOriginPos) {
      return Positions.of(getCenter().getDirection(), newTurretOriginPos);
   }

   @Override
   protected List<Position> buildPath4Detection() {
      return Collections.emptyList();// not needed because we override 'detectObject'
   }

   @Override
   public boolean detectObject(Position detectorPosition, Detector detector) {
      boolean isGunCarriageDetected = getGunCarriageShape().detectObject(detectorPosition, detector);
      if (!isGunCarriageDetected) {
         return getGunShape().detectObject(detectorPosition, detector);
      }
      return true;
   }

   @Override
   public Position getCenter() {
      return getGunCarriageShape().getCenter();
   }

   @Override
   public GunShape getGunShape() {
      return gunCarriage.getGun().getShape();
   }

   @Override
   public Shape getGunCarriageShape() {
      return gunCarriage.getShape();
   }

   public static final class TurretShapeBuilder {
      private GunCarriage gunCarriage;
      private PositionTransformator positionTransformator;

      private TurretShapeBuilder() {
         // private
      }

      public TurretShapeBuilder wighGunCarriage(GunCarriage gunCarriage) {
         this.gunCarriage = gunCarriage;
         return this;
      }

      public TurretShapeBuilder wighPositionTransformator(PositionTransformator positionTransformator) {
         this.positionTransformator = positionTransformator;
         return this;
      }

      public TurretShapeImpl build() {
         GunShape gunShape = gunCarriage.getGun().getShape();
         List<PathSegment> combinedPath = combinePath(gunShape, gunCarriage.getShape());
         return new TurretShapeImpl(combinedPath, gunCarriage, positionTransformator);
      }

      public static TurretShapeBuilder builder() {
         return new TurretShapeBuilder();
      }
   }
}
